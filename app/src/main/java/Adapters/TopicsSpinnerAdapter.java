package Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Dialog.NewTopicDialog;

/**
 * Created by XPS on 4/18/2016.
 */
public class TopicsSpinnerAdapter extends BaseAdapter {
    private Context context;
    List<String> spinnerObject;
    GridView imagesGrid;
    NewTopicDialog dialog;
    List<Integer> integers;

    public TopicsSpinnerAdapter(Context context, NewTopicDialog newTopicDialog,List<String> objects) {
        //super(context, textViewResourceId, objects);
        this.context=context;
        spinnerObject = objects;
        dialog = newTopicDialog;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return spinnerObject.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label=(TextView)convertView;

        if (convertView==null) {
            convertView=new TextView(context);
            label=(TextView)convertView;
            label.setText(spinnerObject.get(position));
        }
        return convertView;
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View topicImages = inflater.inflate(R.layout.dialog_topic_add_child,null);
        imagesGrid = (GridView) topicImages.findViewById(R.id.topicImagesGrid);
        fillIntegers();

        TopicImagesGridViewAdapter adapter = new TopicImagesGridViewAdapter(context,integers);
        imagesGrid.setAdapter(adapter);
        adjustGridView();

        imagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                dialog.setTopicImage(integers.get(pos));

            }
        });

        return imagesGrid;
    }

    private void adjustGridView(){
        imagesGrid.setNumColumns(GridView.AUTO_FIT);
        imagesGrid.setColumnWidth(210);
        imagesGrid.setMinimumHeight(50);
        imagesGrid.setHorizontalSpacing(5);
        imagesGrid.setVerticalSpacing(5);
        imagesGrid.setStretchMode(GridView.STRETCH_SPACING);
    }

    private void fillIntegers(){
        integers = new ArrayList<Integer>();
        List<String> sad = new ArrayList<String >();
        int arrayId = context.getResources().getIdentifier("graphics" , "array", context.getPackageName());

        if (arrayId != 0)
        {
            TypedArray recources = context.getResources().obtainTypedArray(arrayId);
            for (int i = 0; i < recources.length(); i++ )
                integers.add(recources.getResourceId(i,0));
            recources.recycle();
        }
    }
}