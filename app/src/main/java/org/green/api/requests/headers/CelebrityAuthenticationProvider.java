package org.green.api.requests.headers;

import android.net.Uri;

import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestConstants;
import com.rokk3rlabs.celebrity_cruise_framework_android.api.RestParams;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.cruise.CruiseRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.excursions.ExcursionRequest;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.PersonalInfo;
import com.rokk3rlabs.celebrity_cruise_framework_android.domain.profile.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CelebrityAuthenticationProvider {

  public static CelebrityAuthenticationProvider instance;

  public CelebrityAuthenticationProvider(){
    instance = this;
  }

  public static CelebrityAuthenticationProvider getInstance(){
    return instance == null ? new CelebrityAuthenticationProvider() : instance;
  }

  public Map authenticateRequest(Map<String,String> client) {
    client.put("header.application", RestConstants.HEADER_APPLICATION_CELEBRITY);
    client.put("header.language", RestConstants.HEADER_LANGUAGE_EN_US);
    client.put("header.brand", RestConstants.HEADER_BRAND_C);
    return client;
  }

  public Map authenticateRequestCruises(Map<String, String> client, CruiseRequest cruise) {
    client.put("header.application", RestConstants.HEADER_APPLICATION_CELEBRITY);
    client.put("header.language", RestConstants.HEADER_LANGUAGE_EN_US);
    client.put("header.domainId", "1");
    client.put(RestParams.INCLUDE_RESULTS, cruise.getIncludeResults());
    client.put(RestParams.INCLUDE_FACETS, cruise.getIncludeFacets());
    client.put(RestParams.PAGINATION_OFFSET, String.valueOf(cruise.getPaginationOffset()));
    client.put(RestParams.PAGINATION_COUNT, String.valueOf(cruise.getPaginationCount()));
    client.put(RestParams.CRITERIA_OFFICE, cruise.getCriteriaOffice());
    client.put(RestParams.CRITERIA_COUNTRY, cruise.getCriteriaCountry());
    client.put(RestParams.CRITERIA_CURRENCY, cruise.getCriteriaCurrency());
    client.put(RestParams.CRITERIA_CHANNEL, cruise.getCriteriaChannel());
    client.put(RestParams.CRITERIA_BOOKING_TYPE, cruise.getCriteriaBookingType());
    client.put(RestParams.CRITERIA_CRUISE_TYPE, cruise.getCruiseType());
    client.put(RestParams.CRITERIA_BRAND, cruise.getCriteriaBrand());
    if(cruise.getGroupBy() != null){
      client.put(RestParams.GROUP_BY, cruise.getGroupBy());
    }
    if(cruise.getRegionValue() != null){
      client.put(RestParams.CRITERIA_REGION_VALUE, cruise.getRegionValue());
    }
    return client;
  }

  public String criteriaProductSearchRequest(int page, String categories, String sailDate, String shipCode){
    StringBuilder dataJson = getHeaderInfo();
    dataJson.append("  \"criteria\":{\n");
    dataJson.append("    \"categories\":"+categories+",\n");
    dataJson.append("    \"sailing\":{\n");
    dataJson.append("      \"shipCode\":\""+shipCode+"\",\n");
    dataJson.append("      \"sailDate\":\""+sailDate+"\"\n");
    dataJson.append("    }\n");
    dataJson.append("  },\n");
    dataJson.append("  \"resultPreferences\":{\n");
    dataJson.append("    \"includeFacets\":\"true\",\n");
    dataJson.append("    \"includeResults\":\"true\",\n");
    dataJson.append("    \"includeSoldOut\":\"false\",\n");
    dataJson.append("    \"pagination\": {\n");
    dataJson.append("      \"count\": \""+RestConstants.PAGINATION_COUNT+"\",\n");
    dataJson.append("      \"offset\": \""+page+"\"\n");
    dataJson.append("    }");
    dataJson.append("  }\n");
    dataJson.append("}");
    return  dataJson.toString();
  }

  public String criteriaProductsRequest(String pailoadStyle, String identifiers){
    StringBuilder dataJson = getHeaderInfo();
    dataJson.append("    \"identifiers\":{\n");
    dataJson.append("        \"identifiers\": [\n" + identifiers + "]\n");
    dataJson.append("    },\n");
    dataJson.append("    \"resultsPreferences\":{\n");
    dataJson.append("        \"payloadStyle\": \""+ pailoadStyle +"\"\n");
    dataJson.append("    }\n");
    dataJson.append("}\n");
    return dataJson.toString();
  }

  public String criteriaCharacteristicsRequest(String pailoadStyle, String identifiers){
    StringBuilder dataJson = getHeaderInfo();
    dataJson.append("    \"resultsPreferences\":{\n");
    dataJson.append("        \"payloadStyle\": \""+ pailoadStyle +"\"\n");
    dataJson.append("    },\n");
    dataJson.append("    \"identifiers\":{\n");
    dataJson.append("        \"identifiers\": [\n" + identifiers + "]\n");
    dataJson.append("    }\n");
    dataJson.append("}\n");
    return dataJson.toString();
  }

  public String criteriaInterestsRequest(String pailoadStyle){
    StringBuilder dataJson = getHeaderInfo();
    dataJson.append("    \"resultsPreferences\":{\n");
    dataJson.append("        \"payloadStyle\": \""+ pailoadStyle +"\"\n");
    dataJson.append("    }\n");
    dataJson.append("}\n");
    return dataJson.toString();
  }

  public String criteriaRegisterUserRequest(Profile profile){
    PersonalInfo personalInfo = profile.getPersonalInfo();
    StringBuilder dataJson = getHeaderInfoRegister();
    StringBuilder businessP = getBusinessProgramDomainsJson();
    StringBuilder loginInfo = getLoginInfoJson(profile.getUserId(), profile.getPassword());
    StringBuilder options = getOptionsJson();
    dataJson.append(businessP);
    dataJson.append(loginInfo);
    dataJson.append(options);

    dataJson.append("    \"personalInfo\" :{\n");
    dataJson.append("    \"citizenshipCountry\":\"" + personalInfo.getCitizenShipCountry() + "\",\n");
    dataJson.append("    \"email\":\"" + personalInfo.getEmail() + "\",\n");
    dataJson.append("    \"firstName\":\"" + personalInfo.getFirstName() + "\",\n");
    dataJson.append("    \"gender\":\"" + personalInfo.getGender() + "\",\n");
    dataJson.append("    \"isoLanguageCode\":\"" + personalInfo.getIsoLanguageCode() + "\",\n");
    dataJson.append("    \"lastName\":\"" + personalInfo.getLastName() + "\",\n");
    dataJson.append("    \"titleId\":\"" + personalInfo.getTitleId() + "\"\n");
    dataJson.append("}}\n");
    return dataJson.toString();
  }

  public StringBuilder getHeaderInfo(){
    StringBuilder headers = new StringBuilder();
    headers.append("{\n");
    headers.append("    \"header\" :{\n");
    headers.append("    \"application\":\""+RestConstants.HEADER_APPLICATION_PCP+"\",\n");
    headers.append("    \"language\":\""+RestConstants.HEADER_LANGUAGE_EN_US+"\",\n");
    headers.append("    \"brand\":\""+RestConstants.HEADER_BRAND_C+"\"\n");
    headers.append("    },\n");
    return headers;
  }

  public StringBuilder getHeaderInfoRegister(){
    StringBuilder headers = new StringBuilder();
    headers.append("{\n");
    headers.append("    \"header\" :{\n");
    headers.append("      \"application\":\"" + RestConstants.HEADER_APPLICATION_CELEBRITY + "\",\n");
    headers.append("      \"language\":\"" + RestConstants.HEADER_LANGUAGE_EN_US + "\",\n");
    headers.append("      \"brand\":\"" + RestConstants.HEADER_BRAND_C + "\",\n");
    headers.append("      \"domainId\":" + 6 + "\n");
    headers.append("    },\n");
    return headers;
  }

  public StringBuilder getBusinessProgramDomainsJson() {
    StringBuilder builder = new StringBuilder();
    builder.append("    \"businessProgramDomains\" :[{\n");
    builder.append("      \"programCode\":\"" + "CEL-CC-ENROL" + "\",\n");
    builder.append("      \"programDomainId\":" + 8);
    builder.append("    \n}],\n");
    return builder;
  }

  public StringBuilder getLoginInfoJson(String userId, String password) {
    StringBuilder builder = new StringBuilder();
    builder.append("    \"loginInfo\" :{\n");
    builder.append("      \"password\":\"" + password + "\",\n");
    builder.append("      \"userId\":\"" + userId + "\"");
    builder.append("    \n},\n");
    return builder;
  }

  public String getCourtesyHold(String shipCode, String sailDate, String packageId, String deckNumber, String fareCode, String cabinNumber, Profile profile){
    StringBuilder jsonObjects = getHeaderInfoRegister();
    jsonObjects.append("\"sailingInfo\": {\n");
    jsonObjects.append("    \"shipCode\": \""+shipCode+"\",\n");
    jsonObjects.append("    \"sailDate\": \""+sailDate+"\",\n");
    jsonObjects.append("    \"packageCode\": \""+packageId+"\",\n");
    jsonObjects.append("    \"currencyCode\": \"USD\",\n");
    jsonObjects.append("    \"vendorCode\": \"C\"\n");
    jsonObjects.append("  },\n");
    jsonObjects.append("  \"profileId\": \""+profile.getUserId()+"\",\n");
    jsonObjects.append("  \"countryCode\": \"USA\",\n");
    jsonObjects.append("  \"reservations\": [\n");
    jsonObjects.append("    {\n");
    jsonObjects.append("      \"gratuitiesPrepaid\": \"false\",\n");
    jsonObjects.append("      \"cabin\": {\n");
    jsonObjects.append("        \"berthedCategoryCode\": \""+deckNumber+"\",\n");
    jsonObjects.append("        \"fareCode\": \""+fareCode+"\",\n");
    jsonObjects.append("        \"pricedCategoryCode\": \""+deckNumber+"\",\n");
    jsonObjects.append("        \"waitListed\": \"false\",\n");
    jsonObjects.append("        \"accessible\": \"false\",\n");
    jsonObjects.append("        \"number\": \""+cabinNumber+"\"\n");
    jsonObjects.append("      },\n");
    jsonObjects.append("      \"insurance\": {\n");
    jsonObjects.append("        \"code\": \"CRCR\",\n");
    jsonObjects.append("        \"optionIndicator\": \"false\"\n");
    jsonObjects.append("      },\n");
    jsonObjects.append("      \"fareQualifiers\": {\n");
    jsonObjects.append("        \"seniorReqd\": \"false\",\n");
    jsonObjects.append("        \"militaryReqd\": \"false\",\n");
    jsonObjects.append("        \"policeReqd\": \"false\",\n");
    jsonObjects.append("        \"fireFighterReqd\": \"false\"\n");
    jsonObjects.append("      },\n");
    jsonObjects.append("      \"guestDetails\": [\n");
    jsonObjects.append("        {\n");
    jsonObjects.append("          \"guestContact\": {\n");
    jsonObjects.append("            \"guestRefNumber\": \"0\",\n");
    jsonObjects.append("            \"personalInfo\": {\n");
    jsonObjects.append("              \"name\": {\n");
    jsonObjects.append("                \"title\": \"MR\",\n");
    jsonObjects.append("                \"firstName\": \""+profile.getPersonalInfo().getFirstName()+"\",\n");
    jsonObjects.append("                \"lastName\": \""+profile.getPersonalInfo().getLastName()+"\"\n");
    jsonObjects.append("              },\n");
    jsonObjects.append("              \"age\": \"0\",\n");
    jsonObjects.append("              \"birthDate\": \"1987-04-13\",\n");
    jsonObjects.append("              \"gender\": \""+profile.getPersonalInfo().getGender()+"\",\n");
    jsonObjects.append("              \"nationality\": \"USA\"\n");
    jsonObjects.append("            },\n");
    jsonObjects.append("            \"address\": {\n");
    jsonObjects.append("              \"countryCode\": \"USA\",\n");
    jsonObjects.append("              \"countryName\": \"USA\"\n");
    jsonObjects.append("            },\n");
    jsonObjects.append("            \"email\": \""+profile.getPersonalInfo().getEmail()+"\"\n");
    jsonObjects.append("          }\n");
    jsonObjects.append("        }\n");
    jsonObjects.append("      ],\n");
    jsonObjects.append("      \"dining\": [\n");
    jsonObjects.append("        {\n");
    jsonObjects.append("          \"sitting\": \"O\",\n");
    jsonObjects.append("          \"sittingType\": \"OPEN\",\n");
    jsonObjects.append("          \"sittingInstance\": \"OPNCELT\"\n");
    jsonObjects.append("        }\n");
    jsonObjects.append("      ]\n");
    jsonObjects.append("    }\n");
    jsonObjects.append("  ],\n");
    jsonObjects.append("  \"courtesyHold\": \"true\"\n");
    jsonObjects.append("}");
    return jsonObjects.toString();
  }

  public StringBuilder getOptionsJson() {
    StringBuilder builder = new StringBuilder();
    builder.append("    \"optins\":[{\n");
    builder.append("      \"deliveryMethod\":\"" + "EMAIL" + "\",\n");
    builder.append("      \"optinResponseCode\":\"" + "Y" + "\"");
    builder.append("    \n}],\n");
    return builder;
  }

  public Map authenticateHeaders() {
    Map<String,String> client = new HashMap<>();
    client.put("Content-Type", "application/json");
    client.put("Accept", "application/json");
    return client;
  }

  public String criteriaStateroomsRequest(String shipCode, String sailDate, String packageId, String stateroomsType) {
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.STATEROOMS_CATEGORIES)
        .appendQueryParameter("shipCode", shipCode)
        .appendQueryParameter("sailDate", sailDate)
        .appendQueryParameter("packageId", packageId)
        .appendQueryParameter("adultCount", "2")
        .appendQueryParameter("childCount", "0")
        .appendQueryParameter("countryCode", "USA")
        .appendQueryParameter("currency", "USD")
        .appendQueryParameter("fareCode", "BESTRATE")
        .appendQueryParameter("numberOfCabins", "5")
        .appendQueryParameter("includeDeckInfo", "true")
        .appendQueryParameter("accessibleReqd", "false")
        .appendQueryParameter("seniorReqd", "false")
        .appendQueryParameter("militaryReqd", "false")
        .appendQueryParameter("policeReqd", "false")
        .appendQueryParameter("fireFighterReqd", "false")
        .appendQueryParameter("stateroomType", stateroomsType)
        .build();
    return urlBuilder.toString();
  }

  public Uri.Builder criteriaHeadersBaseRequest(String urlBase){
    return Uri.parse(urlBase).buildUpon()
        .appendQueryParameter("header.application", RestConstants.HEADER_APPLICATION_CELEBRITY)
        .appendQueryParameter("header.language", RestConstants.HEADER_LANGUAGE_EN_US)
        .appendQueryParameter("header.brand", RestConstants.HEADER_BRAND_C);
  }

  public String criteriaExcursionSearch(ExcursionRequest excursionRequest){
    StringBuilder dataJson = getHeaderInfo();
    JSONObject jsonCriteria = new JSONObject();
    try {
      JSONArray jsonCategories = new JSONArray("[" + excursionRequest.getCategories() + "]");
      jsonCriteria.put("categories", jsonCategories);
      JSONArray activityTypes = new JSONArray("[" + excursionRequest.getActivityTypes() + "]");
      jsonCriteria.put("activityTypes", activityTypes);
      JSONArray jsonLocations = new JSONArray("[" + excursionRequest.getLocations() + "]");
      jsonCriteria.put("locations", jsonLocations);
      JSONArray jsonDifficultyLevels = new JSONArray("[" + excursionRequest.getDifficultyLevels() + "]");
      jsonCriteria.put("difficultyLevels", jsonDifficultyLevels);

      JSONObject sailing = new JSONObject();
      sailing.put("shipCode", "");
      sailing.put("sailDate", "");
      jsonCriteria.put("sailing", sailing);
      JSONObject jsonDataCriteria = new JSONObject();
      jsonDataCriteria.put("criteria", jsonCriteria);
      dataJson.append(jsonDataCriteria.toString().substring(1, jsonDataCriteria.toString().length() - 1));
      dataJson.append(",");

    } catch (JSONException e) {
      e.printStackTrace();
    }
    dataJson.append("    \"resultPreferences\" :{\n");
    dataJson.append("    \"includeFacets\":\"" + excursionRequest.getIncludeFacets() + "\",\n");
    dataJson.append("    \"includeResults\":\"" + excursionRequest.getIncludeResults() + "\",\n");
    dataJson.append("    \"includeSoldOut\":\"" + excursionRequest.getIncludeSoldouts() + "\",\n");

    dataJson.append("    \"pagination\" :{\n");
    dataJson.append("    \"offset\":\"" + excursionRequest.getPaginationOffset() + "\",\n");
    dataJson.append("    \"count\":\"" + excursionRequest.getPaginationCount() + "\"\n");
    dataJson.append("    }\n");

    dataJson.append("    } }\n");

    return dataJson.toString();
  }

  public String criteriaLoginRequest(String userId, String password) {
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.LOGIN_PROFILE)
        .appendQueryParameter("userId", userId)
        .appendQueryParameter("password", password)
        .build();
    return urlBuilder.toString();
  }

  public String criteriaCaptainsClubRequest(Profile profile){
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.CAPTAINS_CLUB)
        .appendQueryParameter(RestParams.USER_ID, profile.getUserId())
        .appendQueryParameter(RestParams.USER_ACCESS_TOKEN, profile.getUserAccessToken())
        // TODO : Remove hard coded loyalty code when the code is returned
        .appendQueryParameter(RestParams.LOYALTY_NUMBER, "738652965")
        .build();
    return urlBuilder.toString();
  }

  public String criteriaUpdateUserRequest(Profile profile) {
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.UPDATE_USER_PROFILE)
        .appendQueryParameter("contacts.telephoneNumber", profile.getPersonalInfo().getTelephoneNumber())
        .appendQueryParameter("personalInfo.email", profile.getPersonalInfo().getEmail())
        .appendQueryParameter("personalInfo.firstName", profile.getPersonalInfo().getFirstName())
        .appendQueryParameter("personalInfo.isoLanguageCode", profile.getPersonalInfo().getIsoLanguageCode())
        .appendQueryParameter("personalInfo.lastName", profile.getPersonalInfo().getLastName())
        .appendQueryParameter("userAccessToken", profile.getUserAccessToken())
        .appendQueryParameter("userId", profile.getUserId())
        .build();
    return urlBuilder.toString();
  }

  public String criteriaBookingRequest(String userAccessToken, String userId, String countryCode) {
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.MY_CRUISES)
        .appendQueryParameter("bookingTypeCode", "ALL")
        .appendQueryParameter("folderTypeCode", "ALL")
        .appendQueryParameter("retrieveAllBookings", "true")
        .appendQueryParameter("userAccessToken", userAccessToken)
        .appendQueryParameter("userId", userId)
        .appendQueryParameter("countryCode", countryCode)
        .build();
    return urlBuilder.toString();
  }

  public String forgotPasswordUrlBuilder(String email){
    Uri urlBuilder = criteriaHeadersBaseRequest(RestConstants.FORGOT_PASSWORD)
        .appendQueryParameter(RestParams.HEADER_DOMAIN, "6")
        .appendQueryParameter(RestParams.EMAIL, email)
        .build();
    return urlBuilder.toString();
  }
}
