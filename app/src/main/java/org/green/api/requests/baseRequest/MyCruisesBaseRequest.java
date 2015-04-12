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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.MyCruisesRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.MyCruises;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rokk3r26 on 3/20/15.
 */
public class MyCruisesBaseRequest {

  private MyCruisesBaseRequestListener listener;
  private Context mContext;
  public static MyCruisesBaseRequest mInstance;


  // region Public methods
  public static synchronized MyCruisesBaseRequest getInstance(){
    return mInstance == null ? new MyCruisesBaseRequest() : mInstance;
  }

  public MyCruisesBaseRequest(){
    mInstance = this;
  }

  public void callMyCruiseRequest(Context context, String userAccessToken, String userId, String countryCode) {
    mContext = context;
    String tag_json_obj = "json_obj_login";
    String url = CelebrityAuthenticationProvider.getInstance().criteriaBookingRequest(userAccessToken, userId, countryCode);

    Request request  = new MyCruisesRequest(Request.Method.GET, url , null, onSuccessBookingList(), onErrorBooking()){

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

  //endregion

  // region Private methods

  private Response.Listener<ArrayList<MyCruises>> onSuccessBookingList() {
    return new Response.Listener<ArrayList<MyCruises>>() {
      @Override
      public void onResponse(ArrayList<MyCruises> profile) {
        if(listener != null){
          Gson gson = new GsonBuilder().create();
          Log.i("MyCruisesBaseRequest", "MyCruisesBaseRequest " + gson.toJson(profile));
          listener.onSuccess(gson.toJson(profile));
        }
      }
    };
  }

  private Response.ErrorListener onErrorBooking() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("MyCruisesBaseRequest", "Error MyCruises ", error);
        error.printStackTrace();
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  //endregion

  // region Callbacks

  public interface MyCruisesBaseRequestListener {
    public void onSuccess(String myCruises);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(MyCruisesBaseRequestListener listener) {
    this.listener = listener;
  }
}
