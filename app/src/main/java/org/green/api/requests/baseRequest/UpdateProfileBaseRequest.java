package org.green.api.requests.baseRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.profile.UserProfileRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;

import java.util.Map;

/**
 * Created by juanangelardila on 3/8/15.
 */
public class UpdateProfileBaseRequest {

  private UpdateProfileBaseRequestListener listener;
  private Context mContext;
  public static UpdateProfileBaseRequest mInstance;


  // region Public methods
  public static synchronized UpdateProfileBaseRequest getInstance(){
    return mInstance == null ? new UpdateProfileBaseRequest() : mInstance;
  }

  public UpdateProfileBaseRequest(){
    mInstance = this;
  }

  public void callUpdateProfile(Context context, Profile profile) {
    mContext = context;
    String tag_json_obj = "json_obj_update";
    String url = CelebrityAuthenticationProvider.getInstance().criteriaUpdateUserRequest(profile);
    Request request  = new UserProfileRequest(Request.Method.GET, url , null, onSuccessUpdate(), onErrorUpdate()){

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

  private Response.Listener<Profile> onSuccessUpdate() {
    return new Response.Listener<Profile>() {
      @Override
      public void onResponse(Profile update) {
        if(listener != null){
          Gson gson = new GsonBuilder().create();
          listener.onSuccess(gson.toJson(update));
        }
      }
    };
  }

  private Response.ErrorListener onErrorUpdate() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        if(listener != null){
          listener.onError(NetworkError.NetworkErrorType.TIMEOUT);
        }
      }
    };
  }

  //endregion

  // region Callbacks

  public interface UpdateProfileBaseRequestListener {

    public void onSuccess(String profileResponse);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setListener(UpdateProfileBaseRequestListener listener) {
    this.listener = listener;
  }

  //endregion


}
