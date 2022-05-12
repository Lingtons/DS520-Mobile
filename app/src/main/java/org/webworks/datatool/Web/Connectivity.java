package org.webworks.datatool.Web;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {


    /**
     * Method checks if device is connected to a network
     * */
	public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;    
    }

    /**
     * Method checks if the device is connected to the internet and internet is available
     * */
    public static boolean isInternetAvailable() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
