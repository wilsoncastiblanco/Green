package org.green.api.base;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by rokk3r26 on 3/4/15.
 */
public class NetworkError {

  /**
   * network error type
   */
  public enum NetworkErrorType {
    TIMEOUT, EMPTY, PARSE, SERVER, NOCONNECTION, AUTH, UNKNOWN
  }

  /**
   * Handle your error types accordingly. For Timeout & No connection error, you can show 'retry' button.
   * For AuthFailure, you can re login with user credentials.
   * For ClientError, 400 & 401, Errors happening on client side when sending api request.
   * In this case you can check how client is forming the api and debug accordingly.
   * For ServerError 5xx, you can do retry or handle accordingly.
   * @param error
   * @return
   */
  public static NetworkErrorType catchErrorType(VolleyError error){
    if(error.networkResponse != null) {
      int statusCode = error.networkResponse.statusCode;
      NetworkResponse response = error.networkResponse;
      Log.e("NetworkError", "ERROR STATUS CODE: " + statusCode + " " + response.data);
    } else if(error.getCause() != null) {
      Log.e("NetworkError", "ERROR CAUSE: " + error.getCause().getMessage());
    } else {
      Log.e("NetworkError", "ERROR : " + error.getMessage());
    }
    if(error instanceof ServerError) {
      return NetworkErrorType.SERVER;
    } else if( error instanceof AuthFailureError) {
      return NetworkErrorType.AUTH;
    } else if( error instanceof ParseError) {
      return NetworkErrorType.PARSE;
    } else if( error instanceof NoConnectionError) {
      return NetworkErrorType.NOCONNECTION;
    } else if( error instanceof TimeoutError) {
      return NetworkErrorType.TIMEOUT;
    }
    return NetworkErrorType.UNKNOWN;
  }

}
