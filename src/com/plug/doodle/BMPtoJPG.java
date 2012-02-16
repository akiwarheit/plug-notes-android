package com.plug.doodle;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.plug.utils.DateUtils;

public class BMPtoJPG {

	private DateUtils dateUtils;
	private static Bitmap bitmap;

	private static int quality = 100;
	
	private static String userID;
	private static String fileName;
	private static String pathStorage;
	private static String TAG = "Bitmap Error";

	private static File directory;
	private static File file;
	private static File bitmapPath;
	
	private static FileOutputStream fos = null;
	private static FileInputStream fis = null;
	private static FileDescriptor fd = null;

	private static long date;

	public BMPtoJPG(String userId, File bitmapPath) {
		BMPtoJPG.userID = userId;
		BMPtoJPG.bitmapPath = bitmapPath;

		// Create folder in SD card
		pathStorage = Environment.getExternalStorageDirectory().toString();
		directory = new File(pathStorage + "/" + userId);
		directory.mkdir();
		
		// fileName is based on the date to avoid redundancy
		date = System.currentTimeMillis();
		dateUtils = new DateUtils(date);
		fileName = "/" + dateUtils.getStringDate();

		try {
			// Get bitmap to be compress
			fis = new FileInputStream(bitmapPath);
			fd = fis.getFD();
			bitmap = BitmapFactory.decodeFileDescriptor(fd);
			bitmap = BitmapFactory.decodeFile(bitmapPath.toString());

			// Place the file on the created folder
			file = new File(directory, "/" + fileName + ".jpg");
			fos = new FileOutputStream(file);

			// To Convert bitmap to Jpeg
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "" + e.getMessage());
		}
	}

	public static Bitmap getBitmap() {
		return bitmap;
	}

	public static void setBitmap(Bitmap bitmap) {
		BMPtoJPG.bitmap = bitmap;
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File file) {
		BMPtoJPG.file = file;
	}

	public static String getUserID() {
		return userID;
	}

	public static void setUserID(String userID) {
		BMPtoJPG.userID = userID;
	}

	public static File getBitmapPath() {
		return bitmapPath;
	}

	public static void setBitmapPath(File bitmapPath) {
		BMPtoJPG.bitmapPath = bitmapPath;
	}
}