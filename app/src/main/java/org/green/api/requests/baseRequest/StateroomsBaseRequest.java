package org.green.api.requests.baseRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.bookingRequest.StateroomsCategories;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.manager.LoopManager;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.staterooms.Staterooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rokk3r26 on 3/5/15.
 */
public class StateroomsBaseRequest extends LoopManager {

  private static StateroomsBaseRequest instance;
  private Context mContext;
  private ArrayList<Staterooms> stateroomsList = new ArrayList<>();
  private List<String> stateroomsListTypes = new ArrayList<>();
  private int position = 1;
  private NetworkError.NetworkErrorType typeError;
  private String tag_req_obj_staterooms;


  public interface StateroomsBaseRequestEventListener{
    public void OnLoadStaterooms(String resultStaterooms);
    public void OnErrorStaterooms(NetworkError.NetworkErrorType errorType);
  }

  private StateroomsBaseRequestEventListener listener;

  public void setOnStateroomsBaseRequestEventListener(StateroomsBaseRequestEventListener listener) {
    this.listener = listener;
  }

  public static StateroomsBaseRequest getInstance(){
    return (instance == null) ? new StateroomsBaseRequest() : instance;
  }

  public StateroomsBaseRequest() {
    this.instance = this;
  }

  public void callRetriveStaterooms(Context context , String shipCode, String sailDate, String packageId, List<String> stateroomsTypes){
    this.mContext = context;
    resetValues();
    this.stateroomsListTypes = stateroomsTypes;
    int code = 100;
    tag_req_obj_staterooms = "json_obj_req_staterooms";
    for (String stateroomsType : stateroomsTypes) {
      synchronized (stateroomsTypes) {
        Staterooms staterooms = new Staterooms();
        staterooms.setSailDate(sailDate);
        staterooms.setShipCode(shipCode);
        staterooms.setPackageId(packageId);
        staterooms.setCode("" + (code ++));
        String url = CelebrityAuthenticationProvider.getInstance().criteriaStateroomsRequest(shipCode, sailDate, packageId, stateroomsType);
        Request request = new StateroomsCategories(Request.Method.GET, url, null, OnSuccess(), OnError(), staterooms) {
          @Override
          public String getBodyContentType() {
            return RestConstants.CONTENT_TYPE_URLENCODED;
          }

          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
          }
        };
        request.setRetryPolicy(RestConstants.defaultRetryPolicy);
        request.setTag(tag_req_obj_staterooms);
        ApplicationFramework.getInstance().addToRequestQueue(request, tag_req_obj_staterooms, mContext);
      }
    }
  }

  private void resetValues() {
    this.position = 1;
    this.stateroomsListTypes.clear();
  }

  private Response.Listener<Staterooms> OnSuccess() {
    return new Response.Listener<Staterooms>() {
      @Override
      public void onResponse(Staterooms staterooms) {
        if(staterooms != null){
          stateroomsList.add(staterooms);
        }
        updatePosition(position ++, stateroomsListTypes.size());
      }
    };
  }

  private Response.ErrorListener OnError() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("StateroomsBaseRequest", "ERROR!! ", error);
        typeError = NetworkError.catchErrorType(error);
        updatePosition(position ++, stateroomsListTypes.size());
      }
    };
  }

  public void cancelAllRequest(){
    ApplicationFramework.getInstance().cancelPendingRequests(tag_req_obj_staterooms);
  }

  @Override
  public void OnEndIteration() {
    if(listener != null){
      if(typeError != null){
        listener.OnErrorStaterooms(typeError);
      } else if(stateroomsList.size() == stateroomsListTypes.size()) {
        Gson gson = new GsonBuilder().create();
        listener.OnLoadStaterooms(gson.toJson(stateroomsList));
      } else if(stateroomsList.size() == 0){
        listener.OnErrorStaterooms(NetworkError.NetworkErrorType.EMPTY);
      }
    }
    stateroomsList.clear();
  }

}
