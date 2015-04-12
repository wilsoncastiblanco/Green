package org.green.api.requests.contentService;

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
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.GsonRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.ship.Ship;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.ship.Statistics;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokk3r26 on 1/23/15.
 */
public class ShipContentRequest extends GsonRequest<Ship> {

  public ShipContentRequest(int method, String url, String requestBody, Response.Listener<Ship> listener,
                               Response.ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
  }

  @Override
  protected Response<Ship> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
      JsonElement data = jsonObject.get(RestParams.SHIP_CONTENT);
      JsonArray statistics = data.getAsJsonObject().get(RestParams.SHIP_STATISTICS)
          .getAsJsonObject().get(RestParams.SHIP_ITEM).getAsJsonArray();
      Map<String, String> mapStatistics = new HashMap<>();
      for(JsonElement element : statistics){
        String key = element.getAsJsonObject().get(RestParams.SHIP_TITLE).getAsString().trim();
        String value = element.getAsJsonObject().get(RestParams.SHIP_SHORT_D).getAsString().trim();
        mapStatistics.put(key.replaceAll("\\s+",""), value);
      }
      JSONObject jsonStatistics = new JSONObject(mapStatistics);
      Statistics statisticsJson = new GsonBuilder().create().fromJson( jsonStatistics.toString(), new TypeToken<Statistics>() {}.getType());
      Ship response = new GsonBuilder().create().fromJson(data, new TypeToken<Ship>() {}.getType());
      response.setStatistics(statisticsJson);
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}