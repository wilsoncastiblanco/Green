package org.green.api.requests.baseRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.green.api.RestConstants;
import org.green.api.RestParams;
import org.green.api.base.NetworkError;
import org.green.api.requests.contentService.RecollectionPointsContent;
import org.green.api.requests.headers.GreenAuthenticationProvider;
import org.green.app.App;
import org.green.domain.RecollectionPoints;
import org.green.domain.RecollectionPointsA;
import org.green.domain.RecollectionPointsParams;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecollectionPointsBaseRequest {
  private static RecollectionPointsBaseRequest mInstance;
  private NetworkError.NetworkErrorType errorType;

  public RecollectionPointsEventListener listener;

  public interface RecollectionPointsEventListener{
    public void onSuccess(String response);
    public void onError(NetworkError.NetworkErrorType errorType);
  }

  public void setRecollectionPointsRequestListener(RecollectionPointsEventListener listener) {
    this.listener = listener;
  }

  public RecollectionPointsBaseRequest(){
    mInstance = this;
  }

  public static synchronized RecollectionPointsBaseRequest getInstance() {
    return mInstance == null ? new RecollectionPointsBaseRequest() : mInstance;
  }


  public void callRecollectionPoints(Context context, final RecollectionPointsParams recollectionPointsParams) {
    String tag_json_points = "recollection_points_tag";
    Context mContext = context;
    final Map<String, String> params = new HashMap<>();
    params.put(RestParams.LATITUDE, String.valueOf(recollectionPointsParams.getLatitude()));
    params.put(RestParams.LONGITUDE, String.valueOf(recollectionPointsParams.getLongitude()));
    params.put(RestParams.DISTANCE, String.valueOf(recollectionPointsParams.getDistance()));
    params.put(RestParams.LIMIT, String.valueOf(recollectionPointsParams.getLimit()));
    params.put(RestParams.UNIT, recollectionPointsParams.getUnit());
    Request request = new RecollectionPointsContent(Request.Method.POST, RestConstants.GET_RECOLLECTION_POINTS, new Gson().toJson(params), OnSuccess(), OnError()){
      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_JSON;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return GreenAuthenticationProvider.getInstance().authenticateHeaders();
      }

      @Override
      public Map<String, String> getParams() throws AuthFailureError{
        params.put(RestParams.LATITUDE, String.valueOf(recollectionPointsParams.getLatitude()));
        params.put(RestParams.LONGITUDE, String.valueOf(recollectionPointsParams.getLongitude()));
        params.put(RestParams.DISTANCE, String.valueOf(recollectionPointsParams.getDistance()));
        params.put(RestParams.LIMIT, String.valueOf(recollectionPointsParams.getLimit()));
        params.put(RestParams.UNIT, recollectionPointsParams.getUnit());
        return params;
      }

      @Override
      public byte[] getBody() {
        return super.getBody();
      }
    };
    request.setRetryPolicy(RestConstants.defaultRetryPolicy);
    App.getInstance().addToRequestQueue(request, tag_json_points, mContext);
  }

  private Response.ErrorListener OnError() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        listener.onError(NetworkError.catchErrorType(error));
      }
    };
  }

  private Response.Listener<List<RecollectionPointsA>> OnSuccess() {
    return new Response.Listener<List<RecollectionPointsA>>() {
      @Override
      public void onResponse(List<RecollectionPointsA> response) {
        if(listener != null){
          listener.onSuccess(new Gson().toJson(response));
        }
      }
    };
  }
}
