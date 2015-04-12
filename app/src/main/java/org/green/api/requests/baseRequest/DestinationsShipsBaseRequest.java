package org.green.api.requests.baseRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.araneaapps.android.libs.logger.ALog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.search.CruiseSearchWithRegionRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.CruiseRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.destination.DestinationShipItem;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.destination.DestinationShipItems;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wilsoncastiblanco on 3/8/15.
 */
public class DestinationsShipsBaseRequest {
  public static DestinationsShipsBaseRequest mInstance;
  private Context mContext;

  private static final int PAGINATION_COUNT   = 1;
  private static final int OFFSET             = 0;
  private static final String INCLUDE_RESULTS = "false";
  private static final String INCLUDE_FACETS  = "true";
  private static final String SHIP_CODE       = "SHPCD";
  private String destinationCode;

  public static synchronized DestinationsShipsBaseRequest getInstance(){
    return mInstance == null ? new DestinationsShipsBaseRequest() : mInstance;
  }

  public DestinationsDetailShipsBaseRequestListener listener;

  public interface DestinationsDetailShipsBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnDestinationsDetailShipsBaseRequestListener(DestinationsDetailShipsBaseRequestListener listener){
    this.listener = listener;
  }

  public DestinationsShipsBaseRequest(){
    mInstance = this;
  }

  public void callDestinationsShips(Context context, String destinationCode) throws UnsupportedEncodingException {
    mContext = context;
    this.destinationCode = destinationCode;
    String tag_json_obj = "json_obj_req_cruise_search";
    Map<String, String> params = new HashMap<String, String>();
    CruiseRequest cruiseRequest = new CruiseRequest();
    cruiseRequest.setPaginationOffset(OFFSET);
    cruiseRequest.setPaginationCount(PAGINATION_COUNT);
    cruiseRequest.setIncludeFacets(INCLUDE_FACETS);
    cruiseRequest.setIncludeResults(INCLUDE_RESULTS);
    cruiseRequest.setRegionValue(destinationCode);
    CelebrityAuthenticationProvider.getInstance().authenticateRequestCruises(params, cruiseRequest);
    String url = RestConstants.SEARCH_CRUISE + new String(ApiUtils.encodeParameters(params,RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request = new CruiseSearchWithRegionRequest(Request.Method.GET, url, null, OnSuccessSearch(), OnErrorSearch()){
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

  private Response.Listener<ArrayList<DestinationShipItems>> OnSuccessSearch() {
    return new Response.Listener<ArrayList<DestinationShipItems>>() {
      @Override
      public void onResponse(ArrayList<DestinationShipItems> response) {
        ArrayList<DestinationShipItems> items = getShipsByDestination(response);
        ArrayList<DestinationShipItem> item = getShipsByCount(items);
        if(listener != null && !item.isEmpty()){
          Gson gson = new GsonBuilder().create();
          ALog.i(gson.toJson(setDestinationCode(item)));
          listener.onSuccess(gson.toJson(setDestinationCode(item)));
        }else{
          if(listener != null){
            listener.onError(NetworkError.NetworkErrorType.EMPTY);
          }
        }
      }
    };
  }

  private Response.ErrorListener OnErrorSearch() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  public ArrayList<DestinationShipItems> getShipsByDestination(ArrayList<DestinationShipItems> data){
    ArrayList<DestinationShipItems> items = new ArrayList<DestinationShipItems>();
    for(DestinationShipItems item : data){
      if(item.getName().trim().equalsIgnoreCase(SHIP_CODE)){
        items.add(item);
      }
    }
    return items;
  }

  public ArrayList<DestinationShipItem> getShipsByCount(ArrayList<DestinationShipItems> data){
    ArrayList<DestinationShipItem> dataItems = new ArrayList<DestinationShipItem>();
    for(DestinationShipItems items : data){
      for(DestinationShipItem item : items.getItem()){
        if(item.getCount() > 0){
          dataItems.add(item);
        }
      }
    }
    return dataItems;
  }

  public ArrayList<DestinationShipItem> setDestinationCode(ArrayList<DestinationShipItem> items){
    for(DestinationShipItem item : items){
      item.setDestinationCode(destinationCode);
    }
    return items;
  }
}
