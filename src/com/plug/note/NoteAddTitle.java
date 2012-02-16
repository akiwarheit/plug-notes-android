package com.plug.note;

import keendy.projects.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class NoteAddTitle extends Dialog{
	
	public NoteAddTitle(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.title_dialog);
	}

	

	

	
}

