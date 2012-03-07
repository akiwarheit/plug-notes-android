package com.plug.actions;

import keendy.projects.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.plug.doodle.DrawingActivity;

public class OnShakeAction {
  
  public static AlertDialog dialog;
  
  public void doAction(Context context,
      final DrawingActivity.PaintView paintView) {
    
    if (dialog != null && dialog.isShowing()) {
      return;
    }
    
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    
    builder.setMessage("Are you sure you want to clear drawing?")
        .setCancelable(false).setTitle("Drawing shake")
        .setIcon(R.drawable.alert)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            paintView.onClear();
            dialog.cancel();
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });
    
    dialog = builder.create();
    dialog.show();
  }
}
