/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Chute Corporation U.S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.green.utilities.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Darko.Grozdanovski
 */
public class PreferenceUtil {

  public static final String TAG = PreferenceUtil.class.getSimpleName();

  private final Context context;

  private static final String PREF_FILE_NAME = "preferences";

  private SharedPreferences prefs;

  private PreferenceUtil(Context context) {
    this.context = context;
    prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
  }

  static PreferenceUtil instance;

  public static PreferenceUtil get() {
    return instance;
  }

  public static void init(Context context) {
    if (instance == null) {
      instance = new PreferenceUtil(context.getApplicationContext());
    }
  }

  public void clearPreferences() {
    getAccessToken().delete();
    getUserToken().delete();
    getDeviceId().delete();
    getUsername().delete();
    getUserPassword().delete();
    getUserLocation().delete();
    getUserId().delete();
    getName().delete();
    getPhoto().delete();
    getUriImage().delete();
    getConversationsPreference().delete();
    getUrbanAirshipDeviceToken().delete();
    getCurrentActivity().delete();
    getExcursionsSearchDestinationsPosition().delete();
    getExcursionsSearchInterestsPosition().delete();
    getActivityTitle().delete();
    getMenuPosition().delete();
  }

  public void clearExcursionsPreferences(){
    getExcursionsSearchInterestsPosition().delete();
    getExcursionsSearchDestinationsPosition().delete();
  }

  public StringPreference getAccessToken() {
    return new StringPreference(prefs, "key_access_token", "");
  }

  public StringPreference getUserToken() {
    return new StringPreference(prefs, "key_user_token", "");
  }

  public StringPreference getDeviceId() {
    return new StringPreference(prefs, "key_device_id", "");
  }

  public StringPreference getAuthToken() {
    return new StringPreference(prefs, "key_auth_token", "");
  }

  public StringPreference getUsername() {
    return new StringPreference(prefs, "key_username", "");
  }

  public StringPreference getUserPassword() {
	  return new StringPreference(prefs, "key_userpassword");
  }
  
  public BooleanPreference getUserLocation() {
    return new BooleanPreference(prefs, "key_userlocation");
  }
  
  public StringPreference getUserId(){
	  return new StringPreference(prefs, "key_userid");
  }
  
  public StringPreference getName(){
	  return new StringPreference(prefs, "key_name");
  }
  
  public StringPreference getPhoto(){
	  return new StringPreference(prefs, "key_photo");
  }

  public StringPreference getUriImage() {
    return new StringPreference(prefs, "key_uri");
  }

  public BooleanPreference getFirstStartPreference() {
    return new BooleanPreference(prefs, "key_is_first_start", true);
  }
  
  public StringPreference getConversationsPreference(){
    return new StringPreference(prefs, "key_conversations");
  }
  
  public StringPreference getUrbanAirshipDeviceToken(){
    return new StringPreference(prefs, "key_urbanairshipdevicetoken");
  }
  
  public StringPreference getCurrentActivity(){
    return new StringPreference(prefs, "key_currentactivity");
  }

  public IntPreference getMenuPosition(){
    return new IntPreference(prefs, "key_menu_position");
  }

  public StringPreference getActivityTitle(){
    return new StringPreference(prefs, "key_activity_menu");
  }

  public StringPreference getExcursionsSearchDestinationsPosition(){
    return new StringPreference(prefs, "key_search_excursions_destinations_position");
  }

  public StringPreference getExcursionsSearchInterestsPosition(){
    return new StringPreference(prefs, "key_search_excursions_interests_position");
  }
}
