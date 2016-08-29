package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import adapters.SectionedSettingsAdapter;
import adapters.SettingsAdapter;
import helpers.ItemDecorator;


/**
 * Created by XPS on 08/28/2016.
 */
public class SettingsActivity extends DriveOperationsActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.settings_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SectionedSettingsAdapter.Section> sections =  new ArrayList<>();
        sections.add(new SectionedSettingsAdapter.Section(0,getString(R.string.backup)));

        SettingsAdapter settingsAdapter = new SettingsAdapter(this);
        SectionedSettingsAdapter mSectionedAdapter =
                new SectionedSettingsAdapter(this,R.layout.section_layout,
                R.id.sectionTittle,settingsAdapter);

        SectionedSettingsAdapter.Section[] dummy =
                new SectionedSettingsAdapter.Section[sections.size()];
        SectionedSettingsAdapter.Section[] sectionsArray = sections.toArray(dummy);
        mSectionedAdapter.setSections(sectionsArray);
        recyclerView.addItemDecoration(new ItemDecorator(this,sectionsArray));

        recyclerView.setAdapter(mSectionedAdapter);
    }
}
