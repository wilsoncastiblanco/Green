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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.contentService.ItineraryContentRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.Itinerary;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokk3r26 on 2/11/15.
 */
public class ItineraryBaseRequest {

  private Context mContext;
  private static ItineraryBaseRequest instance;

  public ItineraryBaseRequest() {
    instance = this;
  }
  private ItineraryBaseRequestListener listener;
  public interface ItineraryBaseRequestListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setOnItineraryBaseRequestListener(ItineraryBaseRequestListener listener) {
    this.listener = listener;
  }

  public static synchronized ItineraryBaseRequest newInstance(){
    return (instance != null) ? instance: new ItineraryBaseRequest();
  }

  public void callItineraryContent(Context context, String packageId) throws UnsupportedEncodingException {
    mContext = context;
    String tag_json_obj = "json_obj_req_itinerary_content";
    Map<String, String> params = new HashMap<>();
    CelebrityAuthenticationProvider.getInstance().authenticateRequest(params);
    params.put(RestParams.PACKAGE_ID, packageId);
    String url = RestConstants.ITINERARY_CONTENT + new String(ApiUtils.encodeParameters(params, RestConstants.DEFAULT_PARAMS_ENCODING), RestConstants.DEFAULT_PARAMS_ENCODING);
    Request request = new ItineraryContentRequest(Request.Method.GET, url, null, onSuccessContent(), onErrorContent()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj, context);
  }

  private Response.ErrorListener onErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(listener != null) {
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private Response.Listener<Itinerary> onSuccessContent() {
    return new Response.Listener<Itinerary>() {
      @Override
      public void onResponse(Itinerary response) {
        if(listener != null) {
          Gson gson = new GsonBuilder().create();
          listener.onSuccess(gson.toJson(response));
        }
      }
    };
  }

}
