package org.green.api.requests.baseRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.base.multipart.NetworkError;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.headers.CelebrityAuthenticationProvider;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.requests.pcpProducts.ProductsRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.app.ApplicationFramework;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.excursions.ExcursionRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.products.CharacteristicsDetail;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.products.Products;
import com.rokk3rlabs.celebrity_cruise_framework_android.utilities.ApiUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rokk3r26 on 2/12/15.
 */
public class ProductsBaseRequest {

  private static ProductsBaseRequest mInstance;
  private Context mContext;
  private boolean completeContent = false;
  private boolean completeDetail = false;
  private boolean completeInfo = false;
  private String categories;
  private String sailDate = "";
  private String shipCode = "";
  private long initTime;
  private static ArrayList<CharacteristicsDetail> characteristicsList;

  private static final String INCLUDE_RESULTS = "true";
  private static final String INCLUDE_FACETS  = "false";
  private static final String INCLUDE_SOLDOUT = "true";

  public ProductsBaseRequest() {
    mInstance = this;
  }
  private ArrayList<Products> productsList = new ArrayList<>();
  private int searchRecord;

  public static ProductsBaseRequest getInstance(){
    return (mInstance == null) ? new ProductsBaseRequest(): mInstance;
  }

  private void resetValues() {
    completeContent = false;
    completeDetail = false;
    completeInfo = false;
    categories = null;
    sailDate = null;
    shipCode = null;
    productsList.clear();
  }

  private ProductsBaseEventListener listener;



  public interface ProductsBaseEventListener {
    public void OnLoadProductsData(String jsonExcursion);
    public void OnErrorProducts(NetworkError.NetworkErrorType errorType);
  }

  public void setOnProductsBaseEventListener(ProductsBaseEventListener listener) {
    this.listener = listener;
  }

