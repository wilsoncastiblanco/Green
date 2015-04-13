package org.green.modules.map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.green.R;
import org.green.api.base.NetworkError;
import org.green.database.RealmDataBaseConnection;
import org.green.database.RecollectionPoints.GetRecollectionPoints;
import org.green.domain.RecollectionPoints;
import org.green.domain.RecollectionPointsParams;
import org.green.modules.main.MainActivity;
import org.green.utilities.NetworkErrorUtil;

import java.util.List;


public class GreenMapFragment extends Fragment {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private static View view;

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
    mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
  }

  private void getLocationData(){
    GetRecollectionPoints getRecollectionPoints = new GetRecollectionPoints(getActivity(), new RecollectionPointsParams());
    getRecollectionPoints.setDestionationsEventListener(new GetRecollectionPoints.DestinationsListEventListener() {
      @Override
      public void onDataListLoad() {
        List<RecollectionPoints> recollectionPointsList = RealmDataBaseConnection.loadRecollectionPointsList();

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
}
