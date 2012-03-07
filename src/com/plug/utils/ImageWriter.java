package com.plug.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class ImageWriter {
  private static final String TAG = ImageWriter.class.getSimpleName();
  
  public static boolean writeAsJPG(Context context, Bitmap bitmap,
      String filename) {
    int quality = 100;
    
    filename = filename + ".jpg";
    Log.d("WRITING", "writing image: " + filename);
    FileOutputStream fos = null;
    try {
      fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Log.d(TAG, "file not found");
      return false;
    }
    bitmap.compress(CompressFormat.JPEG, quality, fos);
    Log.d(TAG, "finished writing");
    
    // bos.flush();
    // bos.close();
    try {
      fos.flush();
      fos.close();
    } catch (IOException e) {
      Log.d(TAG, "error closing");
      e.printStackTrace();
    }
    // ExifInterface exif = new ExifInterface(file.getAbsolutePath());
    // exif.setAttribute(ExifInterface.TAG_DATETIME, new TimeStamp().getDate());
    // exif.saveAttributes();
    Log.d(TAG, "Saved successful");
    
    return true;
  }
  
  public static int[] decodeYUV420SP(byte[] yuv420sp, int width, int height) {
    
    final int frameSize = width * height;
    
    int rgb[] = new int[width * height];
    for (int j = 0, yp = 0; j < height; j++) {
      int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
      for (int i = 0; i < width; i++, yp++) {
        int y = (0xff & ((int) yuv420sp[yp])) - 16;
        if (y < 0)
          y = 0;
        if ((i & 1) == 0) {
          v = (0xff & yuv420sp[uvp++]) - 128;
          u = (0xff & yuv420sp[uvp++]) - 128;
        }
        
        int y1192 = 1192 * y;
        int r = (y1192 + 1634 * v);
        int g = (y1192 - 833 * v - 400 * u);
        int b = (y1192 + 2066 * u);
        
        if (r < 0)
          r = 0;
        else if (r > 262143)
          r = 262143;
        if (g < 0)
          g = 0;
        else if (g > 262143)
          g = 262143;
        if (b < 0)
          b = 0;
        else if (b > 262143)
          b = 262143;
        
        rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00)
            | ((b >> 10) & 0xff);
        
      }
    }
    return rgb;
  }
  
}
