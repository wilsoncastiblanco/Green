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
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.Cruise;

import java.io.UnsupportedEncodingException;

/**
 * Created by rokk3r26 on 1/19/15.
 */
public class PackageContentRequest extends GsonRequest<Cruise> {
  Cruise mCruise;

  public PackageContentRequest(int method, String url, String requestBody, Response.Listener<Cruise> listener,
                               Response.ErrorListener errorListener, Cruise cruise) {
    super(method, url, requestBody, listener, errorListener);
    mCruise = cruise;
  }

  @Override
  protected Response<Cruise> parseNetworkResponse(NetworkResponse networkResponse) {
    try {
      String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
      JsonParser jsonParser = new JsonParser();
      JsonObject jsonObject = (JsonObject)jsonParser.parse(jsonString);
      JsonElement data = jsonObject.get(RestParams.PACKAGE_CONTENT);
      Cruise response = new GsonBuilder().create().fromJson(data, new TypeToken<Cruise>(){}.getType());
      response.setPackageId(mCruise.getPackageId());
      response.setSailDate(mCruise.getSailDate());
      return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JsonSyntaxException e) {
      return Response.error(new ParseError(e));
    }
  }
}
