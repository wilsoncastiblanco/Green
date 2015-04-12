package org.green.api.requests.contentService;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.GsonRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.destination.DestinationsPorts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by wilsoncastiblanco on 3/8/15.
 */
public class PortContentRequest extends GsonRequest<ArrayList<DestinationsPorts>> {

  public PortContentRequest(int method, String url, String requestBody, Response.Listener<ArrayList<DestinationsPorts>> listener,
                            Response.ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
  }

  @Override
  protected Response<ArrayList<DestinationsPorts>> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
      JsonArray data = jsonObject.getAsJsonArray(RestParams.PORT);
      ArrayList<DestinationsPorts> response = new GsonBuilder().create().fromJson(data, new TypeToken<ArrayList<DestinationsPorts>>() {}.getType());
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}