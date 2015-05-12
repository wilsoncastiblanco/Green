package org.green.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wilsoncastiblanco on 4/12/15.
 */
public class RecollectionPoints extends RealmObject {
  @PrimaryKey
  private int idLocation;
  private int idRecollectionType;
  private String type;
  private String code;

  public RecollectionPoints() {
  }

  public RecollectionPoints(int idLocation, int idRecollectionType, String description, String address, float latitude, float longitude, String type, String code, float distance) {
    this.idLocation = idLocation;
    this.idRecollectionType = idRecollectionType;
    this.type = type;
    this.code = code;
  }

  public int getIdLocation() {
    return idLocation;
  }

  public void setIdLocation(int idLocation) {
    this.idLocation = idLocation;
  }

  public int getIdRecollectionType() {
    return idRecollectionType;
  }

  public void setIdRecollectionType(int idRecolectionType) {
    this.idRecollectionType = idRecolectionType;
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

}
