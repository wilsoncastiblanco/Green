package org.green.api;

import com.android.volley.DefaultRetryPolicy;

public class RestConstants {
  /**
   * Headers
   * */
  public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
  public static String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
  public static String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

  /**
   * Pagination params
   */
  public static final int PAGINATION_COUNT   = 5;
  public static final int DISTANCE = 15;

  /**
   * Timeout params
   */
  public static int TIME_OUT = 15000;
  public static int NUM_INTENTS = 2;
  public static final DefaultRetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(TIME_OUT, NUM_INTENTS, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

  /***
   * Base URLs
   * */
  public static String BASE_URL = "http://green.tengounasolucion.com/v1/";
  /**
   * URLs Services
   * */
  public static String GET_RECOLLECTION_POINTS = BASE_URL + "get_recolection_points";
}
