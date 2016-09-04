package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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
    private SettingsAdapter mSettingsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(getString(R.string.setting_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.settings_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SectionedSettingsAdapter.Section> sections =  new ArrayList<>();
        sections.add(new SectionedSettingsAdapter.Section(0,getString(R.string.backup)));

        mSettingsAdapter = new SettingsAdapter(this);
         SectionedSettingsAdapter sectionedSettingsAdapter =
                 new SectionedSettingsAdapter(this,R.layout.section_layout,
                R.id.sectionTittle,mSettingsAdapter);

        SectionedSettingsAdapter.Section[] dummy =
                new SectionedSettingsAdapter.Section[sections.size()];
        SectionedSettingsAdapter.Section[] sectionsArray = sections.toArray(dummy);
        sectionedSettingsAdapter.setSections(sectionsArray);
        recyclerView.addItemDecoration(new ItemDecorator(this,sectionsArray));

        recyclerView.setAdapter(sectionedSettingsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void updateSummaries(){
      if(mSettingsAdapter != null){
          mSettingsAdapter.updateSummariesContent();
          mSettingsAdapter.notifyDataSetChanged();
      }
    }
}
