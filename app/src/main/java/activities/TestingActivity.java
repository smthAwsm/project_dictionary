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

import Adapters.TestSwipeAdapterAdvanced;
import Adapters.TestSwipeAdapterBeginner;
import Adapters.TestSwipeAdapterNormal;
import Fragments.EmptyFragment;
import Fragments.TestingBeginnerFragment;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingActivity extends AppCompatActivity {

    private long currentTopicID;
    private int testing_type;
    List<Word> wordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_activity);

        testing_type = getIntent().getIntExtra(Tags.TESTING_TYPE_TAG,0);
        currentTopicID = getIntent().getLongExtra(Tags.TOPIC_TAG, 0);

        try {
            wordList = Word.find(Word.class, "topic_ID = "+currentTopicID +"");
        } catch (Exception e){
            wordList = new ArrayList<Word>();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.testingBeginViewPager);

        switch (testing_type){
            case 0:
                TestSwipeAdapterBeginner swipeAdapterBeginner = new TestSwipeAdapterBeginner(getSupportFragmentManager(),wordList);
                viewPager.setAdapter(swipeAdapterBeginner);
                break;
            case 1:
                TestSwipeAdapterNormal swipeAdapterNormal = new TestSwipeAdapterNormal(getSupportFragmentManager(),wordList);
                viewPager.setAdapter(swipeAdapterNormal);
                break;
            case 2:
                TestSwipeAdapterAdvanced swipeAdapterAdvanced = new TestSwipeAdapterAdvanced(getSupportFragmentManager(),wordList);
                viewPager.setAdapter(swipeAdapterAdvanced);
                break;
        }


    }
}
