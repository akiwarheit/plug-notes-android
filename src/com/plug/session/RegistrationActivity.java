//package com.plug.session;
//
//import java.util.Date;
//
//import keendy.projects.R;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.plug.database.model.User;
//
//public class RegistrationActivity extends Activity implements OnClickListener{
//	
//	@InjectView(R.id.firstname)          private EditText firstName;
//	@InjectView(R.id.lastname) 			 private EditText lastName;
//	@InjectView(R.id.username) 			 private EditText username;
//	@InjectView(R.id.password) 			 private EditText password;
//	@InjectView(R.id.pass_confirm) 	     private EditText confirmation;
//	@InjectView(R.id.email_address)      private EditText email_add;
//	@InjectView(R.id.register)  		 private Button register;
//	
//	String firstname;
//	String lastname;
//	String userName;
//	String pass;
//	String confirm;
//	String email;
//	Date date_registered;
//	User user=null;
//	Context context;
//	
//	
//	public void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.registration);	
//		context = this.getApplicationContext();
//		register.setOnClickListener(this);
//	}
//
//
//	@Override
//	public void onClick(View view) {
////		user = getTexts();
////		User.register(context, user);
//	}
//	
//	public User getTexts(){
////		firstname = firstName.getText().toString();
////		lastname = lastName.getText().toString();
////		userName = username.getText().toString();
////		pass = password.getText().toString();
////		email = email_add.getText().toString();
////		date_registered = new Date(System.currentTimeMillis());
////		user = new User(userName, pass,email,firstname,lastname);	
//		return user;
//	}
//
//}
