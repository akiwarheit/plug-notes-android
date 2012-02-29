package com.plug.session;

import java.io.IOException;

import keendy.projects.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
	
	/**
	 * @author kevin
	 */
	public class LoginTask extends AsyncTask<Void, Integer, User> {
		
		private ProgressDialog dialog;
		private SessionCallback callback;
		private User user;
		private String email;
		private String password;
		private Context context;

		public LoginTask(Context context, String email, String password, SessionCallback callback) {
			this.callback = callback;
			this.context = context;
			this.dialog = new ProgressDialog(context);
			this.email = email;
			this.password = password;
		}
		
		@Override
		protected void onPreExecute() {
			this.dialog.setIndeterminate(true);
			this.dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.spinner_loading));
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
			if(this.dialog.isShowing())
				this.dialog.dismiss();
			
			if(finish != null) {
  			this.user = finish;
      	User.setUser(context, this.user);
      	LoginActivity.this.toHomepage();
			} else {
				Toast.makeText(context, "Invalid username or pass, bro.", Toast.LENGTH_LONG).show();
			}
    	
//			if(this.user != null) {
//  			LoginActivity.this.toHomepage();
//			} else
				
//				Log.e("CANT LOGIN FOR SOME REASON BRO", "and i dont know why");
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
  		this.dialog.setProgress(progress[0]);
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
		
	}
}
