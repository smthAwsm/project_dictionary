package Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import Fragments.TestingBeginnerFragment;
import Fragments.TestingNormalFragment;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestSwipeAdapterNormal extends FragmentStatePagerAdapter {

    Set<Integer> generated;
    List<Word> wordsInfo;

    public TestSwipeAdapterNormal(FragmentManager fm, List<Word> wordData) {
        super(fm);
        wordsInfo = wordData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TestingNormalFragment();
        getRandomWords(position);
       // List<Integer> randomList = new ArrayList<Integer>();
        //randomList.addAll(generated);
        Integer[] array = generated.toArray(new Integer[0]);

        Bundle bundle = new Bundle();
        bundle.putString(Tags.WORD_VALUE_TAG, wordsInfo.get(position).getValue());
        bundle.putString(Tags.WORD_TRANSLATE_TAG, wordsInfo.get(position).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_1,wordsInfo.get(array[0]).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_2,wordsInfo.get(array[1]).getTranslation());
        bundle.putString(Tags.WORD_VALUE_FAKE_3,wordsInfo.get(array[2]).getTranslation());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return wordsInfo.size();
    }

    private void getRandomWords(int position){
        Random rng = new Random();

        generated = new LinkedHashSet<Integer>();
        while (generated.size() < 3)
        {
            Integer next = rng.nextInt(wordsInfo.size());
            if (next != position)
            generated.add(next);
        }
    }
}
