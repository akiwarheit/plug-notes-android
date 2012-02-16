package com.plug.doodle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class JPGToBMP {
	static FileInputStream in; 
	static BufferedInputStream buf; 
	Bitmap bitmap;

	
	public static Bitmap covert(File path){
		Bitmap bMap=null;
		try{
			in = new FileInputStream(path.toString()); 
			buf = new BufferedInputStream(in,1024); 
			byte[] bMapArray= new byte[buf.available()]; 
			buf.read(bMapArray); 
			bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length); 

			if (in != null) { 
			in.close(); 
			} 
			if (buf != null) { 
			buf.close(); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bMap;
	}
	
}
