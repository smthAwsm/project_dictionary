package activities;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;



import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.TestSwipeAdapterBeginner;
import Fragments.EmptyFragment;
import Fragments.TestingBeginnerFragment;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingActivity extends AppCompatActivity {

    private long currentTopicID;
    List<Word> wordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_activity);
        currentTopicID = getIntent().getLongExtra(Tags.TOPIC_TAG, 0);

        try {
            wordList = Word.find(Word.class, "topic_ID = "+currentTopicID +"");
        } catch (Exception e){
            wordList = new ArrayList<Word>();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.testingBeginViewPager);
        TestSwipeAdapterBeginner swipeAdapterBeginner = new TestSwipeAdapterBeginner(getSupportFragmentManager(),wordList);
        viewPager.setAdapter(swipeAdapterBeginner);


    }
}
