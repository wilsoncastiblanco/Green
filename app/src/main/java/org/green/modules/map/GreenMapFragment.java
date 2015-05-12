package org.green.modules.map;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.green.R;
import org.green.api.RestConstants;
import org.green.api.RestParams;
import org.green.api.base.NetworkError;
import org.green.database.RealmDataBaseConnection;
import org.green.database.RecollectionPoints.GetRecollectionPoints;
import org.green.domain.RecollectionPoints;
import org.green.domain.RecollectionPointsParams;
import org.green.modules.main.MainActivity;
import org.green.utilities.NetworkErrorUtil;

import java.util.List;


public class GreenMapFragment extends Fragment implements LocationListener {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private static View view;
  private String KILOMETERS = "kilometers";
  private double latitude;
  private double longitude;

  public static GreenMapFragment newInstance(){
    GreenMapFragment greenMapFragment = new GreenMapFragment();
    greenMapFragment.setArguments(new Bundle());
    return greenMapFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_map, container, false);
    setUpMapIfNeeded();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    if (!getFragmentManager().isDestroyed() && mapFragment != null){
      getFragmentManager().beginTransaction().remove(mapFragment).commit();

    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {
    if (mMap == null) {
      mMap = ((MapFragment) MainActivity.fragmentManager.findFragmentById(R.id.map)).getMap();
      if (mMap != null) {
        setUpMap();
      }
    }
  }

  /**
   * This is where we can add markers or lines, add listeners or move the camera. In this case, we
   * just add a marker near Africa.
   * <p/>
   * This should only be called once and when we are sure that {@link #mMap} is not null.
   */
  private void setUpMap() {
    mMap.setMyLocationEnabled(true);
    LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    String provider = locationManager.getBestProvider(criteria, true);
    Location location = locationManager.getLastKnownLocation(provider);
    if(location!=null){
      onLocationChanged(location);
    }
    locationManager.requestLocationUpdates(provider, 400000, RestConstants.DISTANCE, this);
  }

  @Override
  public void onLocationChanged(Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
    LatLng latLng = new LatLng(latitude, longitude);
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
    mMap.animateCamera(cameraUpdate);
    getLocationData();
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {

  }

  @Override
  public void onProviderEnabled(String s) {

  }

  @Override
  public void onProviderDisabled(String s) {

  }

  private void getLocationData(){
    Log.i("Green Loop", "????????????????");
    GetRecollectionPoints getRecollectionPoints = new GetRecollectionPoints(getActivity(), getDataUser());
    getRecollectionPoints.setDestionationsEventListener(new GetRecollectionPoints.DestinationsListEventListener() {
      @Override
      public void onDataListLoad() {
        List<RecollectionPoints> recollectionPointsList = RealmDataBaseConnection.loadRecollectionPointsList();
        Log.i("Green data", recollectionPointsList.get(0).getCode());
      }

      @Override
      public void onDataListError(NetworkError.NetworkErrorType errorType) {
        new NetworkErrorUtil(getActivity(), getFragmentManager(), errorType, new NetworkErrorUtil.NetworkErrorEventListener() {
          @Override
          public void OnRetryNetworkConnection() {
            getLocationData();
          }

          @Override
          public void OnEmptyResponse() {
          }

          @Override
          public void OnCancelConnection() {
          }
        });
      }
    });
  }

  private RecollectionPointsParams getDataUser(){
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String units = preferences.getString(getResources().getString(R.string.pref_units_key), getResources().getString(R.string.pref_units_kilometers));
    RecollectionPointsParams recollectionPointsParams = new RecollectionPointsParams();
    recollectionPointsParams.setUnit(units);
    recollectionPointsParams.setDistance(RestConstants.DISTANCE);
    recollectionPointsParams.setLatitude(latitude);
    recollectionPointsParams.setLongitude(longitude);
    recollectionPointsParams.setLimit(RestConstants.PAGINATION_COUNT);
    return recollectionPointsParams;
  }
}