  /**
   * get products list of ID
   * @param context
   * @param categories
   * @param sailDate
   * @param shipCode
   */
  public void callProductsIdsBaseRequest(Context context, int page, String categories, String sailDate, String shipCode) {
    initTime = System.currentTimeMillis();
    mContext = context;
    resetValues();
    this.categories = categories;
    this.sailDate = sailDate;
    this.shipCode = shipCode;
    this.searchRecord = 0;
    String text_json_obj = "json_obj_req_products_identifiers";
    String url = RestConstants.PCP_PRODUCTS_SEARCH;
    String params = CelebrityAuthenticationProvider.getInstance()
        .criteriaProductSearchRequest(ApiUtils.getOffset(page), categories, sailDate, shipCode);
    Request request = new ProductsRequest(Request.Method.POST, url, params, OnSuccess(), OnError()){
      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_URLENCODED;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
      }
    };
    request.setRetryPolicy(RestConstants.defaultRetryPolicy);
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }

  public void callSearchExcursionsRequest(Context context, int page, ExcursionRequest excursionRequest) {
    initTime = System.currentTimeMillis();
    mContext = context;
    resetValues();
    this.sailDate = "";
    this.shipCode = "";
    this.searchRecord = 1;

    excursionRequest.setPaginationOffset(ApiUtils.getOffset(page));
    excursionRequest.setPaginationCount(RestConstants.PAGINATION_COUNT);
    excursionRequest.setIncludeFacets(INCLUDE_FACETS);
    excursionRequest.setIncludeResults(INCLUDE_RESULTS);
    excursionRequest.setIncludeSoldouts(INCLUDE_SOLDOUT);

    String text_json_obj = "json_obj_req_products_search";
    String url = RestConstants.PCP_PRODUCTS_SEARCH;
    String params = CelebrityAuthenticationProvider.getInstance().criteriaExcursionSearch(excursionRequest);
    Request request = new ProductsRequest(Request.Method.POST, url, params, OnSuccess(), OnError()){
      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_URLENCODED;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
      }
    };
    request.setRetryPolicy(RestConstants.defaultRetryPolicy);
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }



  private Response.Listener<JsonObject> OnSuccess() {
    return new Response.Listener<JsonObject>() {
      @Override
      public void onResponse(JsonObject jsonObject) {
        JsonArray resultList = jsonObject.getAsJsonArray(RestParams.PRODUCTS_SEARCH);
        ArrayList<Products> excursions = new GsonBuilder().create().fromJson(resultList, new TypeToken<ArrayList<Products>>(){}.getType());
        if(excursions.size() > 0) {
          productsList = excursions;
          callProductsRequest(excursions);
        } else {
          if(listener != null){
            listener.OnErrorProducts(NetworkError.NetworkErrorType.EMPTY);
          }
        }
      }
    };
  }

  private Response.ErrorListener OnError() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("ProductsBaseRequest", "ERROR IDS EXCURSIONS ", error);
        if(listener != null){
          listener.OnErrorProducts(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private void callProductsRequest(ArrayList<Products> excursions){
    String excursionArray = ProductsRequest.getArrayIdentifiers(excursions);
    callProductsContentRequest(excursionArray);
    callProductsDetailRequest(excursionArray);
  }

  /**
   * get list of products content
   * @param ids
   */
  private void callProductsContentRequest(String ids){
    String text_json_obj = "json_obj_req_products_content";
    String url = RestConstants.PCP_PRODUCTS;
    String params = CelebrityAuthenticationProvider.getInstance().criteriaProductsRequest(RestConstants.PAYLOAD_STYLE_CONTENT, ids);
    Request request = new ProductsRequest(Request.Method.POST, url, params, OnSuccessContent(), OnErrorContent()){
      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_URLENCODED;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
      }
    };
    request.setRetryPolicy(RestConstants.defaultRetryPolicy);
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }

  private Response.ErrorListener OnErrorContent() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("ProductsBaseRequest", "ERROR CONTENT ", error);
        if(listener != null){
          listener.OnErrorProducts(NetworkError.NetworkErrorType.TIMEOUT);
        }
      }
    };
  }

  /**
   * get list of products detail
   * @param ids
   */
  private void callProductsDetailRequest(String ids){
    String text_json_obj = "json_obj_req_products_detail";
    String url = RestConstants.PCP_PRODUCTS;
    String params = CelebrityAuthenticationProvider.getInstance().criteriaProductsRequest(RestConstants.PAYLOAD_STYLE_DETAIL, ids);
    Request request = new ProductsRequest(Request.Method.POST, url, params, OnSuccessDetail(), OnErrorDetail()){
      @Override
      public String getBodyContentType() {
        return RestConstants.CONTENT_TYPE_URLENCODED;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return CelebrityAuthenticationProvider.getInstance().authenticateHeaders();
      }
    };
    request.setRetryPolicy(RestConstants.defaultRetryPolicy);
    ApplicationFramework.getInstance().addToRequestQueue(request, text_json_obj, mContext);
  }

  private Response.ErrorListener OnErrorDetail() {
    return new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e("ProductsBaseRequest", "ERROR DETAILS ", error);
        if(listener != null){
          listener.OnErrorProducts(NetworkError.catchErrorType(error));
        }
      }
    };
  }

  private Response.Listener<JsonObject> OnSuccessDetail() {
    return new Response.Listener<JsonObject>() {
      @Override
      public void onResponse(JsonObject jsonObject) {
        JsonArray data = jsonObject.get(RestParams.DETAILS).getAsJsonObject().getAsJsonArray(RestParams.DETAILS);
        ArrayList<Products> detailList = new GsonBuilder().create().fromJson(data, new TypeToken<ArrayList<Products>>(){}.getType());
        //merge de the productsList with the products detail
        for(int i = 0; i < productsList.size(); i ++){
          for(int j = 0; j < detailList.size(); j ++){
            if(productsList.get(i).getId().getId().equals(detailList.get(j).getIdentifier().getId())){
              synchronized (productsList) {
                productsList.get(i).setBrowsePrices(detailList.get(j).getBrowsePrices());
                productsList.get(i).setDuration(detailList.get(j).getDuration());
                productsList.get(i).setSailDate(sailDate);
                productsList.get(i).setShipCode(shipCode);
                productsList.get(i).setCategories(categories);
                if(detailList.get(j).getCharacteristics() != null && !detailList.get(j).getCharacteristics().getCharacteristics().isEmpty()) {
                  productsList.get(i).setCharacteristicsName(detailList.get(j).getCharacteristics().getCharacteristics().get(0).getValue().getId());
                }
              }
            }
          }
        }
        completeDetail = true;
        OnCompleteInfo();
      }
    };
  }

  private Response.Listener<JsonObject> OnSuccessContent() {
    return new Response.Listener<JsonObject>() {
      @Override
      public void onResponse(JsonObject jsonObject) {
        JsonArray data = jsonObject.get(RestParams.CONTENT_DETAIL).getAsJsonObject().getAsJsonArray(RestParams.CONTENT_DETAIL);
        ArrayList<Products> contentList = new GsonBuilder().create().fromJson(data, new TypeToken<ArrayList<Products>>(){}.getType());
        //merge de the productsList with the products Content
        for(int i = 0; i < productsList.size(); i ++){
          for(int j = 0; j < contentList.size(); j ++){
            if(productsList.get(i).getId().getId().equals(contentList.get(j).getIdentifier().getId())){
              productsList.get(i).setSearchRecord(searchRecord);
              productsList.get(i).setCode(contentList.get(j).getCode());
              productsList.get(i).setName(contentList.get(j).getName());
              productsList.get(i).setImage(contentList.get(j).getImage());
              productsList.get(i).setCarousel(contentList.get(j).getCarousel());
              productsList.get(i).setLongDescription(contentList.get(j).getLongDescription());

              productsList.get(i).setAdditionalInfo(contentList.get(j).getAdditionalInfo());
              productsList.get(i).setHighlights(contentList.get(j).getHighlights());
              productsList.get(i).setImportantNotes(contentList.get(j).getImportantNotes());
              productsList.get(i).setInformation(contentList.get(j).getInformation());

              productsList.get(i).setTitleCopy(contentList.get(j).getTitleCopy());
              productsList.get(i).setShortDescription(contentList.get(j).getShortDescription());
              productsList.get(i).setMenus(contentList.get(j).getMenus());
              if(contentList.get(j).getFeatures() != null) {
                productsList.get(i).setFeatures(contentList.get(j).getFeatures());
              }

              if(contentList.get(j).getHeroImage() != null) {
                productsList.get(i).setHeroImage(contentList.get(j).getHeroImage());
              }

              if(sailDate.equals("") && shipCode.equals("")) {
                productsList.get(i).setDetail(false);
              } else {
                productsList.get(i).setDetail(true);
              }
            }
          }
        }
        completeContent = true;
        OnCompleteInfo();
      }
    };
  }



  /**
   * validate the data is complete
   */
  private void OnCompleteInfo(){
    if(completeDetail && completeContent){
      if(listener != null && !completeInfo){
        if(productsList != null && productsList.size() > 0) {
          Gson gson = new GsonBuilder().create();
          String jsonResult = gson.toJson(getDefinitiveProducts(productsList));
          Log.i("TIME", "TIME!!!! " + ((System.currentTimeMillis() - initTime) / 1000) + "seg.");
          if(listener != null) {
            listener.OnLoadProductsData(jsonResult);
          }
          completeInfo = true;
          productsList.clear();
        } else {
          if(listener != null) {
            listener.OnErrorProducts(NetworkError.NetworkErrorType.EMPTY);
          }
        }
      }
    }
  }

  private ArrayList<Products> getDefinitiveProducts(ArrayList<Products> productsList){
    ArrayList<Products> definitiveProducts = new ArrayList<>();
    for (Products product : productsList){
      if(product.getCode() != null){
        definitiveProducts.add(product);
      }
    }
    return definitiveProducts;
  }

}
