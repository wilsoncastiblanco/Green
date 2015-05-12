package org.green.api.requests.contentService;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.green.api.RestParams;
import org.green.api.base.GsonRequest;
import org.green.domain.RecollectionPoints;
import org.green.domain.RecollectionPointsA;
import org.green.utilities.GsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecollectionPointsContent extends GsonRequest<List<RecollectionPointsA>> {

  public RecollectionPointsContent(int method, String url, String requestBody, Response.Listener<List<RecollectionPointsA>> listener,
                            Response.ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
    Log.i("Green body", requestBody);
  }

  @Override
  protected Response<List<RecollectionPointsA>> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      List<RecollectionPointsA> response = new ArrayList<>();
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
      String status = jsonObject.getAsJsonObject().get(RestParams.STATUS).getAsString();
      if(status.equalsIgnoreCase(RestParams.SUCCESS)){
        JsonArray data = jsonObject.getAsJsonObject().getAsJsonArray(RestParams.DATA);
        response = new GsonBuilder().create().fromJson(data, new TypeToken<List<RecollectionPointsA>>() {
        }.getType());
      }
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}
