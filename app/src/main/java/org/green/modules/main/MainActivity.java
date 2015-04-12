package org.green.modules.main;

import android.app.FragmentManager;
import android.os.Bundle;

import org.green.R;
import org.green.modules.map.GreenMapFragment;
import org.green.modules.tips.TipsFragment;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;


public class MainActivity extends MaterialNavigationDrawer implements MaterialAccountListener {

  public static FragmentManager fragmentManager;

  @Override
  public void init(Bundle bundle) {


    // add accounts
    MaterialAccount account = new MaterialAccount(this.getResources(),"NeoKree","neokree@gmail.com",R.drawable.photo, R.drawable.bamboo);
    this.addAccount(account);


    this.disableLearningPattern();

    // set listener
    this.setAccountListener(this);

    // create sections
    fragmentManager = getFragmentManager();
    this.addSection(newSection("Map", GreenMapFragment.newInstance()));
    this.addSection(newSection("Section 2", TipsFragment.newInstance()));
  }


  @Override
  public void onAccountOpening(MaterialAccount materialAccount) {

  }

  @Override
  public void onChangeAccount(MaterialAccount materialAccount) {

  }
}
