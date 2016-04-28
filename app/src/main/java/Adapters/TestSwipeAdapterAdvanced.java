package Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import Fragments.TestingAdvancedFragment;
import Fragments.TestingBeginnerFragment;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestSwipeAdapterAdvanced extends FragmentStatePagerAdapter {

    List<Word> wordsInfo;

    public TestSwipeAdapterAdvanced(FragmentManager fm, List<Word> wordData) {
        super(fm);
        wordsInfo = wordData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TestingAdvancedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Tags.WORD_VALUE_TAG,wordsInfo.get(position).getValue());
        bundle.putString(Tags.WORD_TRANSLATE_TAG,wordsInfo.get(position).getTranslation());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return wordsInfo.size();
    }
}
