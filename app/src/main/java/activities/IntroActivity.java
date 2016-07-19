package activities;

/**
 * Created by XPS on 07/18/2016.
 */

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.study.xps.projectdictionary.R;

import adapters.IntroPagerAdapter;
import helpers.Utils;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private IntroPagerAdapter mSectionsPagerAdapter;
    private CoordinatorLayout mCoordinator;
    private ImageButton mNextBtn;
    private Button mSkipBtn, mFinishBtn;
    private ImageView[] mIndicators;
    private int page = 0;   //  to track page position

    int[] colorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }

        colorList = new int[]{
                ContextCompat.getColor(this, R.color.cyan),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.green)
        };

        setContentView(R.layout.activity_pager);
        setupComponents();
    }

    private void setupComponents(){
        mNextBtn = (ImageButton) findViewById(R.id.intro_btn_next);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mNextBtn.setImageDrawable(
                    Utils.tintMyDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chevron_right_24dp), Color.WHITE)
            );
        }
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(page);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mIndicators = new ImageView[]{
                (ImageView) findViewById(R.id.intro_indicator_0),
                (ImageView) findViewById(R.id.intro_indicator_1),
                (ImageView) findViewById(R.id.intro_indicator_2)
        };
        updateIndicators(page);

        mSkipBtn = (Button) findViewById(R.id.intro_btn_skip);
        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),DictionariesActivity.class));
            }
        });

        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),DictionariesActivity.class));
            }
        });

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_content);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position,
                                           float positionOffset, int positionOffsetPixels) {
                    final ArgbEvaluator evaluator = new ArgbEvaluator();
                    int colorUpdate = (Integer) evaluator.evaluate(positionOffset,
                            colorList[position], colorList[position == 2 ? position : position + 1]);
                    mViewPager.setBackgroundColor(colorUpdate);
                }

                @Override
                public void onPageSelected(int position) {
                    page = position;
                    updateIndicators(page);

                    switch (position) {
                        case 0:
                            mViewPager.setBackgroundColor(colorList[0]);
                            break;
                        case 1:
                            mViewPager.setBackgroundColor(colorList[1]);
                            break;
                        case 2:
                            mViewPager.setBackgroundColor(colorList[2]);
                            break;
                    }

                    mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                    mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state) {}
            };

    void updateIndicators(int position) {
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }
}
