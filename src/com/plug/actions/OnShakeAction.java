package com.plug.actions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.plug.doodle.DrawingActivity;

public class OnShakeAction {

    public static AlertDialog _dialog;

    public void doAction(Context context, final DrawingActivity.PaintView paintView){

        if(_dialog != null && _dialog.isShowing()){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Are you sure you want to clear drawing?")
                .setCancelable(false)
                .setTitle("Drawing shake")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        paintView.onClear();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        _dialog = builder.create();
        _dialog.show();
    }
}
