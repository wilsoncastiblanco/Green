package org.green.database;

import android.content.Context;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rokk3r26 on 2/5/15.
 */
public class RealmDataBaseConnection {
  private static String LOG_TAG = RealmConstants.class.getSimpleName();
  private static Context mContext;
  private static RealmDataBaseConnection mInstance;
  private static Realm realm;

  public interface RealmDataLoadListener{
    public void OnDataListLoadEventListener(List<?> data);
  }

  public RealmDataLoadListener listener;

  public void setOnRealmDataLoadListener(RealmDataLoadListener listener) {
    this.listener = listener;
  }

  public RealmDataBaseConnection(Context context) {
    mContext = context;
    //delete all database
    //Realm.deleteRealmFile(mContext);
    //re-instance the database
    realm = Realm.getInstance(mContext);
    mInstance = this;
  }

  public static synchronized RealmDataBaseConnection getInstance(Context context){
    return (mInstance == null) ? new RealmDataBaseConnection(context): mInstance;
  }
}
