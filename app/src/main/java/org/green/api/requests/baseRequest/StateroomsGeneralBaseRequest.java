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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.catalogService.StateroomSummaryRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.StateroomContentRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.manager.LoopManager;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.staterooms.Staterooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JohanFabiel on 09/03/2015.
 */
public class StateroomsGeneralBaseRequest extends LoopManager {


  private StateroomsGeneralEventListener listener;

  private String tag_json_obj_staterooms = "json_obj_req_staterooms_summary";
  private String tag_json_obj_staterooms_content = "json_obj_req_staterooms_content";
  private Context mContext;
  private NetworkError.NetworkErrorType errorType;
  private List<Staterooms> stateroomsList;
  private int codeListSize;
  private int position = 1;
  private static StateroomsGeneralBaseRequest mInstance;
  private String LOG_TAG = StateroomsGeneralBaseRequest.class.getSimpleName();

  public StateroomsGeneralBaseRequest() {
    mInstance = this;
  }

  public static StateroomsGeneralBaseRequest getInstance(){
    return (mInstance != null) ? mInstance: new StateroomsGeneralBaseRequest();
  }

  public interface StateroomsGeneralEventListener {
    public void onLoadStateroomSuccess(String response);
    public void onStateroomError(NetworkError.NetworkErrorType errorType);
  }

  public void setStateroomBaseRequestListener(StateroomsGeneralEventListener listener) {
    this.listener = listener;
  }

  public void callStateroomsSummary(Context context){
    this.mContext = context;
    resetData();
    String url = CelebrityAuthenticationProvider.getInstance().criteriaHeadersBaseRequest(RestConstants.STATEROOMS_SUMMARY)
        .build().toString();
    Request request = new StateroomSummaryRequest(Request.Method.GET, url, null, OnSuccessSummary(), OnErrorSummary()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj_staterooms, mContext);
  }

  private void resetData() {
    position = 1;
    stateroomsList = new ArrayList<>();
    stateroomsList.clear();
  }

  private Response.ErrorListener OnErrorSummary() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e(LOG_TAG, "Staterooms Summary error ", error);
        listener.onStateroomError(NetworkError.NetworkErrorType.TIMEOUT);
      }
    };
  }

  private Response.Listener<ArrayList<Staterooms>> OnSuccessSummary() {
    return new Response.Listener<ArrayList<Staterooms>>() {
      @Override
      public void onResponse(ArrayList<Staterooms> stateroomsSummary) {
        codeListSize = stateroomsSummary.size();
        for(Staterooms stateroom : stateroomsSummary){
          synchronized (stateroomsSummary){
            callStateroomsContent(stateroom);
          }
        }
      }
    };
  }

  private void callStateroomsContent(Staterooms stateroom){
    String url = CelebrityAuthenticationProvider.getInstance().criteriaHeadersBaseRequest(RestConstants.STATEROOMS_CONTENT)
        .appendQueryParameter(RestParams.STATEROOM_TYPE_CODE, stateroom.getCode())
        .build().toString();
    Request request = new StateroomContentRequest(Request.Method.GET, url, null, OnSuccessContent(), OnErrorContent()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj_staterooms_content, mContext);
  }

  private Response.ErrorListener OnErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e(LOG_TAG, "Staterooms Content error ", error);
        errorType = NetworkError.NetworkErrorType.TIMEOUT;
        updatePosition(position++, codeListSize);
      }
    };
  }

  private Response.Listener<Staterooms> OnSuccessContent() {
    return new Response.Listener<Staterooms>() {
      @Override
      public void onResponse(Staterooms stateroom) {
        if(stateroom != null) {
          stateroomsList.add(stateroom);
        }
        updatePosition(position++, codeListSize);
      }
    };
  }

  @Override
  public void OnEndIteration() {
    if(listener != null){
      if(errorType != null){
        listener.onStateroomError(errorType);
      } else if(stateroomsList != null && !stateroomsList.isEmpty()){
        Gson gson = new GsonBuilder().create();
        listener.onLoadStateroomSuccess(gson.toJson(stateroomsList));
      } else {
        listener.onStateroomError(NetworkError.NetworkErrorType.EMPTY);
      }
    }
    stateroomsList.clear();
  }
}
