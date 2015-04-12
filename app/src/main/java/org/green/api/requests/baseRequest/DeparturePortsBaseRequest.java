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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.ports.DeparturePorts;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wilsoncastiblanco on 3/18/15.
 */
public class DeparturePortsBaseRequest {

  public static DeparturePortsBaseRequest mInstance;

  public static synchronized DeparturePortsBaseRequest getInstance(){
    return mInstance == null ? new DeparturePortsBaseRequest() : mInstance;
  }

  public DeparturePortsBaseRequest(){
    mInstance = this;
  }

  private DeparturePortsBaseRequestListener listener;
  private Context mContext;


  public interface DeparturePortsBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnDeparturePortsBaseRequestListener(DeparturePortsBaseRequestListener listener){
    this.listener = listener;
  }

  public void callDeparturePorts(Context context) throws UnsupportedEncodingException {
    mContext = context;
    String tag_json_obj = "json_obj_req_departure_ports";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.PORT_TYPE, RestConstants.PORT_TYPE_D);
    String url = RestConstants.PORTS_SUMMARY + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING),RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request  = new PortsSummaryRequest<DeparturePorts>(Request.Method.GET, url , null, onSuccessSummary(), onErrorSummary(), DeparturePorts[].class){

      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_URLENCODED;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
      }
    };
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj, mContext);
  }

  private Response.Listener<ArrayList<DeparturePorts>> onSuccessSummary() {
    return new Response.Listener<ArrayList<DeparturePorts>>() {
      @Override
      public void onResponse(ArrayList<DeparturePorts> destinations) {
        if(listener != null){
          Gson gson = new GsonBuilder().create();
          if(!destinations.isEmpty()) {
            ALog.i(gson.toJson(destinations));
            listener.onSuccess(gson.toJson(destinations));
          } else {
            listener.onError(NetworkError.NetworkErrorType.EMPTY);
          }
          destinations.clear();
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

}
