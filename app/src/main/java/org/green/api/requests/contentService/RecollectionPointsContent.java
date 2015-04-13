package org.green.api.requests.contentService;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.green.api.base.GsonRequest;
import org.green.domain.RecollectionPoints;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


public class RecollectionPointsContent extends GsonRequest<List<RecollectionPoints>> {

  private Map<String, String> params;

  public RecollectionPointsContent(int method, String url, String requestBody,Map<String, String> params, Response.Listener<List<RecollectionPoints>> listener,
                            Response.ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
    this.params = params;
  }

  @Override
  public Map<String, String> getParams() {
    return params;
  }

  @Override
  protected Response<List<RecollectionPoints>> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
      //JsonElement data = jsonObject.get(RestParams.DESTINATION_CONTENT);
      List<RecollectionPoints> response = new GsonBuilder().create().fromJson(jsonObject, new TypeToken<List<RecollectionPoints>>() {}.getType());
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}
