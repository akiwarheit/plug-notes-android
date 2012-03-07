package com.plug.database.provider;

import android.content.Context;

import com.plug.database.Db4oHelper;
import com.plug.database.model.User;

public class UserProvider extends Db4oHelper {
  
  public static UserProvider provider = null;
  
  public UserProvider(Context context) {
    super(context);
  }
  
  public static UserProvider getInstance(Context context) {
    if (provider == null)
      provider = new UserProvider(context);
    
    return provider;
  }
  
  public void store(User user) {
    db().store(user);
  }
  
  public void delete(User user) {
    db().delete(user);
  }
  
}
