package adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.study.xps.projectdictionary.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by XPS on 4/18/2016.
 */
public class TopicImagesGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mTopicIconsList;
    private LayoutInflater mLayoutInflater;
    private Holder mHolder;

    public TopicImagesGridViewAdapter(Context context, List<Integer> imagesID){
        this.mContext = context;
        mTopicIconsList = imagesID;
        mHolder = new Holder();
    }

    @Override
    public int getCount() {
        return mTopicIconsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mLayoutInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridViewItem = mLayoutInflater.
                inflate(R.layout.dialog_topic_child_grid_item,null);

        mHolder.topicImage = (ImageView) gridViewItem.findViewById(R.id.childGridItemImage);
        loadBitmap(mTopicIconsList.get(position), mHolder.topicImage);

        return gridViewItem;
    }

    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }

    private class Holder
    {
        ImageView topicImage;
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> mImageViewWeakReference;
        private int mResId = 0;

        public BitmapWorkerTask(ImageView imageView) {
            mImageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            mResId = params[0];
            return decodeSampledBitmapFromResource(mContext.getResources(), mResId, 50, 50);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mImageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = mImageViewWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res,
                                                      int resId,int reqWidth, int reqHeight) {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeResource(res, resId, options);
        }

        public  int calculateInSampleSize(BitmapFactory.Options options,
                                          int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }
    }
}
