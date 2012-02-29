package com.plug.note;


import java.io.File;
import java.util.ArrayList;

import keendy.projects.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

import com.plug.Action;
import com.plug.PlugApplication;
import com.plug.database.model.Note;
import com.plug.database.model.Notebook;
import com.plug.database.model.User;
import com.plug.image.handler.Image;
import com.plug.main.HomeActivity;

/**
 * TODO implement menus for editing the title, discarding etc
 */
public class NoteEditorActivity extends Activity implements OCRCallback {

	 private PlugApplication application;
	 private static String TAG = "Note Editor";
  
	  /**  Different states this note editor may enter */
	  private static final int STATE_EDIT = 0;
	  private static final int STATE_INSERT = 1;
	
	  private static final int REQUEST_OCR = 1002;
	  private int SELECT_PICTURE = 1001;
		
	  /** our variables worth noting :) */
	     		
	  private EditText noteView;
	  private EditText titleEditText;  
	  private Note note;
	  private Gallery gallery;
	  
	  /** Our current state */
	  private int mState;
	  
	  /** SQLite variables or projections */
	  private String content;
	   
	  private Notebook notebook;
	  private String action;
	  private String title = "Untitled";
	  
	  private User currentUser;
	  
	  private NotificationManager manager;
	  
	  private String selectedImagePath;
	  private ArrayList<String> path2;
//	  private String[] projection;
//	  private Cursor cursor;
	  private WindowManager win;
  
   
  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (PlugApplication) getApplicationContext();
		
		setContentView(R.layout.note_editor2);
		/** Get the intent (either insert new note or edit existing */
		final Intent intent = getIntent();
		action = intent.getAction();

		init();
		showNotification();
		
		}
  
	
	/*
   * TODO implement options menu depending on intent
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
  	super.onCreateOptionsMenu(menu);

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.title_menu, menu);
     
		return true;
  }
  
  @Override
	public boolean onOptionsItemSelected(MenuItem item) {
  	switch (item.getItemId()) {
      case R.id.cancel_menu:
        startActivity(new Intent(NoteEditorActivity.this, HomeActivity.class));
        break;
      case R.id.camera_menu:
    	  callCamera();
    	  break;
      case R.id.note_attach:
    	  gallery.setVisibility(View.VISIBLE);
			 Intent intent = new Intent();
          intent.setType("image/*");
          intent.setAction(Intent.ACTION_GET_CONTENT);
          startActivityForResult(Intent.createChooser(intent,
                  "Select Picture"), SELECT_PICTURE);
          break;
      default:
        return super.onOptionsItemSelected(item);
  		}
  	
  	return false;
 }

  
  @Override
  public void onDestroy() {
		manager.cancelAll();
		super.onDestroy();
  }
  
  @Override
  public void onBackPressed() {
  	preSave();
		switch(mState) {
		  case STATE_INSERT:
		  	if(!content.equals("")) {
		  		note.setTitle(title);
		  		note.setContent(content);
 					if(Action.INSERT_NOTE_IN_NOTEBOOK.equals(action)) {   				
 						notebook = application.getCurrentNotebook();
 						note.setNotebook(notebook);
 					}
  				try {
  					note.setPaths(path2);
    				Note.store(this, note);
    				Log.e("", note.getContent());
    				Log.e("",note.getTitle());
    				Log.e("",note.getListSize()+"");
  				} finally {
  					
  				}
		  	}
				finish();
				break;
		  case STATE_EDIT:
		  	Note updatedNote = new Note(title, content);
//		  	updatedNote.setAndroidCreated(note.getAndroidCreated());
		  
		  	updatedNote.setUserId(User.getLoggedInUser(this).getId());
		  	updatedNote.setNotebook(application.getCurrentNotebook());
		  	updatedNote.setPaths(path2);
		  	Note.update(this, note, updatedNote);
				finish();
				break;
		}
  }

	
	
	public void callCamera(){
		Log.d(TAG, "Starting camera...");
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, REQUEST_OCR);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (resultCode == Activity.RESULT_CANCELED) {
		}
		 if (requestCode == SELECT_PICTURE) {
//	  		  Bitmap bitmap = null;
	          Uri selectedImageUri = data.getData();
	          selectedImagePath = getPath(selectedImageUri);
	          path2.add(selectedImagePath);	 
	          //note.setPaths(selectedImagePath);
//	          bitmap = Image.rescaleBitmap(getApplicationContext(), selectedImageUri, win);	        		         
	          gallery.setAdapter(new ImageAdapter(getApplicationContext()));
	     }
		
		if(requestCode == REQUEST_OCR) {
			if(resultCode == RESULT_OK) {
				Bitmap x = (Bitmap) data.getExtras().get("data");
//				String storage = Environment.getExternalStorageDirectory().toString() + "/plug/";
//				String absoluteStorage = storage + "tessdata/";
//				
//				String[] paths = new String[] { storage, absoluteStorage };
//				
//				for(String path : paths) {
//					File dir = new File(path);
//  				dir.mkdirs();
//				}
//			
//				if (!(new File(storage + "tessdata/eng.traineddata")).exists()) {
//					try {
//						
//						AssetManager manager = getAssets();
//						InputStream is = manager.open("tessdata/eng.traineddata");
//						OutputStream os = new FileOutputStream(storage + "tessdata/eng.traineddata");
//						
//						byte[] buf = new byte[1024];
//						int len;
//						
//						while((len = is.read(buf)) > 0) {
//							os.write(buf,0,len);
//						}
//						
//						is.close();
//						os.close();
//						
//						Log.i("SUCCESS: ", "coppied eng.traineddata");
//						
//					} catch (IOException e) {
//						Log.e("FAILED TO COPY: ", e.getMessage());					
//					}
//				}

				
				new OCRTask(this,x,this).execute();

//				TessBaseAPI tesseract = new TessBaseAPI();
//				tesseract.setDebug(true);
//				tesseract.init(FileManager.STORAGE_PATH, "eng");
//				tesseract.setImage(x);
//				String result = tesseract.getUTF8Text();
//				
//				Log.i(TAG, result);
//				Toast.makeText(this, result, Toast.LENGTH_LONG);
//				tesseract.end();
//				
//				noteView.setText(noteView.getText() + " " + result);
			}
		}
	}
	
	private void preSave() {
		if(noteView.getText().toString() != "") {
			if(!titleEditText.getText().toString().equals(""))
				title = titleEditText.getText().toString();
			
  		content = noteView.getText().toString(); 
		}
	}
	
	
	private void init() {
		note = new Note();
		win = getWindowManager();
		titleEditText = (EditText) findViewById(R.id.note_title);
		gallery = (Gallery) findViewById(R.id.gallery_image);
		path2 = new ArrayList<String>();
			
//		projection = new String[]{MediaStore.Images.Thumbnails._ID};
//    cursor = managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
//    projection, null, null, MediaStore.Images.Thumbnails._ID +" ASC");
		
		currentUser = User.getLoggedInUser(this);
		noteView=(EditText) findViewById(R.id.note);
	  
		/** Set current state depending on intent */
		if(Intent.ACTION_INSERT.equals(action)) {
		  mState = STATE_INSERT; 		
  		  note.setUserId(currentUser.getId());
		} 
		
		else if(Intent.ACTION_EDIT.equals(action)) {
		  
		  mState = STATE_EDIT;
  		  note = application.getCurrentNote();
  		  title = note.getTitle();
  		  content = note.getContent();
  		  path2 = note.getPaths();

		  titleEditText.setText(title);
		  noteView.setText(content);
		  path2 = note.getPaths();
		
		  if(path2.size()>0){ 
			 gallery.setAdapter(new ImageAdapter(getApplicationContext()));
			 gallery.setVisibility(View.VISIBLE);
		  }
		 
		}	
		else if(Action.INSERT_NOTE_IN_NOTEBOOK.equals(action)) {
			mState = STATE_INSERT;
	  		note = new Note();
	  		note.setUserId(currentUser.getId());
			notebook = application.getCurrentNotebook();
		}
	}
	
	private void showNotification() {
		
		manager = (NotificationManager) this
		.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.plug_icon_green_small,
		"PLUG Notes",System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		new Intent(this, NoteEditorActivity.class), 0);
		notification.setLatestEventInfo(this, "PLUG Notes",
		"by JACK", contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;
		manager.notify(0, notification);
}

	
	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    
	 
	    private Integer[] mImageIds = new Integer[path2.size()];
