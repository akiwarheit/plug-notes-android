package com.plug.database.model;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.plug.PlugApplication;
import com.plug.database.provider.UserProvider;
import com.plug.web.Response;
import com.plug.web.WebService;

public class User {
  
  @SerializedName("id")
  private long id;
  @SerializedName("email")
  private String email;
  @SerializedName("encrypted_password")
  private String encrypted_pass;
  @SerializedName("authentication_token")
  private String auth_token;
  @SerializedName("created_at")
  private String created_at;
  @SerializedName("current_sign_in_at")
  private String current_sign_in;
  @SerializedName("current_sign_in_IP")
  private String current_sign_in_IP;
  @SerializedName("password_salt")
  private String password_salt;
  @SerializedName("last_sign_in_at")
  private String last_sign_in_at;
  @SerializedName("last_sign_in_ip")
  private String last_sign_in_IP;
  @SerializedName("remember_created_at")
  private String remember_created_at;
  @SerializedName("remember_token")
  private String remember_token;
  @SerializedName("reset_password_token")
  private String reset_password_token;
  @SerializedName("sign_in_count")
  private String sign_in_count;
  @SerializedName("updated_at")
  private String updated_at;
  private String password;
  
  // {
  // "user":{
  // "authentication_token":null,
  // "created_at":"2011-12-24T06:08:27Z",
  // "current_sign_in_at":"2012-01-07T09:52:56Z",
  // "current_sign_in_ip":"192.168.0.103",
  // "email":"akiwarheit@gmail.com",
  // "encrypted_password":"$2a$10$ZWpJmG8mY0VkkrAEd3Iz/e6mY1aF6tmwLBzAgYVsLW0lh2fu08Fpq",
  // "id":1,
  // "last_sign_in_at":"2011-12-29T06:37:44Z",
  // "last_sign_in_ip":"127.0.0.1",
  // "password_salt":"$2a$10$ZWpJmG8mY0VkkrAEd3Iz/e",
  // "remember_created_at":null,
  // "remember_token":null,
  // "reset_password_token":null,
  // "sign_in_count":3,
  // "updated_at":"2012-01-07T09:52:56Z"
  // }
  // }
  
  public User() {
    
  }
  
  public User(String email, String password) {
    super();
    Date date = new Date(System.currentTimeMillis());
    this.email = email;
    this.created_at = date.toString();
    this.current_sign_in = date.toString();
    this.updated_at = date.toString();
    this.last_sign_in_at = date.toString();
  }
  
  public static void store(Context context, User user) {
    PlugApplication plug = (PlugApplication) context.getApplicationContext();
    UserProvider provider = plug.getUserProvider();
    provider.store(user);
    provider.db().commit();
  }
  
  public static User find(Context context, User user) {
    PlugApplication plug = (PlugApplication) context.getApplicationContext();
    UserProvider provider = plug.getUserProvider();
    return (User) provider.db().queryByExample(user).next();
  }
  
  public static void update(Context context, User user, String email,
      String password) {
    PlugApplication plug = (PlugApplication) context.getApplicationContext();
    UserProvider provider = plug.getUserProvider();
    User currentUser;
    currentUser = (User) provider.db().queryByExample(user).next();
    currentUser.setEmail(email);
    currentUser.setPassword(password);
    provider.store(currentUser);
    provider.db().commit();
  }
  
  public static void delete(Context context, User user) {
    PlugApplication plug = (PlugApplication) context.getApplicationContext();
    UserProvider provider = plug.getUserProvider();
    provider.delete(user);
    provider.db().commit();
  }
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getEncrypte_pass() {
    return encrypted_pass;
  }
  
  public void setEncrypte_pass(String encryptePass) {
    this.encrypted_pass = encryptePass;
  }
  
  public String getAuth_token() {
    return auth_token;
  }
  
  public void setAuth_token(String authToken) {
    this.auth_token = authToken;
  }
  
  public String getCreated_at() {
    return created_at;
  }
  
  public void setCreated_at(String createdAt) {
    this.created_at = createdAt;
  }
  
  public String getCurrent_sign_in() {
    return current_sign_in;
  }
  
  public void setCurrent_sign_in(String currentSignIn) {
    this.current_sign_in = currentSignIn;
  }
  
  public String getCurrent_sign_in_IP() {
    return current_sign_in_IP;
  }
  
  public void setCurrent_sign_in_IP(String currentSignInIP) {
    this.current_sign_in_IP = currentSignInIP;
  }
  
  public String getPassword_salt() {
    return password_salt;
  }
  
  public void setPassword_salt(String passwordSalt) {
    this.password_salt = passwordSalt;
  }
  
  public String getLast_sign_in_at() {
    return last_sign_in_at;
  }
  
  public void setLast_sign_in_at(String lastSignInAt) {
    this.last_sign_in_at = lastSignInAt;
  }
  
  public String getLast_sign_in_IP() {
    return last_sign_in_IP;
  }
  
  public void setLast_sign_in_IP(String lastSignInIP) {
    this.last_sign_in_IP = lastSignInIP;
  }
  
  public String getRemember_created_at() {
    return remember_created_at;
  }
  
  public void setRemember_created_at(String rememberCreatedAt) {
    this.remember_created_at = rememberCreatedAt;
  }
  
  public String getRemember_token() {
    return remember_token;
  }
  
  public void setRemember_token(String rememberToken) {
    this.remember_token = rememberToken;
  }
  
  public String getReset_password_token() {
    return reset_password_token;
  }
  
  public void setReset_password_token(String resetPasswordToken) {
    this.reset_password_token = resetPasswordToken;
  }
  
  public String getSign_in_count() {
    return sign_in_count;
  }
  
  public void setSign_in_count(String signInCount) {
    this.sign_in_count = signInCount;
  }
  
  public String getUpdated_at() {
    return updated_at;
  }
  
  public void setUpdated_at(String updatedAt) {
    this.updated_at = updatedAt;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public static void setUser(Context context, User user) {
    PlugApplication application = (PlugApplication) context
        .getApplicationContext();
    UserProvider provider = application.getUserProvider();
    
    User loggedInUser = getLoggedInUser(context);
    
    if (loggedInUser != null) {
      loggedInUser.setEmail(user.getEmail());
      loggedInUser.setId(user.getId());
    } else
      loggedInUser = user;
    
    provider.store(loggedInUser);
    application.setCurrentUser(getLoggedInUser(context));
    provider.db().commit();
  }
  
  public static User getLoggedInUser(Context context) {
    PlugApplication application = (PlugApplication) context
        .getApplicationContext();
    UserProvider provider = application.getUserProvider();
    
    List<User> users = provider.db().query(User.class);
    
    Log.i("Size is: ", "" + users.size());
    
    if (users.size() >= 1)
      return provider.db().query(User.class).next();
    else
      return null;
  }
  
  public static void logout(Context context) {
    PlugApplication application = (PlugApplication) context
        .getApplicationContext();
    UserProvider provider = application.getUserProvider();
    
    provider.db().delete(getLoggedInUser(context));
    
    provider.db().commit();
  }
  
  public static User login(String email, String password)
      throws NullPointerException, ClientProtocolException, IOException {
    
    User user = null;
    
    HttpResponse response;
    Response container = new Response();
    Gson gson = new Gson();
    
    response = WebService.requestLogin(email, password);
    String json = EntityUtils.toString(response.getEntity());
    
    Log.i("Login URL", WebService.LOGIN_URL);
    Log.i("Login response", json);
    
    container = gson.fromJson(json, Response.class);
    
    user = container.user;
    
    return user;
  }
  
}
