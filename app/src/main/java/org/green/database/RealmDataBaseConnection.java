package org.green.database;

import android.content.Context;
import android.util.Log;

import org.green.domain.RecollectionPoints;
import org.json.JSONArray;
import org.json.JSONException;

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
    Realm.deleteRealmFile(mContext);
    //re-instance the database
    realm = Realm.getInstance(mContext);
    mInstance = this;
  }

  public static synchronized RealmDataBaseConnection getInstance(Context context){
    return (mInstance == null) ? new RealmDataBaseConnection(context): mInstance;
  }



  /**
   * Map one string in class of realm
   * @param stringJsonArray
   * @param eClass
   */
  public static void mapRealmObjByJsonArray(String stringJsonArray, Class<? extends io.realm.RealmObject> eClass){
    if(stringJsonArray == null){
      return;
    }
    realm.beginTransaction();
    try {
      JSONArray allObject = new JSONArray(stringJsonArray);
      realm.createOrUpdateAllFromJson(eClass, allObject);
    } catch (JSONException e) {
      e.printStackTrace();
      Log.e(LOG_TAG, "json error exception", e);
    }
    realm.commitTransaction();
  }

  public static void mapRealmObjByJsonObject(String stringJsonObject, Class<? extends io.realm.RealmObject> eClass){
    if(stringJsonObject == null){
      return;
    }
    realm.beginTransaction();
    realm.createOrUpdateObjectFromJson(eClass, stringJsonObject);
    realm.commitTransaction();
  }

  /**
   * Load recollection points list
   */
  public static List<RecollectionPoints> loadRecollectionPointsList( ){
    List<RecollectionPoints> result = realm.where(RecollectionPoints.class).findAll();
    realm.commitTransaction();
    return result;
  }
}
