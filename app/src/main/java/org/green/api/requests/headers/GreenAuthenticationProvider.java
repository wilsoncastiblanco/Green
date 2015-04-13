package org.green.api.requests.headers;

import java.util.HashMap;
import java.util.Map;

public class GreenAuthenticationProvider {

  public static GreenAuthenticationProvider instance;

  public GreenAuthenticationProvider(){
    instance = this;
  }

  public static GreenAuthenticationProvider getInstance(){
    return instance == null ? new GreenAuthenticationProvider() : instance;
  }

  public Map authenticateHeaders() {
    Map<String,String> client = new HashMap<>();
    client.put("Content-Type", "application/json");
    client.put("Accept", "application/json");
    return client;
  }
}
