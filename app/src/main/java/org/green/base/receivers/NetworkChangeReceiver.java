package org.green.base.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;


import org.green.R;
import org.green.base.components.AlertTextView;
import org.green.utilities.Network;

/**
 * Created by johan on 19/01/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    AlertTextView alertTextView = new AlertTextView(context);
    if(!Network.isOnline(context)) {
      alertTextView.setTitle(context.getString(R.string.alert_search_no_internet));
    } else {
      alertTextView.setTitle(context.getString(R.string.alert_search_internet));
    }
    int actionBarHeight = 75;
    TypedValue tv = new TypedValue();
    if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }
    alertTextView.show();
    Toast toast = new Toast(context);
    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, actionBarHeight);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(alertTextView);
    toast.show();
  }

  /**
   * disable the broadcastreceiver
   * @param context
   */
  public static void disableNetWorkReceiver(Context context){
    ComponentName component=new ComponentName(context, NetworkChangeReceiver.class);
    context.getPackageManager()
        .setComponentEnabledSetting(component,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP);
  }

  /**
   * enable the broadcastreceiver
   * @param context
   */
  public  static void enableNetworkReceiver(Context context){
    ComponentName component=new ComponentName(context, NetworkChangeReceiver.class);
    context.getPackageManager()
        .setComponentEnabledSetting(component,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP);
  }

}
