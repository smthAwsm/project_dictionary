package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

/**
 * Created by XPS on 08/28/2016.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private Context mContextActivity;
    private String[] mSettingProperties;
    private String[] mSettingSummaries;

    public SettingsAdapter(Context contextActivity) {
        mContextActivity = contextActivity;
        mSettingProperties = new String[]{
                mContextActivity.getString(R.string.current_account),
                mContextActivity.getString(R.string.backup_data),
                mContextActivity.getString(R.string.restore_data)
        };

        mSettingSummaries = new String[]{
                "", mContextActivity.getString(R.string.sync_date),
                mContextActivity.getString(R.string.local_date)
        };
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
        holder.mItemTitle.setText(mSettingProperties[position]);
        holder.mItemSummary.setText(mSettingSummaries[position]);
    }

    @Override
    public int getItemCount() { return mSettingProperties.length; }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View mItemView;
        public TextView mItemTitle;
        public TextView mItemSummary;

        public ViewHolder(View itemView) {
            super(itemView);

            this.mItemView = itemView;
            this.mItemTitle = (TextView) mItemView.findViewById(R.id.settings_item_title);
            this.mItemSummary = (TextView) mItemView.findViewById(R.id.settings_item_summary);
        }
    }
}
