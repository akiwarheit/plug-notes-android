package com.plug.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.plug.doodle.DrawingActivity.PaintView;

public class FileManager {
	
  public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/plug/";
	public static final String TESSERACT_PATH = STORAGE_PATH + "tessdata/";
	public static final String IMAGE_PATH = STORAGE_PATH + "images/";
	
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
	
	public void saveImage(PaintView v) {
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.buildDrawingCache(true);

		Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		String filename = String.valueOf(System.currentTimeMillis());
    		
		if (bm != null) {
	    try {
        File imageFolder = new File(IMAGE_PATH);
        if(!imageFolder.exists())
        	imageFolder.mkdir();
        
        OutputStream fOut = null;
        File file = new File(imageFolder.getAbsolutePath(), filename+".jpg");
        fOut = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
        Log.e("ImagePath", "Image Path : " + MediaStore.Images.Media.insertImage
	        		( context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName()));
	    }
	    catch (Exception e) {
        e.printStackTrace();
	    }
	    
	    Toast.makeText(context, "Saved successfully.", Toast.LENGTH_LONG).show();
		}
	}
	
}
