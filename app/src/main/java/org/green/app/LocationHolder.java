package org.green.app;

import android.location.Location;

import java.util.Observable;

public class LocationHolder extends Observable {

  private static LocationHolder instance;

  public static void init() {
    if (instance == null) {
      instance = new LocationHolder();
    }
  }

  public static LocationHolder get() {
    return instance;
  }

  Location location;

  public void setLocation(Location location) {
    this.location = location;
    setChanged();
    notifyObservers();
  }

  public Location getLocation() {
    return location;
  }

}
