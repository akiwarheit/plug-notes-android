package com.plug.web;

import java.io.IOException;

import keendy.projects.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.plug.database.model.User;

public class LoginTask extends AsyncTask<Void, Integer, User> {
  
  private ProgressDialog dialog;
  private SessionCallback callback;
  private User user;
  private String email;
  private String password;
  private Context context;
  
  public LoginTask(Context context, String email, String password,
      SessionCallback callback) {
    this.callback = callback;
    this.context = context;
    this.dialog = new ProgressDialog(context);
    this.email = email;
    this.password = password;
  }
  
  @Override
  protected void onPreExecute() {
    this.dialog.setIndeterminate(true);
    this.dialog.setIndeterminateDrawable(context.getResources().getDrawable(
        R.anim.spinner_loading));
    this.dialog.setMessage("Loggin' in, bro");
    this.dialog.setProgress(0);
    this.dialog.show();
  }
  
  @Override
  protected User doInBackground(Void... params) {
    try {
      user = User.login(email, password);
    } catch (NullPointerException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    
    return user;
  }
  
  @Override
  protected void onPostExecute(User finish) {
    if (this.dialog.isShowing())
      this.dialog.dismiss();
    
    if (finish != null) {
      // this.user = finish;
      // User.setUser(context, this.user);
      // LoginActivity.this.toHomepage();
      callback.onSuccessfullLogin(finish);
    } else {
      callback.onUnsuccessfullLogin();
    }
    
    // if(this.user != null) {
    // LoginActivity.this.toHomepage();
    // } else
    
    // Log.e("CANT LOGIN FOR SOME REASON BRO", "and i dont know why");
  }
  
  @Override
  protected void onProgressUpdate(Integer... progress) {
    this.dialog.setProgress(progress[0]);
  }
}
