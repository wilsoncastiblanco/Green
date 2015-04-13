package org.green.database.RecollectionPoints;

import android.content.Context;

import org.green.api.base.NetworkError;
import org.green.api.requests.baseRequest.RecollectionPointsBaseRequest;
import org.green.database.RealmDataBaseConnection;
import org.green.domain.RecollectionPoints;
import org.green.domain.RecollectionPointsParams;


/**
 * Created by wilsoncastiblanco on 4/13/15.
 */
public class GetRecollectionPoints {
  private RecollectionPointsParams recollectionPointsParams;

  public static GetRecollectionPoints mInstance;
  private Context mContext;

  public GetRecollectionPoints(Context context, RecollectionPointsParams recollectionPointsParams){
    this.mContext = context;
    this.recollectionPointsParams = recollectionPointsParams;
  }

  public interface DestinationsListEventListener{
    public void onDataListLoad();
    public void onDataListError(NetworkError.NetworkErrorType errorType);
  }

  private DestinationsListEventListener listener;

  public void setDestionationsEventListener(DestinationsListEventListener listener) {
    this.listener = listener;
    init();
  }

  public void init() {
    RecollectionPointsBaseRequest.getInstance().callRecollectionPoints(mContext, recollectionPointsParams);
    RecollectionPointsBaseRequest.getInstance().setRecollectionPointsRequestListener(new RecollectionPointsBaseRequest.RecollectionPointsEventListener() {
      @Override
      public void onSuccess(String response) {
        RealmDataBaseConnection.mapRealmObjByJsonArray(response, RecollectionPoints.class);
        if(listener != null) {
          listener.onDataListLoad();
        }
      }

      @Override
      public void onError(NetworkError.NetworkErrorType errorType) {
        if(listener != null) {
          listener.onDataListError(errorType);
        }
      }
    });
  }

  public void cancelListener(){
    this.listener = null;
  }

}
