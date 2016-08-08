package adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.study.xps.projectdictionary.R;
import dialogs.NewTopicDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by XPS on 4/18/2016.
 */
public class TopicsSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private NewTopicDialog mNewTopicDialog;
    private List<Integer> mIconResouresId;

    public TopicsSpinnerAdapter(Context context, NewTopicDialog newTopicDialog) {
        this.mContext = context;
        this.mNewTopicDialog = newTopicDialog;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = new View(mContext);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View topicImages = inflater.inflate(R.layout.dialog_topic_add_child,null);
        GridView mTopicIconsGrid = (GridView) topicImages.findViewById(R.id.topicImagesGrid);
        mIconResouresId = new ArrayList<>();
        fillIconsId(mIconResouresId);

        TopicImagesGridViewAdapter adapter =
                new TopicImagesGridViewAdapter(mContext, mIconResouresId);
        mTopicIconsGrid.setAdapter(adapter);
        mTopicIconsGrid.setLayoutParams(
                new AbsListView.LayoutParams(mTopicIconsGrid.getLayoutParams()));

        mTopicIconsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                mNewTopicDialog.setTopicImage(mIconResouresId.get(pos));
            }
        });

        return mTopicIconsGrid;
    }

    private void fillIconsId(List<Integer> iconsList){
        int arrayId = mContext.getResources().
                getIdentifier("graphics" , "array", mContext.getPackageName());

        if (arrayId != 0)
        {
            TypedArray recources = mContext.getResources().obtainTypedArray(arrayId);
            for (int i = 0; i < recources.length(); i++ )
                iconsList.add(recources.getResourceId(i,0));
            recources.recycle();
        }
    }
}