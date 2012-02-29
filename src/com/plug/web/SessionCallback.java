package com.plug.web;

import com.plug.database.model.User;

public interface SessionCallback {
	
	public abstract void onSuccessfullLogin(User user);
	
	public abstract void onUnsuccessfullLogin();
}
