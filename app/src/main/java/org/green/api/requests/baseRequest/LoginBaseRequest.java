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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.LoginRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;

import java.util.Map;

/**
 * Created by juanangelardila on 3/8/15.
 */
public class LoginBaseRequest {

  private LoginBaseRequestListener listener;
  private Context mContext;
  public static LoginBaseRequest mInstance;


  // region Public methods
  public static synchronized LoginBaseRequest getInstance(){
    return mInstance == null ? new LoginBaseRequest() : mInstance;
  }

  public LoginBaseRequest(){
    mInstance = this;
  }

  public void callLogin(Context context, Profile profile) {
    mContext = context;
    String tag_json_obj = "json_obj_login";
    String url = CelebrityAuthenticationProvider.getInstance().criteriaLoginRequest(profile.getUserId(), profile.getPassword());

    Request request  = new LoginRequest(Request.Method.GET, url , null, onSuccessLogin(), onErrorLogin()){

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

  private Response.Listener<Profile> onSuccessLogin() {
    return new Response.Listener<Profile>() {
      @Override
      public void onResponse(Profile profile) {
        if(listener != null){
          Gson gson = new GsonBuilder().create();
          Log.i("LoginBaseRequest", "User login " + gson.toJson(profile));
          listener.onSuccess(gson.toJson(profile));
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

  public interface LoginBaseRequestListener{

    public void onSuccess(String profile);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(LoginBaseRequestListener listener) {
    this.listener = listener;
  }

  //endregion


}
