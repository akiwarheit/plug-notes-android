package com.plug.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class DialogFactory {
	
	public static void showInfoDialog(Context context, String title, String message){
		showInfoDialog(context, title, message, "Close");
	}

	public static void showInfoDialog(Context context, String title, String message, String confirmMessage){
		final AlertDialog dialog = createDialog(context, title, message);
		dialog.setButton(confirmMessage, new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialog.dismiss();
				
			}});
		
		dialog.show();
		
	}
	
	private static AlertDialog createDialog(Context context, String title, String message){
		final AlertDialog dialog =new AlertDialog.Builder(context).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		return dialog;
		
	}
	
	public static void showConfirmDialog(Context context, String title, String message, String positiveMessage, String negativeMessage,final DialogActionListener listener){
		
	
		final AlertDialog dialog = createDialog(context, title, message);
		dialog.setButton(Dialog.BUTTON_POSITIVE,positiveMessage, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listener.onClickPositiveButton(null);
				
			}});
	
		
		dialog.setButton(Dialog.BUTTON_NEGATIVE,negativeMessage, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();		
			}});
		
		dialog.show();
	}
	
	
	
	public static void showConfirmDialog(Context context, String title, String message,final DialogActionListener listener){
		showConfirmDialog(context, title, message,"Yes", "No", listener);
		
		
	}
	
	
	public static void showTextInputDialog(Context context, String title, String message,final DialogActionListener listener){
		final AlertDialog dialog = createDialog(context, title, message);
		final EditText text = new EditText(context);
		dialog.setView(text);
		dialog.setButton(Dialog.BUTTON_POSITIVE,"Ok", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listener.onClickPositiveButton(text.getText().toString());
				
			}});
	
		
		dialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();		
			}});
			dialog.show();
		
		
	}
	
	public interface DialogActionListener{
		
		public void onClickPositiveButton(Object object);
		
	}
}

