package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.study.xps.projectdictionary.R;
import adapters.TestSwipeAdapterAdvanced;
import adapters.TestSwipeAdapterBeginner;
import adapters.TestSwipeAdapterNormal;
import models.Tags;
import models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingActivity extends AppCompatActivity {

    private long mCurrentTopicID;
    private int mTestingType;
    private List<Word> mWordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_activity);

        mTestingType = getIntent().getIntExtra(Tags.TESTING_TYPE_TAG,0);
        mCurrentTopicID = getIntent().getLongExtra(Tags.TOPIC_TAG, 0);

        try {
            mWordList = Word.find(Word.class, "topic_ID = "+ mCurrentTopicID +"");
        } catch (Exception e){
            mWordList = new ArrayList<>();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.testingBeginViewPager);

        switch (mTestingType){
            case 0:
                TestSwipeAdapterBeginner swipeAdapterBeginner = new TestSwipeAdapterBeginner(
                        getSupportFragmentManager(), mWordList);
                viewPager.setAdapter(swipeAdapterBeginner);
                break;
            case 1:
                TestSwipeAdapterNormal swipeAdapterNormal = new TestSwipeAdapterNormal(
                        getSupportFragmentManager(), mWordList);
                viewPager.setAdapter(swipeAdapterNormal);
                break;
            case 2:
                TestSwipeAdapterAdvanced swipeAdapterAdvanced = new TestSwipeAdapterAdvanced(
                        getSupportFragmentManager(), mWordList);
                viewPager.setAdapter(swipeAdapterAdvanced);
                break;
        }
    }
}
