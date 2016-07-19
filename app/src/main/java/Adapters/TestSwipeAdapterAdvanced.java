package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.TestingAdvancedFragment;
import models.Tags;
import models.Word;

import java.util.List;


/**
 * Created by XPS on 4/28/2016.
 */
public class TestSwipeAdapterAdvanced extends FragmentStatePagerAdapter {

    private List<Word> mWordsList;

    public TestSwipeAdapterAdvanced(FragmentManager fm, List<Word> wordData) {
        super(fm);
        mWordsList = wordData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TestingAdvancedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Tags.WORD_VALUE_TAG, mWordsList.get(position).getValue());
        bundle.putString(Tags.WORD_TRANSLATE_TAG, mWordsList.get(position).getTranslation());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mWordsList.size();
    }
}
