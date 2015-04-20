package org.green.modules.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.green.R;
import org.green.base.receivers.NetworkChangeReceiver;
import org.green.modules.map.GreenMapFragment;
import org.green.modules.tips.TipsFragment;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;


public class MainActivity extends MaterialNavigationDrawer {

  public static FragmentManager fragmentManager;

  @Override
  public void init(Bundle savedInstanceState) {

    PackageInfo pInfo = null;
    try {
      pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    // set header data
    setDrawerHeaderImage(R.drawable.mat3);
    setUsername(getResources().getString(R.string.app_name));
    setUserEmail(pInfo.versionName);
    fragmentManager = getFragmentManager();
    // create sections
    this.addSection(newSection(getResources().getString(R.string.recollection_points), GreenMapFragment.newInstance()));
    this.addSection(newSection(getResources().getString(R.string.my_places), TipsFragment.newInstance()));
    this.addDivisor();
    this.addSection(newSection(getResources().getString(R.string.batteries), R.drawable.ic_battery_menu, TipsFragment.newInstance()).setSectionColor(getResources().getColor(R.color.batteries_color)));
    this.addSection(newSection(getResources().getString(R.string.tires), R.drawable.ic_tires_menu, TipsFragment.newInstance()).setSectionColor(getResources().getColor(R.color.tires_color)));
    this.addSection(newSection(getResources().getString(R.string.medical), R.drawable.ic_medical_menu, TipsFragment.newInstance()).setSectionColor(getResources().getColor(R.color.medical_color)));
    this.addSection(newSection(getResources().getString(R.string.sprites), R.drawable.ic_spray_menu, TipsFragment.newInstance()).setSectionColor(getResources().getColor(R.color.spray_color)));
    this.addDivisor();
    this.addSection(newSection(getResources().getString(R.string.ecological_tips), TipsFragment.newInstance()));
    this.addSection(newSection(getResources().getString(R.string.ecological_products), TipsFragment.newInstance()));
    // create bottom section
    //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));
    this.disableLearningPattern();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      startActivity(new Intent(this, SettingsActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  @Override
  protected void onResume() {
    super.onResume();
    NetworkChangeReceiver.enableNetworkReceiver(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    NetworkChangeReceiver.disableNetWorkReceiver(this);
  }


}
