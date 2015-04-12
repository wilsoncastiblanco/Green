package org.green.api.requests.baseRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.bookingRequest.CourtesyHoldRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.staterooms.CourtesyHold;

import java.util.Map;

/**
 * Created by rokk3r26 on 3/16/15.
 */
public class CourtesyBaseRequest {


  private static CourtesyBaseRequest mInstance;
  private String tag_json_obj_courtesy = "json_obj_req_courtesy_booking";
  private Context mContext;

  public CourtesyBaseRequest() {
    mInstance = this;
  }

  public static CourtesyBaseRequest getInstance(){
    return (mInstance != null) ? mInstance : new CourtesyBaseRequest();
  }

  public void callCourtesyBooking(Context context, String shipCode, String sailDate, String packageId, String deckNumber, String fareCode, String cabinNumber, Profile profile){
    mContext = context;
    String url = RestConstants.CREATE_BOOKNG;
    String requestBody = CelebrityAuthenticationProvider.getInstance().getCourtesyHold(shipCode, sailDate, packageId, deckNumber, fareCode, cabinNumber, profile);
    Request request = new CourtesyHoldRequest(Request.Method.POST, url, requestBody, onSuccess(), onError()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, tag_json_obj_courtesy, mContext);
  }

  private Response.ErrorListener onError() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(listener != null){
          listener.OnErrorCourtesy(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private Response.Listener<CourtesyHold> onSuccess() {
    return new Response.Listener<CourtesyHold>() {
      @Override
      public void onResponse(CourtesyHold response) {
        String courtese = new GsonBuilder().create().toJson(response);
        if(listener != null){
          if(response.getHeader().getStatus().equals("Failure")) {
            listener.OnSuccessCourtesy(response.getHeader().getError().get(0).getDescription());
          } else if(response.getHeader().getStatus().equals("Success")){
            listener.OnSuccessCourtesy(response.getHeader().getWarning().get(0).getMsg());
          }
        }
      }
    };
  }


  public interface CourtesyBaseRequestEventListener{
    public void OnSuccessCourtesy(String courtesy);
    public void OnErrorCourtesy(NetworkError.NetworkErrorType errorType);
  }

  private CourtesyBaseRequestEventListener listener;

  public void setOnCourtesyBaseRequestEventListener(CourtesyBaseRequestEventListener listener) {
    this.listener = listener;
  }
}
