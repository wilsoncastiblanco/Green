package org.green.api.requests.baseRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.ForgotPasswordRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.general.GeneralResponse;

import java.util.Map;

/**
 * Created by wilsoncastiblanco on 3/24/15.
 */
public class ForgotPasswordBaseRequest {

  private ForgotPasswordBaseRequestListener listener;
  private Context mContext;
  public static ForgotPasswordBaseRequest mInstance;


  // region Public methods
  public static synchronized ForgotPasswordBaseRequest getInstance(){
    return mInstance == null ? new ForgotPasswordBaseRequest() : mInstance;
  }

  public ForgotPasswordBaseRequest(){
    mInstance = this;
  }

  public void callForgotPassword(Context context, String email) {
    mContext = context;
    String tag_json_obj = "json_obj_forgot_password";
    String url = CelebrityAuthenticationProvider.getInstance().forgotPasswordUrlBuilder(email);
    Request request  = new ForgotPasswordRequest(Request.Method.GET, url , null, onSuccessForgot(), onErrorForgot()){

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

  private Response.Listener<GeneralResponse> onSuccessForgot() {
    return new Response.Listener<GeneralResponse>() {
      @Override
      public void onResponse(GeneralResponse generalResponse) {
        if(listener != null){
          listener.onSuccess(generalResponse);
        }
      }
    };
  }

  private Response.ErrorListener onErrorForgot() {
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

  public interface ForgotPasswordBaseRequestListener{

    public void onSuccess(GeneralResponse generalResponse);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(ForgotPasswordBaseRequestListener listener) {
    this.listener = listener;
  }

  //endregion

}
