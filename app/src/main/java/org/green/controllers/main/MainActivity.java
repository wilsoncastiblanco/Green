package org.green.controllers.main;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import org.green.R;
import org.green.controllers.map.GreenMapFragment;


public class MainActivity extends ActionBarActivity   {

  public static FragmentManager fragmentManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().add(R.id.container, GreenMapFragment.newInstance()).commit();
  }
}
