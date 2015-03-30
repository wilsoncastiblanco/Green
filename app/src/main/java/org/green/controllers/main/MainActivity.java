package org.green.controllers.main;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;

import org.green.R;
import org.green.controllers.map.GreenMapFragment;
import org.green.controllers.tips.TipsFragment;

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

    MaterialAccount account2 = new MaterialAccount(this.getResources(),"Hatsune Miky","hatsune.miku@example.com",R.drawable.photo2,R.drawable.mat2);
    this.addAccount(account2);

    MaterialAccount account3 = new MaterialAccount(this.getResources(),"Example","example@example.com",R.drawable.photo,R.drawable.mat3);
    this.addAccount(account3);

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
