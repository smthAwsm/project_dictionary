package Adapters;

import android.content.Context;
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
        // TODO Auto-generated constructor stub
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
        // TODO Auto-generated method stub
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
        integers.add(R.raw.basket);
        integers.add(R.raw.binoculars);
        integers.add(R.raw.brain);
        integers.add(R.raw.calculator);
        integers.add(R.raw.calendar);
        integers.add(R.raw.chat);
        integers.add(R.raw.clock);
        integers.add(R.raw.coffee);
        integers.add(R.raw.coffee_1);
        integers.add(R.raw.cogwheel);
        integers.add(R.raw.coins);
        integers.add(R.raw.compass);
        integers.add(R.raw.diagram);
        integers.add(R.raw.diagram_1);
        integers.add(R.raw.documentation);
        integers.add(R.raw.dollar_bill);
        integers.add(R.raw.employee);
        integers.add(R.raw.employees);
        integers.add(R.raw.flag);
        integers.add(R.raw.flask);
        integers.add(R.raw.form);
        integers.add(R.raw.glasses);
        integers.add(R.raw.horse);
        integers.add(R.raw.idea);
        integers.add(R.raw.laptop);
        integers.add(R.raw.lifebuoy);
        integers.add(R.raw.magnifying_glass);
        integers.add(R.raw.medal);
        integers.add(R.raw.megaphone);
        integers.add(R.raw.microscope);
        integers.add(R.raw.money_bag);
        integers.add(R.raw.mortarboard);
        integers.add(R.raw.mountain);
        integers.add(R.raw.organization);
        integers.add(R.raw.paper_plane);
        integers.add(R.raw.piggy_bank);
        integers.add(R.raw.pointer);
        integers.add(R.raw.rocket);
        integers.add(R.raw.smartphone);
        integers.add(R.raw.smartphone_1);
        integers.add(R.raw.speedometer);
        integers.add(R.raw.store);
        integers.add(R.raw.suitcase);
        integers.add(R.raw.target);
        integers.add(R.raw.team);
        integers.add(R.raw.telescope);
        integers.add(R.raw.tie);
        integers.add(R.raw.trophy);
        integers.add(R.raw.ufo);
        integers.add(R.raw.website);
    }

}