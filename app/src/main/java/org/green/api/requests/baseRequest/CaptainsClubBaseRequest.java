package org.green.api.requests.baseRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.CaptainsClubRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.captainsClub.CaptainsClub;

import java.util.List;
import java.util.Map;

/**
 * Created by wilsoncastiblanco on 3/24/15.
 */
public class CaptainsClubBaseRequest {
  private CaptainsClubBaseRequestListener listener;
  private Context mContext;
  public static CaptainsClubBaseRequest mInstance;


  // region Public methods
  public static synchronized CaptainsClubBaseRequest getInstance(){
    return mInstance == null ? new CaptainsClubBaseRequest() : mInstance;
  }

  public CaptainsClubBaseRequest(){
    mInstance = this;
  }

  public void callCaptainsClub(Context context, Profile profile) {
    mContext = context;
    String tag_json_obj = "json_obj_login";
    String url = CelebrityAuthenticationProvider.getInstance().criteriaCaptainsClubRequest(profile);
    Request request  = new CaptainsClubRequest(Request.Method.GET, url , null, onSuccessLogin(), onErrorLogin()){
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

  private Response.Listener<List<CaptainsClub>> onSuccessLogin() {
    return new Response.Listener<List<CaptainsClub>>() {
      @Override
      public void onResponse(List<CaptainsClub> captainsClubs) {
        if(listener != null){
          Gson gson = new Gson();
          listener.onSuccess(gson.toJson(captainsClubs));
        }
      }
    };
  }

  private Response.ErrorListener onErrorLogin() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("LoginBaseRequest", "Error Login ", error);
        error.printStackTrace();
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  //endregion

  // region Callbacks

  public interface CaptainsClubBaseRequestListener{

    public void onSuccess(String profile);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(CaptainsClubBaseRequestListener listener) {
    this.listener = listener;
  }

  //endregion

}
