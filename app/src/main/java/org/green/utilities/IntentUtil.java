package org.green.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class IntentUtil {

  public static final Gson mapper = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private final static String PATH_NAME = File.separator + "Foster" + File.separator;
  public static final String KEY_FUNC = "key_origin";
  public static final String KEY_LOCATION = "key_location";

  public static final int REQUEST_CODE_IMAGE_SELECT = 5555;

  public static final String KEY_ID = "key_id";
  public static final String KEY_VALUE = "key_value";
  public static final String KEY_FINISH = "finish";

  public static final String KEY_CATEGORIE = "categories";
  public static final String KEY_GENETAL_CODE = "code";
  public static final String KEY_SAIL_DATE = "sailDate";
  public static final String KEY_SHIP_CODE = "shipCode";
  public static final String KEY_PACKAGE_ID = "packageId";

  public static final String KEY_MESSAGE = "key_message";
  public static final String KEY_IMAGE = "key_image";

  private static HashSet<String> ignoredPhotoPickApps = new HashSet<String>();

  static {
    ignoredPhotoPickApps.add("com.google.android.apps.plus");
  }

  private static String NUM_CALL = "8008528086";
  public static int SELECT_PHOTO = 100;

  public static void startActivity(Context context, Class cls) {
    context.startActivity(new Intent(context, cls));
  }
/*
  public static void startMenuActivity(Activity context, Class cls) {
    Intent intent = new Intent(context, cls);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra(KEY_ID, "Login");
    context.startActivity(intent);
    context.overridePendingTransition(R.anim.fade_in, 0);
  }
   *//*Menu activities*//*
  *//**
   * Secondary activities
   * *//*
  public static void startCruisesDetailActivity(Activity activity, String cruiseCode) {
    Intent cruiseDetail = new Intent(activity, CruisesDetailActivity.class);
    cruiseDetail.putExtra(KEY_ID, cruiseCode);
    activity.startActivity(cruiseDetail);
  }

  public static void startShipActivity(Activity activity, String shipCode){
    Intent shipDetail = new Intent(activity, ShipActivity.class);
    shipDetail.putExtra(KEY_ID, shipCode);
    activity.startActivity(shipDetail);
  }

  public static void startItineraryActivity(Activity activity, String packageId){
    Intent itineraryDetail = new Intent(activity, ItineraryActivity.class);
    itineraryDetail.putExtra(KEY_ID, packageId);
    activity.startActivity(itineraryDetail);
  }

  public static void startDiningDetailActivity(Activity activity, String diningCode) {
    Intent cruiseDetail = new Intent(activity, DiningDetailActivity.class);
    cruiseDetail.putExtra(KEY_ID, diningCode);
    activity.startActivity(cruiseDetail);
  }

  public static void startDiningActivity(Activity activity, String sailDate, String shipCode){
    Intent excursions = new Intent(activity, DiningActivity.class);
    excursions.putExtra(KEY_ID, activity.getString(R.string.title_activity_excursions));
    excursions.putExtra(KEY_CATEGORIE, RestConstants.CATEGORIES_DINING);
    excursions.putExtra(KEY_SAIL_DATE, sailDate);
    excursions.putExtra(KEY_SHIP_CODE, shipCode);
    activity.startActivity(excursions);
  }

  public static void startBeverageActivity(Activity activity, String sailDate, String shipCode){
    Intent excursions = new Intent(activity, BeveragesActivity.class);
    excursions.putExtra(KEY_CATEGORIE, RestConstants.CATEGORIES_BEVERAGE);
    excursions.putExtra(KEY_SAIL_DATE, sailDate);
    excursions.putExtra(KEY_SHIP_CODE, shipCode);
    activity.startActivity(excursions);
  }

  public static void startSpaActivity(Activity activity, String sailDate, String shipCode) {
    Intent excursions = new Intent(activity, SpaActivity.class);
    excursions.putExtra(KEY_CATEGORIE, RestConstants.CATEGORIES_SPA);
    excursions.putExtra(KEY_SAIL_DATE, sailDate);
    excursions.putExtra(KEY_SHIP_CODE, shipCode);
    activity.startActivity(excursions);
  }

  public static void startExcursionsResultsActivity(Activity activity, String sailDate, String shipCode){
    Intent excursions = new Intent(activity, ExcursionsResultsActivity.class);
    excursions.putExtra(KEY_ID, activity.getString(R.string.title_activity_excursions));
    excursions.putExtra(KEY_CATEGORIE, RestConstants.CATEGORIES_EXCURSIONS);
    excursions.putExtra(KEY_SAIL_DATE, sailDate);
    excursions.putExtra(KEY_SHIP_CODE, shipCode);
    activity.startActivity(excursions);
  }*/
}
