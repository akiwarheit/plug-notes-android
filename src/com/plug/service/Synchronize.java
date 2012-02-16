package com.plug.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.plug.PlugApplication;
import com.plug.database.model.Note;
import com.plug.database.model.Notebook;

public class Synchronize extends Service{
	//checks whether the notebook is already uploaded
	//if uploaded check for the notes
	//else 
	PlugApplication plugApp;
	private List<Notebook> notebooks;
	private List<Note> notes;
	private ArrayList<Notebook> unUploadedNotebooks;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		plugApp = (PlugApplication) getApplicationContext();
		notebooks = new ArrayList<Notebook>();
		unUploadedNotebooks = new ArrayList<Notebook>();
		notebooks = Notebook.all(plugApp);
		
		for(int a=0; a<notebooks.size();a++){
			if(!notebooks.get(a).isUploaded()){
				unUploadedNotebooks.add(notebooks.get(a));
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	

}
