package com.plug.doodle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import keendy.projects.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.plug.database.model.User;
import com.plug.main.HomeActivity;
import com.plug.utils.DialogFactory;
import com.plug.utils.DialogFactory.DialogActionListener;

public class DoodleActivity extends Activity{    

	private final static String TAG = DoodleActivity.class.getSimpleName();

	String path = Environment.getExternalStorageDirectory().toString();
//    OutputStream fOut = null;
//    File file = new File(path, "doodletest.jpg");
	
	private Bitmap doodleBitmap;
	private Uri doodle = null;

	private DoodleView doodleView = null;

	private User user;
	
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.d(TAG, "***Starting Doodle Activity"); //+get the name of the activity
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doodle);
        doodleView = (DoodleView) findViewById(R.id.doodle_view);
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.doodle_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.doodle_save:
	    	saveImage(doodleView);
	    	//Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();
	    	this.finish();
	    	startActivity(new Intent(DoodleActivity.this, HomeActivity.class));
	    	return true;
	    case R.id.doodle_undo:
	    	doodleView.clear();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	
//	private void saveDoodle(View v){
//		ImageWriter.writeAsJPG(DoodleActivity.this, doodleBitmap, "doodle");
//	}

	@Override
	public void onBackPressed() {
		
		
//		this.finish();
//		super.onBackPressed();
		if(!isEmpty(doodleView)){
			
		DialogFactory.showConfirmDialog(DoodleActivity.this, "Warning", 
					"Do you wish to save your doodle?", new DialogActionListener(){

				@Override
				public void onClickPositiveButton(Object object) {
					saveDoodle(doodleView);
					Log.d(TAG, "***saved doodle");
					doodleView.clear();
				}
				});
		}
		if(isEmpty(doodleView)){
			this.finish();
//			Intent i = new Intent(this, HomeActivity.class);
//			startActivity(i);
		}
		this.finish();
	}
	
	private void saveDoodle(View v){
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.buildDrawingCache(true);

		Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		
		if (bm != null) {
		    try {
		        String path = Environment.getExternalStorageDirectory().toString();
		        OutputStream fOut = null;
		        File file = new File(path, "doodletest.jpg");
		        fOut = new FileOutputStream(file);
		        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		        fOut.flush();
		        fOut.close();
		        Log.e("ImagePath", "Image Path : " + MediaStore.Images.Media.insertImage
		        		( getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName()));
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    Log.d(TAG, "***doodle saved");
		    Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();
		}
	}
	
//	public static Bitmap loadBitmapFromView(View v) {
//	    Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
//	    Canvas c = new Canvas(b);
//	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//	    v.draw(c);
//	    return b;
//	}
	
	 private void saveImage(View v){

	    	boolean mExternalStorageAvailable = false;
	    	boolean mExternalStorageWriteable = false;
	    	String state = Environment.getExternalStorageState();

	    	if (Environment.MEDIA_MOUNTED.equals(state)) {
	    		// We can read and write the media
	    		mExternalStorageAvailable = mExternalStorageWriteable = true;
	    		Log.d(TAG, "***SD Card support");
	    		
//	    		Canvas c = new Canvas(wallPaperBitmap);
//	    		v.draw(c);
	    		Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	    	    Canvas c = new Canvas(b);
	    	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	    	    v.draw(c);
	    		
	    		
//	    		File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Doodle/SampleImages");
//	    		imagesFolder.mkdirs();
	    		String filename;
	    		Date date = new Date(0);
	    		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
	    		filename =  sdf.format(date);
	    		
	    		try{
	        	 	String path = Environment.getExternalStorageDirectory().toString();
	    			OutputStream fOut = null;
	        	 	File file = new File(path, "/DCIM/Plugnotes/"+filename+".jpg");
	        	 	file.mkdirs();
//	        		File image = new File(imagesFolder,filename + ".jpg");
	        		Uri uriSavedImage = Uri.fromFile(file);
//	      
	        		fOut = new FileOutputStream(file);

	        		b.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	        		fOut.flush();
	        		fOut.close();
	        		Log.d(TAG, "***doodle saved");
	        		Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();

	        		MediaStore.Images.Media.insertImage(getContentResolver(),file
	        				.getAbsolutePath(),file.getName(),file.getName());
	        		
	    		} catch (Exception e) {
	        	 	e.printStackTrace();
	         }
	         
//	    	    // We can read and write the media
//	    	    mExternalStorageAvailable = mExternalStorageWriteable = true;
//	    	    Log.d(TAG, "***SD Card support");
//	    	    Canvas c = new Canvas(wallPaperBitmap);
//	        	v.draw(c);
//	        	File imagesFolder = new File(Environment.getExternalStorageDirectory(), "PlugNotes/Doodle");
//	        	imagesFolder.mkdirs();
//	        	String filename = String.valueOf(System.currentTimeMillis()) ;
//	        	ContentValues values = new ContentValues();   
//	        	values.put(Images.Media.TITLE, filename);
//	        	values.put(Images.Media.DATE_ADDED, System.currentTimeMillis()); 
//	        	values.put(Images.Media.MIME_TYPE, "image/jpeg");
	//
//	        	String meta = String.valueOf(values);
////	        	File image = new File(imagesFolder, "image_001.jpg");
////	        	Uri uriSavedImage = Uri.fromFile(image);
//	        	
////	      	Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
//	        	
//	        	
//	        	try {
////	        		OutputStream outStream = getContentResolver().openOutputStream(uri);
//	        		OutputStream outStream = openFileOutput(meta, Context.MODE_PRIVATE);	
//	        	 wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
//	        	 outStream.flush();
//	        	 outStream.close();
//	        	 Log.d(TAG, "***doodle saved");
//	    		   Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();
//	        	} catch (FileNotFoundException e) {
//	        	 e.printStackTrace();
//	        	} catch (IOException e) {
//	        	 e.printStackTrace();
//	        	}
	    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    	    // We can only read the media
	    	    mExternalStorageAvailable = true;
	    	    mExternalStorageWriteable = false;
	    	} else {
	    	    // Something else is wrong. It may be one of many other states, but all we need
	    	    //  to know is we can neither read nor write
	    	    mExternalStorageAvailable = mExternalStorageWriteable = false;
	    	    
	    	    Log.d(TAG, "***No SD Card mounted on your phone");
	    	}
	    	
	    }

	
	private boolean isEmpty(View v){
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.buildDrawingCache(true);

		Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		if(bm != null){
			Log.d(TAG, "Not Empty Bitmap");
			return false;
		} else {
			Log.d(TAG, "Empty Bitmap");
			return true;		
		}
	}
	
	
}
