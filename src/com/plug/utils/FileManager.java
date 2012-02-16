package com.plug.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	
  public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/plug/";
	public static final String TESSERACT_PATH = STORAGE_PATH + "tessdata/";
	
	private Context context;
	private AssetManager manager;
	
	public FileManager(Context context) {
		this.context = context;
		this.manager = context.getAssets();
		
		initPath();
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void writeRawToSD(String output, String input) {
		if (!(new File(output)).exists()) {
    	try {
    		InputStream is = manager.open(input);
    		OutputStream os = new FileOutputStream(output);
    		
				byte[] buf = new byte[1024];
				int len;
				
				while((len = is.read(buf)) > 0) {
					os.write(buf,0,len);
				}
				
				is.close();
				os.close();
				
				Log.i("SUCCESS: ", output);
				
    	} catch (IOException e) {
				Log.e("FAILED TO COPY: ", e.getMessage());					
			}
		}
	}
	
	public void initPath() {
		
		String[] paths = new String[] { STORAGE_PATH, TESSERACT_PATH };
				
		for(String path : paths) {
			File dir = new File(path);
			dir.mkdirs();
		}
	}
	
}
