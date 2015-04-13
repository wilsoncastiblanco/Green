package org.green.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wilsoncastiblanco on 4/12/15.
 */
public class RecollectionPoints extends RealmObject {
  @PrimaryKey
  private int idLocation;
  private int idRecolectionType;
  private String description;
  private String address;
  private float latitude;
  private float longitude;
  private String type;
  private String code;
  private float distance;

  public int getIdLocation() {
    return idLocation;
  }

  public void setIdLocation(int idLocation) {
    this.idLocation = idLocation;
  }

  public int getIdRecolectionType() {
    return idRecolectionType;
  }

  public void setIdRecolectionType(int idRecolectionType) {
    this.idRecolectionType = idRecolectionType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public float getLatitude() {
    return latitude;
  }

  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public float getDistance() {
    return distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }
}
