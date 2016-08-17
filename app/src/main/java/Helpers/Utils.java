package helpers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.io.IOException;

/**
 * Created by XPS on 07/18/2016.
 */

public class Utils {

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static class IsOnlineTask extends AsyncTask<Void,Void,Boolean> {

        private IsOnlineResultListener mResultListener;

        public IsOnlineTask(IsOnlineResultListener resultListener) {
            this.mResultListener = resultListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e) {
                e.printStackTrace(); }
            catch (InterruptedException e) {
                e.printStackTrace(); }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(mResultListener != null){
                mResultListener.setOnlineStatus(aBoolean);}
        }
    }

    public interface IsOnlineResultListener {
        void setOnlineStatus(boolean result);
    }

    public static boolean isDeviceOnline(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager .getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
