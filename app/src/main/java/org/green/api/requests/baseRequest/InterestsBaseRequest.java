package org.green.api.requests.baseRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.pcpProducts.ProductsRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.products.CharacteristicsDetail;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.products.Interests;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rokk3r26 on 3/26/15.
 */
public class InterestsBaseRequest {


  private static InterestsBaseRequest mInstance;
  private ArrayList<CharacteristicsDetail> characteristicsList;
  private Context mContext;
  private InterestsEventListener listener;
  private String urlCharacteristics;
  private boolean completeCharacteristics = false;
  private boolean completeInterests = false;
  private ArrayList<Interests> interestsList;
  private String urlInterests;

  public interface InterestsEventListener{
    public void OnLoadInterests(String interestJson, String characteristicsJson);
    public void OnErrorInterests(NetworkError.NetworkErrorType errorType);
  }

  public void setOnInterestsEventListener(InterestsEventListener listener) {
    this.listener = listener;
  }

  public InterestsBaseRequest() {
    mInstance = this;
  }

  public static InterestsBaseRequest getInstance(){
    return (mInstance != null) ? mInstance: new InterestsBaseRequest();
  }

  public void callInterestsData(Context context){
    this.mContext = context;
    callCharacteristicsBaseRequest();
    callInterestsBaseRequest();
  }

  private void callInterestsBaseRequest() {
    String text_json_obj = "json_obj_req_products_characteristics";
    urlInterests = RestConstants.PCP_PRODUCTS_ACTIVITY;
    String params = CelebrityAuthenticationProvider.getInstance()
        .criteriaInterestsRequest(RestConstants.PAYLOAD_STYLE_CONTENT);
    Request request = new ProductsRequest(Request.Method.POST, urlInterests, params, OnSuccessInterests(), OnErrorResponse()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }

  private Response.Listener<JsonObject> OnSuccessInterests() {
    return new Response.Listener<JsonObject>() {
      @Override
      public void onResponse(JsonObject jsonObject) {
        JsonArray data = new JsonArray();
        if(urlInterests.contains("stg5")) {
          data = jsonObject.get(RestParams.DETAILS).getAsJsonObject().getAsJsonArray(RestParams.DETAILS);
        } else {
          if(jsonObject != null){
            data = jsonObject.get(RestParams.CONTENT_DETAIL).getAsJsonObject().getAsJsonArray(RestParams.CONTENT_DETAIL);
          }
        }
        ArrayList<Interests> tempList = new GsonBuilder().create()
            .fromJson(data, new TypeToken<ArrayList<Interests>>() {
            }.getType());
        interestsList = new ArrayList<>();
        for(int i = 0; i < tempList.size(); i++){
          if(tempList.get(i).getName() != null){
            interestsList.add(tempList.get(i));
          }
        }
        completeInterests = true;
        saveResults();
      }
    };
  }


  private void callCharacteristicsBaseRequest() {
    String text_json_obj = "json_obj_req_products_characteristics";
    urlCharacteristics = RestConstants.PCP_PRODUCTS_CHARACTERISTICS;
    String params = CelebrityAuthenticationProvider.getInstance()
        .criteriaCharacteristicsRequest(RestConstants.PAYLOAD_STYLE_CONTENT, "");
    Request request = new ProductsRequest(Request.Method.POST, urlCharacteristics, params, OnSuccessCharacteristics(), OnErrorResponse()){
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
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }

  private Response.Listener<JsonObject> OnSuccessCharacteristics() {
    return new Response.Listener<JsonObject>() {
      @Override
      public void onResponse(JsonObject jsonObject) {
        JsonArray data = new JsonArray();
        if(jsonObject.get(RestParams.DETAILS).getAsJsonObject().getAsJsonArray(RestParams.DETAILS) != null){
          data = jsonObject.get(RestParams.DETAILS).getAsJsonObject().getAsJsonArray(RestParams.DETAILS);
        }
        characteristicsList = new GsonBuilder().create().fromJson(data, new TypeToken<ArrayList<CharacteristicsDetail>>(){}.getType());
        completeCharacteristics = true;
        saveResults();
      }
    };
  }

  private Response.ErrorListener OnErrorResponse() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("InterestsBaseRequest", "ERROR INTERESTS ", error);
        completeCharacteristics = false;
        completeInterests = false;
        if(listener != null){
          listener.OnErrorInterests(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private void saveResults() {
    if(completeCharacteristics && completeInterests){
      if(!interestsList.isEmpty() && !characteristicsList.isEmpty()){

        String characteristicsJson = new GsonBuilder().create().toJson(characteristicsList);
        String interestJson = new GsonBuilder().create().toJson(interestsList);
        if(listener != null) {
          listener.OnLoadInterests(interestJson, characteristicsJson);
        }
      } else {
        if(listener != null) {
          listener.OnErrorInterests(NetworkError.NetworkErrorType.EMPTY);
        }
      }
    }
  }
}