//	    private ImageView[] mImages;
	       
	    public ImageAdapter(Context c) {
	       mContext = c;
//	       mImages = new ImageView[mImageIds.length];
	     }
	    public int getCount() {
	      return mImageIds.length;
	    }
	   

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView i = new ImageView(mContext); 
	        Bitmap bitmap = Image.rescaleBitmap(getApplicationContext(), 
	        		Uri.fromFile(new File(path2.get(position))), win);
      		i.setImageBitmap(bitmap);  
	        return i;

	    } 
	}
	
//	public class OCRTask extends AsyncTask<Void, Integer, String> {
//		
//		private ProgressDialog progress;
//		private Context context;
//		private Bitmap imageToProcess;
//		private OCRCallback callback;
//		private TessBaseAPI tesseract; 
//		
//		public OCRTask(Context context, Bitmap bitmap, OCRCallback callback) {
//			this.context = context;
//			this.imageToProcess = bitmap.copy(Bitmap.Config.ARGB_8888, true);;
//			progress = new ProgressDialog(context);
//			this.callback = callback;
//		}
//		
//		@Override
//		protected void onPreExecute() {
//			FileManager manager = new FileManager(context);
//			manager.writeRawToSD(FileManager.TESSERACT_PATH + "eng.traineddata", "tessdata/eng.traineddata");
//			this.progress.setIndeterminate(true);
//			this.progress.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.spinner_loading));
//			this.progress.setMessage("Recognizing using Tesseract, bro.");
//			this.progress.setProgress(0);
//			this.progress.show();
//			tesseract = new TessBaseAPI();
//			tesseract.setDebug(true);
//			tesseract.init(FileManager.STORAGE_PATH, "eng");
//			tesseract.setImage(imageToProcess);
//		}
//
//		@Override
//    protected String doInBackground(Void... params) {
//			String result = tesseract.getUTF8Text();
//	    return result;
//    }
//		
//		protected void onPostExecute(String finish) {
//			if(progress.isShowing())
//				progress.dismiss();
//			
//			callback.onFinishRecognition(finish);
//			
//			tesseract.end();
//		}	
//		
//	}

	@Override
  public void onFinishRecognition(String recognizedText) {
		noteView.setText(noteView.getText() + " " + recognizedText);
  }
	
}