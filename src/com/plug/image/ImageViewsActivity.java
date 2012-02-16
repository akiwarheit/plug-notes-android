package com.plug.image;

import keendy.projects.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class ImageViewsActivity extends Activity implements OnItemClickListener{
	
	Context context;
	Cursor cursor;
	ImageView imageView; 
	Gallery imageGallery;
	String[] projection;
	Uri image_uri;
	int index;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		imageGallery= (Gallery) this.findViewById(R.id.image_gallery);
    imageView = (ImageView) this.findViewById(R.id.image_gallery_view);
    
    projection = new String[]{MediaStore.Images.Thumbnails._ID};
    cursor = managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
    projection, null, null, MediaStore.Images.Thumbnails._ID +" ASC");
    
    imageGallery.setAdapter(new ImageAdapter(this));
    imageGallery.setSelection(0);
      
    
		
	}
	
	public class ImageAdapter extends BaseAdapter{
		
		public ImageAdapter(Context context){
			context = context;
		}

		@Override
		public int getCount() {		
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {	
			return position;
		}

		@Override
		public long getItemId(int position) {		
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ImageView i = new ImageView(ImageViewsActivity.this);
      
      cursor.moveToPosition(position);
      int imageID = cursor.getInt(index);
      Uri uri =Uri.withAppendedPath(
         MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
    
      i.setImageURI(uri); 
      i.setLayoutParams(new Gallery.LayoutParams(80, 70));
      i.setScaleType(ImageView.ScaleType.FIT_XY);
     // i.setBackgroundResource(GalItemBg);
      return i;
			
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
		
	}
	
}
