package com.plug.gallery;

import java.io.FileInputStream;

import com.plug.doodle.DoodleOnImage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends Activity {
	 /** Called when the activity is first created. */
	 CustomGallery coverFlow;
	 ImageAdapter coverImageAdapter;
	 String[] projection;
	 Uri image_uri;
	 Cursor cursor;
	 int index;
	 
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    projection = new String[]{MediaStore.Images.Thumbnails._ID};
    cursor = managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
    projection, null, null, MediaStore.Images.Thumbnails._ID +" ASC");
    coverFlow = new CustomGallery(this);
    
    coverFlow.setAdapter(new ImageAdapter(this));

    coverImageAdapter =  new ImageAdapter(this);
    
     coverFlow.setAdapter(coverImageAdapter);
    
    coverFlow.setSpacing(-25);
    coverFlow.setSelection(4, true);
    coverFlow.setAnimationDuration(1000);
    
        
    setContentView(coverFlow);
    
    coverFlow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {            
             Intent intent = new Intent(GalleryActivity.this,DoodleOnImage.class);
             intent.putExtra("image_path", getRealPathFromURI(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,position));
             startActivity(intent);
        }
    });
   }

public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;

    private FileInputStream fis;    
    private Integer[] mImageIds = new Integer[cursor.getCount()];
    private ImageView[] mImages;
 
    public ImageAdapter(Context c) {
     mContext = c;
     mImages = new ImageView[mImageIds.length];
    }
 public boolean createReflectedImages() {
         final int reflectionGap = 4; 
         
         int index = 0;
         for (int imageId : mImageIds) {
         Bitmap originalImage = BitmapFactory.decodeResource(getResources(), 
         imageId);
          int width = originalImage.getWidth();
          int height = originalImage.getHeight();
          
    
          //This will not scale but will flip on the Y axis
          Matrix matrix = new Matrix();
          matrix.preScale(1, -1);
          
          Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
          
          Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
            , (height + height/2), Config.ARGB_8888);
        
         Canvas canvas = new Canvas(bitmapWithReflection);
         canvas.drawBitmap(originalImage, 0, 0, null);
         Paint deafaultPaint = new Paint();
         canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
         canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
         
         Paint paint = new Paint(); 
         LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
           bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
           TileMode.CLAMP); 
  
         paint.setShader(shader); 
         paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
 
         canvas.drawRect(0, height, width, 
           bitmapWithReflection.getHeight() + reflectionGap, paint); 
         
         ImageView imageView = new ImageView(mContext);
         imageView.setImageBitmap(bitmapWithReflection);
         android.widget.Gallery.LayoutParams imgLayout = new CustomGallery.LayoutParams(280, 280);
         imageView.setLayoutParams(imgLayout);
         imageView.setPadding(30, 100, 20, 20);
         mImages[index++] = imageView;
         
         }
      return true;
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
        i.setLayoutParams(new CustomGallery.LayoutParams(280, 280));
        i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
        cursor.moveToPosition(position);
        int imageID = cursor.getInt(index);
        Uri uri =Uri.withAppendedPath(
        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);

        i.setImageURI(uri); 
        return i;

    }

     public float getScale(boolean focused, int offset) { 
         return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
     } 
     
	}
public String getRealPathFromURI(Uri contentUri, int position) {
    String [] proj={MediaStore.Images.Thumbnails.DATA};
    Cursor cursor = managedQuery( contentUri, proj, null,  null,null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
    cursor.moveToPosition(position);

    return cursor.getString(column_index);
}

 }