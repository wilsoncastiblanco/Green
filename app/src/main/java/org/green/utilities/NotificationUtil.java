package org.green.utilities;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtil {

  private static Toast toast;

  /**
   * Singlelton to show a toast
   * @param context Application context
   * @param message Toast message
   */
  public static void makeSingleShowToast(Context context, int message) {
    if (toast != null) {
      toast.cancel();
    }
    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    toast.show();
  }

  /**
   * Show a toast
   * @param context Application context
   * @param message Toast message
   */
  public static void makeToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Show a toast
   * @param context Application context
   * @param stringId String id of the message
   */
  public static void makeToast(Context context, int stringId) {
    Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
  }

}
