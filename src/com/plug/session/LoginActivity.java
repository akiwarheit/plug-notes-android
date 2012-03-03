package com.plug.session;

import keendy.projects.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.plug.database.model.User;
import com.plug.main.HomeActivity;
import com.plug.web.LoginTask;
import com.plug.web.SessionCallback;
import com.plug.web.WebService;

public class LoginActivity extends Activity implements OnClickListener, SessionCallback {

	private EditText inputEmail;
	private EditText inputPassword;
	private Button loginButton;
	private CheckBox dev;
	
	private static String LOGIN_TAG = "***LOGIN SESSION";	
  private String email;
	private String password;
	private LoginTask login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		init();
	
	}
	
	private void loginHandler(){
  	try {	
  		login = new LoginTask(this, email, password, this);
  		login.execute();
  	}catch (Exception e) {
  		
  	}
	}
	
	private boolean acceptableCredentials(){
		boolean result = false;
		
		email = inputEmail.getText().toString();
		password = inputPassword.getText().toString();
		
		if (email.length() == 0){
			Toast.makeText(this, "Fill out all fields.", Toast.LENGTH_SHORT).show();
		} else {
			result = true;
		}
		
		return result;
	}

	private void toHomepage() {
		Log.d(LOGIN_TAG, "***starting HomeActivity");
  	Intent intent = new Intent(this, HomeActivity.class);
  	startActivity(intent);
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.login_buttonLogin:
				if(acceptableCredentials())
  				loginHandler();
				break;
			case R.id.dev_checkbox:
				if(dev.isChecked()) {
					WebService.initLocation(WebService.LOCAL);
				} else {
					WebService.initLocation(WebService.REMOTE);
				}
				
				Log.e("Web Service updated", WebService.RAW_URL);
				
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		init();
	}

	
	private void init() {
		
		inputEmail = (EditText) findViewById(R.id.login_username);
		inputPassword = (EditText) findViewById(R.id.login_password);
		loginButton = (Button) findViewById(R.id.login_buttonLogin);
		dev = (CheckBox) findViewById(R.id.dev_checkbox);
		
		if(getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		} else {
		
  		dev.setChecked(true);
  		loginButton.setOnClickListener(this);
  		dev.setOnClickListener(this);
  		
  		if(User.getLoggedInUser(this) == null) {
    		Log.d(LOGIN_TAG, "***start login activity");
  		} else
  			toHomepage();
		}
	}
	
	@Override
	public void onSuccessfullLogin(User user){ 
		User.setUser(this, user);
		toHomepage();
	}
	
	@Override
	public void onUnsuccessfullLogin() {
		Toast.makeText(this, "Invalid username or pass, bro.", Toast.LENGTH_LONG).show();	
	}
	
  @Override
  public void onBackPressed() {
  	this.finish();
  }
}
