package adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import activities.DriveOperationsActivity;
import helpers.DriveTasksGenerator;
import models.Tags;

/**
 * Created by XPS on 08/28/2016.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private DriveOperationsActivity mContextActivity;
    private String[] mSettingProperties;
    private String[] mSettingSummaries;

    public SettingsAdapter(DriveOperationsActivity contextActivity) {
        mContextActivity = contextActivity;
        mSettingProperties = new String[]{
                mContextActivity.getString(R.string.current_account),
                mContextActivity.getString(R.string.backup_data),
                mContextActivity.getString(R.string.restore_data)
        };
        mSettingSummaries = getSettingsSummaries();
    }

    private String[] getSettingsSummaries(){
        SharedPreferences pref = mContextActivity.getSharedPreferences(
                Tags.APP_DATA, Context.MODE_PRIVATE);

        String backupAccount = pref.getString(Tags.PREF_ACCOUNT_NAME,mContextActivity.
                getString(R.string.no_backup_info));

        StringBuilder lastBackupSummaries = new StringBuilder();
        lastBackupSummaries.append(mContextActivity.getString(R.string.sync_date)).append(" ");
        String lastBackupDate = pref.getString(Tags.LAST_BACKUP_DATE,mContextActivity.
                getString(R.string.no_backup_info));
        lastBackupSummaries.append(lastBackupDate);

        StringBuilder lastModifSummaries = new StringBuilder();
        lastModifSummaries.append(mContextActivity.getString(R.string.local_date)).append(" ");
        java.io.File file = new java.io.File(Environment.getDataDirectory().getPath()
                + "/data/com.study.xps.projectdictionary/databases/Dictionaries.db");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d = new Date(file.lastModified());
        lastModifSummaries.append(dateFormat.format(d));

        String[] summaries = new String[]{
                backupAccount,lastModifSummaries.toString(),lastBackupSummaries.toString()
        };
        return summaries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContextActivity);
        View settingsItem = inflater.inflate(R.layout.settings_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(settingsItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemView.setTag(position);
        holder.mItemTitle.setText(mSettingProperties[position]);
        holder.mItemSummary.setText(mSettingSummaries[position]);
    }

    @Override
    public int getItemCount() { return mSettingProperties.length; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView mItemTitle;
        public TextView mItemSummary;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mItemView.setOnClickListener(mItemClickListener);
            this.mItemTitle = (TextView) mItemView.findViewById(R.id.settings_item_title);
            this.mItemSummary = (TextView) mItemView.findViewById(R.id.settings_item_summary);
        }

        View.OnClickListener mItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                switch (pos){
                    case 0:
                        if(mContextActivity.initDriveHelper()) {
                            mContextActivity.chooseAnotherAccount();
                        }
                        break;
                    case 1:
                        mContextActivity.executeDriveTask(
                                DriveTasksGenerator.DriveTask.APP_FOLDER_BACKUP_TASK);
                        break;
                    case 2:
                        mContextActivity.executeDriveTask(
                               DriveTasksGenerator.DriveTask.APP_FOLDER_RESTORE_TASK);
                        break;
                }
            }
        };
    }
}
