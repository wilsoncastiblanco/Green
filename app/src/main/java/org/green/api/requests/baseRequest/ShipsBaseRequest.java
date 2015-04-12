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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.catalogService.ShipsSummaryRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.ShipContentRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.manager.LoopManager;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.ship.Ship;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokk3r26 on 1/28/15.
 */
public class ShipsBaseRequest extends LoopManager{

  private int position = 1;
  private int shipsArraySize;

  public ArrayList<Ship> listShips = new ArrayList<Ship>();

  public static ShipsBaseRequest mInstance;
  private Context mContext;
  private NetworkError.NetworkErrorType errorType;

  public static synchronized ShipsBaseRequest getInstance(){
    return mInstance == null ? new ShipsBaseRequest() : mInstance;
  }

  public ShipsBaseRequest(){
    mInstance = this;
  }

  private ShipsBaseRequestListener listener;

  public interface ShipsBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnDestinationsBaseRequestListener(ShipsBaseRequestListener listener){
    this.listener = listener;
  }

  public void callShipsSummary(Context context) throws UnsupportedEncodingException {
    mContext = context;
    String tag_json_obj = "json_obj_req_ships_summary";
    Map<String, String> params = new HashMap<String, String>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    String url = RestConstants.SHIP_SUMMARY + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request = new ShipsSummaryRequest(Request.Method.GET, url, null, onSuccessSummary(), onErrorSummary()){

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

  private Response.Listener<ArrayList<Ship>> onSuccessSummary() {
    return new Response.Listener<ArrayList<Ship>>() {
      @Override
      public void onResponse(ArrayList<Ship> ships) {
        if(!ships.isEmpty()){
          shipsArraySize = ships.size();
          for(Ship ship : ships) {
             synchronized (ships){
               try {
                 callShipContent(ship);
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
        listener.onError(NetworkError.catchErrorType(error));
      }
    };
  }

  private void callShipContent(Ship ship) throws UnsupportedEncodingException {
    String tag_json_obj = "json_obj_req_ship_content";
    Map<String, String> params = new HashMap<String, String>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.SHIP_CODE, ship.getCode());
    String url = RestConstants.SHIP_CONTENT + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING),RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new ShipContentRequest(Request.Method.GET, url , null, onSuccessContent(), onErrorContent()){
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

  private Response.Listener<Ship> onSuccessContent() {
    return new Response.Listener<Ship>() {
      @Override
      public void onResponse(Ship Ship) {
        if(Ship != null){
          listShips.add(Ship);
        }
        updatePosition(position ++, shipsArraySize);
      }
    };
  }

  private Response.ErrorListener onErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        errorType = NetworkError.catchErrorType(error);
        updatePosition(position ++, shipsArraySize);
        error.printStackTrace();
      }
    };
  }

  @Override
  public void OnEndIteration() {
    if(listener != null){
      if(errorType != null){
        listener.onError(errorType);
      } else if(listShips != null && listShips.size() > 0) {
        Gson gson = new GsonBuilder().create();
        listener.onSuccess(gson.toJson(listShips));
      } else {
        listener.onError(NetworkError.NetworkErrorType.EMPTY);
      }
      listShips.clear();
    }
  }
}