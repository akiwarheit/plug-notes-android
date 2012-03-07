package com.plug.database.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Model {
  
  @SerializedName("id")
  private long id;
  @SerializedName("created_at")
  private String railsCreated;
  @SerializedName("updated_at")
  private String railsUpdated;
  @SerializedName("created_android")
  private String androidCreated;
  @SerializedName("updated_android")
  private String androidUpdated;
  
  public Model() {
    Date date = new Date(System.currentTimeMillis());
    this.androidCreated = date.toString();
    this.androidUpdated = date.toString();
  }
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getRailsCreated() {
    return railsCreated;
  }
  
  public void setRailsCreated(String railsCreated) {
    this.railsCreated = railsCreated;
  }
  
  public String getRailsUpdated() {
    return railsUpdated;
  }
  
  public void setRailsUpdated(String railsUpdated) {
    this.railsUpdated = railsUpdated;
  }
  
  public String getAndroidCreated() {
    return androidCreated;
  }
  
  public void setAndroidCreated(String androidCreated) {
    this.androidCreated = androidCreated;
  }
  
  public String getAndroidUpdated() {
    return androidUpdated;
  }
  
  public void setAndroidUpdated(String androidUpdated) {
    this.androidUpdated = androidUpdated;
  }
  
}
