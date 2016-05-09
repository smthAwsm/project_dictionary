package Helpers;

import android.app.FragmentManager;

import java.util.List;

/**
 * Created by XPS on 5/7/2016.
 */
public interface ActivityDataInterface {

   void loadAppropriateFragment();
   void updateData();
   void updateViewData();
   List getActivityData();
   FragmentManager getActivityFragmentManager();
}
