package helpers;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by XPS on 5/7/2016.
 */
public interface ActivityDataInterface {

   void updateData();
   void updateViewData();
   List getActivityData();
   FragmentManager getActivityFragmentManager();
}
