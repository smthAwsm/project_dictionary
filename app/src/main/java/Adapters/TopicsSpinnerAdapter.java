package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.Activities.TopicsActivity;
import com.study.xps.projectdictionary.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import Dialog.NewTopicDialog;
import Models.Topic;

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



        TopicImagesGridAdapter adapter = new TopicImagesGridAdapter(context,integers);
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
        integers.add(R.drawable.basket);
        integers.add(R.drawable.binoculars);
        integers.add(R.drawable.brain);
        integers.add(R.drawable.calculator);
        integers.add(R.drawable.calendar);
        integers.add(R.drawable.chat);
        integers.add(R.drawable.clock);
        integers.add(R.drawable.coffee);
        integers.add(R.drawable.coffee_1);
        integers.add(R.drawable.cogwheel);
        integers.add(R.drawable.coins);
        integers.add(R.drawable.compass);
        integers.add(R.drawable.diagram);
        integers.add(R.drawable.diagram_1);
        integers.add(R.drawable.documentation);
        integers.add(R.drawable.dollar_bill);
        integers.add(R.drawable.employee);
        integers.add(R.drawable.employees);
        integers.add(R.drawable.flag);
        integers.add(R.drawable.flask);
        integers.add(R.drawable.form);
        integers.add(R.drawable.glasses);
        integers.add(R.drawable.horse);
        integers.add(R.drawable.idea);
        integers.add(R.drawable.laptop);
        integers.add(R.drawable.lifebuoy);
        integers.add(R.drawable.magnifying_glass);
        integers.add(R.drawable.medal);
        integers.add(R.drawable.megaphone);
        integers.add(R.drawable.microscope);
        integers.add(R.drawable.money_bag);
        integers.add(R.drawable.mortarboard);
        integers.add(R.drawable.mountain);
        integers.add(R.drawable.organization);
        integers.add(R.drawable.paper_plane);
        integers.add(R.drawable.piggy_bank);
        integers.add(R.drawable.pointer);
        integers.add(R.drawable.rocket);
        integers.add(R.drawable.smartphone);
        integers.add(R.drawable.smartphone_1);
        integers.add(R.drawable.speedometer);
        integers.add(R.drawable.store);
        integers.add(R.drawable.suitcase);
        integers.add(R.drawable.target);
        integers.add(R.drawable.team);
        integers.add(R.drawable.telescope);
        integers.add(R.drawable.tie);
        integers.add(R.drawable.trophy);
        integers.add(R.drawable.ufo);
        integers.add(R.drawable.website);
    }

}