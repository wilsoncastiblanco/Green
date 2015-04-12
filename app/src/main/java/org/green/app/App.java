package org.green.app;

import android.app.Application;

import org.green.database.RealmDataBaseConnection;
import org.green.utilities.preferences.PreferenceUtil;


public class App extends Application {


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

  public static App getInstance(){
    return app;
  }

}
