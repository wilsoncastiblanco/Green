package org.green.api.requests.contentService;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.GsonRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.Itinerary;

import java.io.UnsupportedEncodingException;

/**
 * Created by rokk3r26 on 2/11/15.
 */
public class ItineraryContentRequest extends GsonRequest<Itinerary> {

  public ItineraryContentRequest(int method, String url, String requestBody, Response.Listener<Itinerary> listener, Response.ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
  }

  @Override
  protected Response<Itinerary> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
      JsonElement data = jsonObject.get(RestParams.ITINERARY_CONTENT);
      Itinerary response = new GsonBuilder().create().fromJson(data, new TypeToken<Itinerary>() {}.getType());
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}
