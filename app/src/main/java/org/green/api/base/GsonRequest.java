package org.green.api.base;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;

public abstract class GsonRequest<T> extends JsonRequest<T> {
  public GsonRequest(int method, String url, String requestBody,Listener<T> listener,ErrorListener errorListener) {
    super(method, url, requestBody, listener, errorListener);
  }
}