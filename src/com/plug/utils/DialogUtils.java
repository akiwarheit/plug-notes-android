package com.plug.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

	public static void showAlertDialog(final Context context,
			final String title, final String message,
			final DialogListener listener) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				if (listener != null) {
					listener.onDialogClosed();
				}

			}
		});
		alertDialog.show();

	}

	public interface DialogListener {

		public void onDialogClosed();

	}

}
