package org.green.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

  /**
   * Check if the network is available or not
   * @param context
   * @return
   */
  public static boolean isOnline(Context context) {
    try {
      ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivity != null) {
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null)
          for (int i = 0; i < info.length; i++)
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
              return true;
            }

      }
    } catch (Exception e) {
      return false;
    }

    return false;
  }
}
