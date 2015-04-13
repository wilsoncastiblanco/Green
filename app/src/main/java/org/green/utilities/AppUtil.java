package org.green.utilities;


import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppUtil {

  /**
   * Hide views
   * @param views Views to hide
   */
  public static void hideViews(View... views) {
    for (View v : views) {
      toggleViewVisible(v, false, View.INVISIBLE);
    }
  }

  /**
   * Hide gone views
   * @param views Views to hide
   */
  public static void hideGoneViews(View... views) {
    for (View v : views) {
      toggleViewVisible(v, false, View.GONE);
    }
  }

  /**
   * Show views
   * @param views Views to show
   */
  public static void showViews(View... views) {
    for (View v : views) {
      toggleViewVisible(v, true, View.INVISIBLE);
    }
  }

  /**
   *Animate views 
   * @param anim Animation
   * @param views Views to animate
   */
  public static void animateViews(Animation anim, View... views) {
    for (View v : views) {
      v.startAnimation(anim);
    }
  }
  
  /**
   * Disable views
   * @param views Views to disable
   */
  public static void disableViews(View... views){
    for (View v : views) {
      v.setEnabled(false);
    }
  }
  
  /**
   * Enable views
   * @param views Views to enable
   */
  public static void enableViews(View... views){
    for (View v : views) {
      v.setEnabled(true);
    }
  }
  /**
   * Uncheck RadioButtons views
   * @param views Views to uncheck
   */
  public static void uncheckButtons(Button... views){
    for (Button radioButton : views) {
      radioButton.setPressed(false);
    }
  }

  /**
   * Toggle the visibility
   * @param v View
   * @param isVisible true if is visible
   * @param visibility Type of visibility
   */
  private static void toggleViewVisible(View v, boolean isVisible, int visibility) {
    v.setVisibility(isVisible ? View.VISIBLE : visibility);
  }

  /**
   * Convert stream to string
   * @param is InputStream to convert
   * @return InputStream converted
   */
  public static String convertStreamToString(InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  /**
   * Check the network state and show a dialog
   * @param context Application context
   * @return 
   */
  public static boolean checkNetworkAndShowDialogIfOffline(Activity context) {
    boolean online = Network.isOnline(context);
    if (online == false) {
      //TODO SHOW MESSAGE NETWORK
    }
    return online;
  }
  
  /**
   * Hide the softkeyboard
   * @param v View with the current focus
   */
  public static void hideSoftKeyboard(View v) {
    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
  }
  
  /**
   * Show the softkeyboard
   * @param v View with the current focus
   */
  public static void showSoftKeyboard(View v) {
    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
    imm.showSoftInput(v, 0);
  }

  /**
   * Get the real path from a URI
   * @param context Application context
   * @param contentUri Uri of the file
   * @return
   */
  public static String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
      String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(dataIndex);
    } finally {
      try {
        cursor.close();
      } catch (Exception e) {
      }
    }
  }
  
  
  /**
   * Get the file from Picasa app
   * @param context Application context
   * @param uri Uri of the file
   * @return File from picasa
   */
  public static File getFileFromPicasa(Context context, Uri uri){
    File tempFile = null;
    FileOutputStream fout = null;
    InputStream in = null;
    try { 
      tempFile = File.createTempFile("tempFile", ".tmp");
      tempFile.deleteOnExit();  
      ContentResolver res = context.getContentResolver();
      in =  res.openInputStream(uri);
      fout = new FileOutputStream(tempFile);
      int c;  
      while ((c = in.read()) != -1) {  
        fout.write(c);  
      }  
    } catch (IOException e) {
      e.printStackTrace();
    }finally {  
      try {
        if (in != null) {  
          in.close();  
        }  
        if (fout != null) {  
          fout.close();  
        }  
      } catch (Exception e2) {}
    }  
    return tempFile;
  }

  public final static boolean isValidEmail(CharSequence target) {
    return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
  }

  public static void hideKeyBoard(Activity activity, View view){
    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void showKeyBoard(Activity activity, View view){
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
  }

  /**
   * check if an EditText is empty, and returns a boolean
   * @param editTexts
   * @return
   */
  public static boolean isEmptyMultiEditText(EditText... editTexts){
    for (EditText editText : editTexts) {
      if(editText.getText().toString().trim().length() == 0){
        return true;
      }
    }
    return false;
  }

  /**
   * check if an TextView is empty, and returns a boolean
   * @param textViews
   * @return
   */
  public static boolean isEmptyMultiTextView(TextView... textViews){
    for (TextView textView : textViews) {
      if(textView.getText().toString().trim().isEmpty()){
        return true;
      }
    }
    return false;
  }

  /**
   * Concatenates a zero to the left of the string
   * @param num
   * @return
   */
  public static String zeroToLeft(int num){
    String info = (num < 10) ? "0"+num: ""+num;
    return info;
  }
}
