package com.plug.doodle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import keendy.projects.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.plug.main.HomeActivity;
import com.plug.utils.DialogFactory;
import com.plug.utils.DialogFactory.DialogActionListener;
import com.plug.utils.ImageWriter;

public class DoodleOnImage extends Activity{
private final static String TAG = DoodleOnImage.class.getSimpleName();

	
	private Bitmap doodleBitmap;
	private Uri doodle = null;

	private DoodleView doodleView = null;
	private JPGToBMP convertToBMP;
	
	
	

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        setContentView(R.layout.doodle);
        doodleView = (DoodleView) findViewById(R.id.doodle_view);
        doodleView.setBitmap(convertToBMP.covert(new File(getIntent().getStringExtra("image_path"))));
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
	    	//doodleBitmap = doodleView.getBitmap();
	    	saveDoodle(doodleView);
	    	//Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();
	    	this.finish();
	    	startActivity(new Intent(DoodleOnImage.this, HomeActivity.class));
	    	return true;
	    case R.id.doodle_undo:
	    	doodleView.clear();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	
//	private void saveDoodle(View v){
//		ImageWriter.writeAsJPG(DoodleOnImage.this, doodleBitmap, "doodle");
//	}

	@Override
	public void onBackPressed() {

//		this.finish();
//		super.onBackPressed();
		if(!isEmpty(doodleView)){
			
		DialogFactory.showConfirmDialog(DoodleOnImage.this, "Warning", 
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
