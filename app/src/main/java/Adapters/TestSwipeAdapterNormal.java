package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fragments.TestingNormalFragment;
import models.Tags;
import models.Word;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestSwipeAdapterNormal extends FragmentStatePagerAdapter {

    private Set<Integer> mGeneratedWordIndexes;
    private List<Word> mWordsList;

    public TestSwipeAdapterNormal(FragmentManager fm, List<Word> wordData) {
        super(fm);
        mWordsList = wordData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TestingNormalFragment();
        getRandomWords(position);
        Integer[] array = mGeneratedWordIndexes.toArray(new Integer[0]);

        Bundle bundle = new Bundle();
        bundle.putString(Tags.WORD_VALUE_TAG, mWordsList.get(position).getValue());
        bundle.putString(Tags.WORD_TRANSLATE_TAG, mWordsList.get(position).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_1, mWordsList.get(array[0]).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_2, mWordsList.get(array[1]).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_3, mWordsList.get(array[2]).getTranslation());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mWordsList.size();
    }

    private void getRandomWords(int position){
        Random rng = new Random();

        mGeneratedWordIndexes = new HashSet<Integer>();
        while (mGeneratedWordIndexes.size() < 3)
        {
            Integer next = rng.nextInt(mWordsList.size());
            if (next != position)
            mGeneratedWordIndexes.add(next);
        }
    }
}
