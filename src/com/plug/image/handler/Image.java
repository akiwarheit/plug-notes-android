package com.plug.image.handler;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;

public class Image {
	
	@SerializedName("image")	private long id;
	private Bitmap bitmap;
//	private Uri uri;
//	private String path;
	
	public Image(Bitmap bitmap, Uri uri) {
		this.bitmap = bitmap;
//		this.uri = uri;
	}
	
	public Image(Bitmap bitmap) {
		this.bitmap = bitmap;		
	}
	
	public Image(Uri uri) {
//		this.uri = uri;
	}
	
	public long getId() {
		return id;
	}
	
	public static Bitmap rescaleBitmap(Context context,Uri uri, WindowManager win){
		Bitmap bitmap=null;
		  InputStream is;
			try {
				is = context.getContentResolver().openInputStream(
						  uri);
			  BitmapFactory.Options opts = new BitmapFactory.Options();
    		  opts.inJustDecodeBounds = true;
    		  bitmap = BitmapFactory.decodeStream(is, null, opts);
    		  BitmapFactory.Options ops2 = new BitmapFactory.Options();   		  
    		  Display disp = win.getDefaultDisplay();
    		  int width = disp.getWidth();
    		  float w = opts.outWidth;
    		  int scale = Math.round(w / width);
    		  ops2.inSampleSize = scale;
    		  is = context.getContentResolver().openInputStream(uri);
    		  bitmap = BitmapFactory.decodeStream(is, null, ops2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return bitmap;
	}
	
	public Bitmap rescale(Context context,Uri uri, WindowManager win){
		  InputStream is;
			try {
				is = context.getContentResolver().openInputStream(
						  uri);
			  BitmapFactory.Options opts = new BitmapFactory.Options();
    		  opts.inJustDecodeBounds = true;
    		  bitmap = BitmapFactory.decodeStream(is, null, opts);
    		  BitmapFactory.Options ops2 = new BitmapFactory.Options();   		  
    		  Display disp = win.getDefaultDisplay();
    		  int width = disp.getWidth();
    		  float w = opts.outWidth;
    		  int scale = Math.round(w / width);
    		  ops2.inSampleSize = scale;
    		  is = context.getContentResolver().openInputStream(uri);
    		  bitmap = BitmapFactory.decodeStream(is, null, ops2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return bitmap;
	}
}
