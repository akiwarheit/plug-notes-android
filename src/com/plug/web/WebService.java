package com.plug.web;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class WebService {
	
//	public static String RAW_URL = "http://keeboi.no-ip.org/";
	public static String RAW_URL = "http://192.168.0.108/";
	public static String BASE_URL = RAW_URL + "mobile/";
	public static String POST_NOTEBOOK_URL = BASE_URL + "upload_notebook";
	public static String POST_NOTE_URL = BASE_URL + "upload_note";
	public static String LOGIN_URL = BASE_URL + "login";
	
	public static String LOCAL = "http://192.168.0.108/";
	public static String REMOTE = "http://keeboi.no-ip.org/";
	
	public static HttpResponse postRequest(MultipartEntity entity, String url) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
	  
		return response;
	}
	
	public static HttpResponse postRequest(UrlEncodedFormEntity entity, String url) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
	  
		return response;
	}
	
	
	public static HttpResponse getRequest(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		
		return response;
	}
	
	public static HttpResponse requestLogin(String email, String password) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(LOGIN_URL+"?email=" + email + "&format=json" + "&password=" + password);
		HttpResponse response = httpClient.execute(httpGet);
		
		return response;
	}
	
	public static void initLocation(String RAW) {
		RAW_URL = RAW;
		BASE_URL = RAW_URL + "mobile/";
  	POST_NOTEBOOK_URL = BASE_URL + "upload_notebook";
  	POST_NOTE_URL = BASE_URL + "upload_note";
  	LOGIN_URL = BASE_URL + "login";	
	}
}
