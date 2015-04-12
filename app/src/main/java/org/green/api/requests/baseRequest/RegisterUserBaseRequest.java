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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.RegisterProfileRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;

import java.util.Map;

/**
 * Created by juanangelardila on 3/8/15.
 */
public class RegisterUserBaseRequest {

  private RegisterUserBaseRequestListener listener;
  private Context mContext;
  public static RegisterUserBaseRequest mInstance;


  // region Public methods
  public static synchronized RegisterUserBaseRequest getInstance(){
    return mInstance == null ? new RegisterUserBaseRequest() : mInstance;
  }

  public RegisterUserBaseRequest(){
    mInstance = this;
  }

  public void callRegister(Context context, Profile profile) {
    mContext = context;
    String tag_json_obj = "json_obj_register";
    String url = RestConstants.CREATE_USER_PROFILE;
    String params = CelebrityAuthenticationProvider.getInstance().criteriaRegisterUserRequest(profile);
    Request request  = new RegisterProfileRequest(Request.Method.POST, url , params, onSuccessLogin(), onErrorLogin()){

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
      public void onResponse(Profile response) {
        if(listener != null){
          Gson gson = new GsonBuilder().create();
          listener.onSuccess(response);
        }
      }
    };
  }

  private Response.ErrorListener onErrorLogin() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("RegisterUserBaseRequest", "Register error ", error);
        error.printStackTrace();
        if(listener != null){
          listener.onError(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  //endregion

  // region Callbacks

  public interface RegisterUserBaseRequestListener {

    public void onSuccess(Profile response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(RegisterUserBaseRequestListener listener) {
    this.listener = listener;
  }

  //endregion


}
