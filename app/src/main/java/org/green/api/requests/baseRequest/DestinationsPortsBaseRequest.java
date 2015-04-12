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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.catalogService.PortsSummaryRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.PortContentRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.destination.DestinationsPorts;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wilsoncastiblanco on 3/3/15.
 */
public class DestinationsPortsBaseRequest {

  public static DestinationsPortsBaseRequest mInstance;
  private Context mContext;
  private String destinationCode;

  public static synchronized DestinationsPortsBaseRequest getInstance(){
    return mInstance == null ? new DestinationsPortsBaseRequest() : mInstance;
  }

  public DestinationsPortsBaseRequestListener listener;

  public interface DestinationsPortsBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnPortsBaseRequestListener(DestinationsPortsBaseRequestListener listener){
    this.listener = listener;
  }

  public DestinationsPortsBaseRequest(){
    mInstance = this;
  }

  public void callPortsSummary(Context context, String destinationCode) throws UnsupportedEncodingException {
    mContext = context;
    this.destinationCode = destinationCode;
    String tag_json_obj = "json_obj_req_ports_summary";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.DESTINATION_CODE, destinationCode);
    String url = RestConstants.PORTS_SUMMARY + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING),RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new PortsSummaryRequest<DestinationsPorts>(Request.Method.GET, url , null, onSuccessSummary(), onErrorSummary(), DestinationsPorts[].class){

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

  private Response.Listener<ArrayList<DestinationsPorts>> onSuccessSummary() {
    return new Response.Listener<ArrayList<DestinationsPorts>>() {
      @Override
      public void onResponse(ArrayList<DestinationsPorts> ports) {
        if(!ports.isEmpty()){
          try {
            ArrayList<String> codes = new ArrayList<>();
            for(DestinationsPorts destinationsPorts : ports){
              codes.add(destinationsPorts.code);
            }
            callPortsContent(StringUtil.concatenateArrayItemsByCommas(codes));
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
        }else{
          if(listener != null){
            listener.onError(NetworkError.NetworkErrorType.EMPTY);
          }
        }
      }
    };
  }

  private Response.ErrorListener onErrorSummary() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private void callPortsContent(String ports) throws UnsupportedEncodingException {
    String tag_json_obj = "json_obj_req_ports_content";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.PORT_CODE, ports);
    String url = RestConstants.PORTS_CONTENT + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING),RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new PortContentRequest(Request.Method.GET, url , null, onSuccessContent(), onErrorContent()){

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

  private Response.Listener<ArrayList<DestinationsPorts>> onSuccessContent() {
    return new Response.Listener<ArrayList<DestinationsPorts>>() {
      @Override
      public void onResponse(ArrayList<DestinationsPorts> ports) {
          if(listener != null){
            Gson gson = new GsonBuilder().create();
            if(!ports.isEmpty()) {
              ALog.i(gson.toJson(setDestinationCode(ports)));
              listener.onSuccess(gson.toJson(setDestinationCode(ports)));
            } else {
              listener.onError(NetworkError.NetworkErrorType.EMPTY);
            }
            ports.clear();
          }
      }
    };
  }

  private Response.ErrorListener onErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  public ArrayList<DestinationsPorts> setDestinationCode(ArrayList<DestinationsPorts> items){
    for(DestinationsPorts item : items){
      item.setDestinationCode(destinationCode);
    }
    return items;
  }
}