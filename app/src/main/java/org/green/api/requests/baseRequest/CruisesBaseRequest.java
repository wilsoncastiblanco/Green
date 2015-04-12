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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.PackageContentRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.search.CruiseSearchRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.Cruise;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.CruiseRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.manager.LoopManager;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokk3r26 on 12/15/14.
 */
public class CruisesBaseRequest extends LoopManager {
  public ArrayList<Cruise> listCruises = new ArrayList<>();

  public Cruise cruise;
  public static CruisesBaseRequest instance;
  public CruiseRequest cruiseRequest;
  private NetworkError.NetworkErrorType errorType;

  private int position = 1;
  private int cruisesArraySize;

  private static final String INCLUDE_RESULTS = "true";
  private static final String INCLUDE_FACETS  = "false";
  private static final String GROUP_BY        = "PACKAGE";

  public String tag_json_obj_package = "json_obj_req_package_search";
  public String tag_json_obj_pkg_content = "json_obj_req_package_content";

  private int searchRecord = 0;

  public CruisesBaseRequestListener listener;

  public interface CruisesBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setCruisesBaseRequestListener(CruisesBaseRequestListener listener) {
    this.listener = listener;
  }

  public CruisesBaseRequest(){
    instance = this;
  }
  private Context mContext;

  public static synchronized CruisesBaseRequest getInstance() {
    return instance == null ? new CruisesBaseRequest() : instance;
  }

  public void callCruisesSearch(Context context, int page) throws UnsupportedEncodingException {
    Map<String, String> params = new HashMap<>();
    callCruises(context, params, page);
    searchRecord = 0;
  }

  public void callCruiseSearchWithCriteria(Context context, int page, Map<String, String> criteria) throws UnsupportedEncodingException {
    callCruises(context, criteria, page);
    searchRecord = 1;
  }

  public void callCruises(Context context, Map<String, String> params, int page) throws UnsupportedEncodingException {
    mContext = context;
    resetData();
    cruiseRequest = new CruiseRequest();
    cruiseRequest.setPaginationOffset(ApiUtils.getOffset(page));
    cruiseRequest.setPaginationCount(RestConstants.PAGINATION_COUNT);
    cruiseRequest.setIncludeFacets(INCLUDE_FACETS);
    cruiseRequest.setIncludeResults(INCLUDE_RESULTS);
    cruiseRequest.setGroupBy(GROUP_BY);
    CelebrityAuthenticationProvider.getInstance().authenticateRequestCruises(params, cruiseRequest);
    String url = RestConstants.SEARCH_CRUISE + new String(ApiUtils.encodeParameters(params,RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request = new CruiseSearchRequest(Request.Method.GET, url, null, OnSuccessSearch(), OnErrorSearch()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj_package, mContext);
  }

  private void resetData() {
    listCruises.clear();
    position = 1;
  }

  private Response.ErrorListener OnErrorSearch() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        listener.onError(NetworkError.catchErrorType(error));
      }
    };
  }

  private Response.Listener<ArrayList<Cruise>> OnSuccessSearch() {
    return new Response.Listener<ArrayList<Cruise>>() {
      @Override
      public void onResponse(ArrayList<Cruise> response) {
        try {
          callPackageContent(response);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }
    };
  }

  private void callPackageContent(ArrayList<Cruise> cruises) throws UnsupportedEncodingException {
    cruisesArraySize = cruises.size();
    for(Cruise cruise : cruises){
      synchronized (cruises){
        callPackageContent(cruise);
      }
    }
  }

  private void callPackageContent(final Cruise cruise) throws UnsupportedEncodingException {
    Map<String, String> params = new HashMap<String, String>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.PACKAGE_ID, cruise.getPackageId());
    String url = RestConstants.PACKAGE_CONTENT + new String(ApiUtils.encodeParameters(params,RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new PackageContentRequest(Request.Method.GET, url , null, onSuccess(), onError(), cruise){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj_pkg_content, mContext);
  }

  private Response.Listener<Cruise> onSuccess() {
    return new Response.Listener<Cruise>() {
      @Override
      public void onResponse(Cruise cruise) {
        if(cruiseRequest != null) {
          if(cruise.getDestinationCode() != null && cruise.getShipCode() != null) {
            cruise.setSearchRecord(searchRecord);
            listCruises.add(cruise);
          }
        }
        updatePosition(position++, cruisesArraySize);
      }
    };
  }

  private Response.ErrorListener onError() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        errorType = NetworkError.catchErrorType(error);
        updatePosition(position++, cruisesArraySize);
        error.printStackTrace();
      }
    };
  }

  @Override
  public void OnEndIteration() {
    if(listener != null) {
      Gson gson = new GsonBuilder().create();
      if(errorType != null){
        Log.e("ERROR", "ERROR LOAD CruisesBaseRequest 1 ");
        listener.onError(errorType);
      } else if(listCruises != null && listCruises.size() > 0) {
        listener.onSuccess(gson.toJson(listCruises));
      } else {
        Log.e("ERROR", "ERROR LOAD CruisesBaseRequest 2");
        listener.onError(NetworkError.NetworkErrorType.EMPTY);
      }
    }
    listCruises.clear();
  }
}