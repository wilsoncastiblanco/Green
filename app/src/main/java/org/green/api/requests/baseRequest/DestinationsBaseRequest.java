package org.green.api.requests.baseRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.catalogService.DestinationSummaryRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.DestinationContent;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.destination.Destination;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.manager.LoopManager;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokk3r26 on 1/27/15.
 */
public class DestinationsBaseRequest extends LoopManager{

  public int position = 1;
  public int destinationsArraySize;

  public ArrayList<Destination> listDestinations = new ArrayList<Destination>();
  public static DestinationsBaseRequest mInstance;
  private NetworkError.NetworkErrorType errorType;

  public static synchronized DestinationsBaseRequest getInstance(){
    return mInstance == null ? new DestinationsBaseRequest() : mInstance;
  }

  public DestinationsBaseRequest(){
    mInstance = this;
  }

  private DestinationsBaseRequestListener listener;
  private Context mContext;


  public interface DestinationsBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnDestinationsBaseRequestListener(DestinationsBaseRequestListener listener){
    this.listener = listener;
  }

  public void callDestinationsSummary(Context context) throws UnsupportedEncodingException {
    mContext = context;
    String tag_json_obj = "json_obj_req_destinations_summary";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    String url = RestConstants.DESTINATION_SUMMARY + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING),RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new DestinationSummaryRequest(Request.Method.GET, url , null, onSuccessSummary(), onErrorSummary()){

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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj, mContext);
  }

  private Response.Listener<ArrayList<Destination>> onSuccessSummary() {
    return new Response.Listener<ArrayList<Destination>>() {
      @Override
      public void onResponse(ArrayList<Destination> destinations) {
        if(!destinations.isEmpty()){
          destinationsArraySize = destinations.size();
          for(Destination destination : destinations) {
              synchronized (destinations){
                try {
                  callDestinationContent(destination);
                } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
                }
              }
          }
        }
      }
    };
  }

  private Response.ErrorListener onErrorSummary() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private void callDestinationContent(Destination destination) throws UnsupportedEncodingException {
    String tag_json_obj = "json_obj_req_destinations_content";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.DESTINATION_CODE, destination.getCode());
    String url = RestConstants.DESTINATION_CONTENT + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new DestinationContent(Request.Method.GET, url , null, onSuccessContent(), onErrorContent()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj, mContext);
  }

  private Response.Listener<Destination> onSuccessContent() {
    return new Response.Listener<Destination>() {
      @Override
      public void onResponse(Destination destination) {
        if(destination != null){
          listDestinations.add(destination);
        }
        updatePosition(position++, destinationsArraySize);
      }
    };
  }

  private Response.ErrorListener onErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        errorType = NetworkError.catchErrorType(error);
        updatePosition(position++, destinationsArraySize);
      }
    };
  }


  @Override
  public void OnEndIteration() {
    if(listener != null){
      if(errorType != null){
        listener.onError(errorType);
      } else if(listDestinations != null && listDestinations.size() > 0) {
        Gson gson = new GsonBuilder().create();
        listener.onSuccess(gson.toJson(listDestinations));
      } else {
        listener.onError(NetworkError.NetworkErrorType.EMPTY);
      }
      listDestinations.clear();
    }
  }
}