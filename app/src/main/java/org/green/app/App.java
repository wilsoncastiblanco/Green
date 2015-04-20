package org.green.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.green.database.RealmDataBaseConnection;
import org.green.utilities.preferences.PreferenceUtil;


public class App extends Application {

  public static final String TAG = App.class.getSimpleName();

  private RequestQueue mRequestQueue;

  private static App mInstance;
  private Context mContext;
/*  private static GoogleAnalytics mGa;
  private static Tracker mTracker;

  *//*
   * Google Analytics configuration values.
   *//*
  // Placeholder property ID.
  private static final String GA_PROPERTY_ID = "UA-51205494-2";
  // Dispatch period in seconds.
  private static final int GA_DISPATCH_PERIOD = 30;
  // Prevent hits from being sent to reports, i.e. during testing.
  private static final boolean GA_IS_DRY_RUN = false;
  // GA Logger verbosity.
  private static final LogLevel GA_LOG_VERBOSITY = LogLevel.INFO;
  // Key used to store a user's tracking preferences in SharedPreferences.
  private static final String TRACKING_PREF_KEY = "trackingPreference";

  private static AlertTextView alertTextView;*/
  private static App app;

  public static synchronized App getInstance() {
    return (mInstance != null)  ? mInstance : new App();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    app = this;
    RealmDataBaseConnection.getInstance(this);

    PreferenceUtil.init(this);

    LocationHolder.init();

    //ALog.setDebugTag("Rokk3rTag");
  }

/*  *//*
   * Method to handle basic Google Analytics initialization. This call will not
   * block as all Google Analytics work occurs off the main thread.
   *//*
  private void initializeGa() {
    mGa = GoogleAnalytics.getInstance(this);
    mTracker = mGa.getTracker(GA_PROPERTY_ID);

    // Set dispatch period.
    GAServiceManager.getInstance().setLocalDispatchPeriod(GA_DISPATCH_PERIOD);

    // Set dryRun flag.
    mGa.setDryRun(GA_IS_DRY_RUN);

    // Set Logger verbosity.
    mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

    // Set the opt out flag when user updates a tracking preference.
    SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    userPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener () {
      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
          String key) {
        if (key.equals(TRACKING_PREF_KEY)) {
          GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(sharedPreferences.getBoolean(key, false));
        }
      }
    });
  }


  *//*
   * Returns the Google Analytics tracker.
   *//*
  public static Tracker getGaTracker() {
    return mTracker;
  }

  *//*
   * Returns the Google Analytics instance.
   *//*
  public static GoogleAnalytics getGaInstance() {
    return mGa;
  }*/


  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(mContext);
    }

    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag, Context context) {
    // set the default tag if tag is empty
    mContext = context;
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequests(Object tag) {
    if (mRequestQueue != null) {
      mRequestQueue.cancelAll(tag);
    }
  }

}
