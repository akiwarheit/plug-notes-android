package com.plug.notebook;

import java.util.List;

import keendy.projects.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.plug.Action;
import com.plug.PlugApplication;
import com.plug.database.model.Notebook;
import com.plug.database.model.User;
import com.plug.note.NoteAddTitle;
import com.plug.note.NotesListActivity;

public class NotebooksListActivity extends ListActivity implements OnClickListener,DialogInterface.OnClickListener,OnItemLongClickListener,TextWatcher {
	
	private PlugApplication application;
	
	private NotebooksListAdapter adapter;
	private List<Notebook> notebooks;
	private static final String TAG = NotebooksListActivity.class.getSimpleName();
	
	private NoteAddTitle addDialog;
	private EditText title_text;
	private EditText searchEditText;
	private Button addBttn;
	private String title;
	private String searchParameter;
	
//	private NotificationManager notify;
	
	private ListView listView;
	
//	private OnItemLongClickListener itemLongClickListener;
//	private TextWatcher searchTextWatcher;
	
	private final CharSequence[] items = {"Open","Upload", "Delete"};
	private final int OPEN = 0, UPLOAD = 1, DELETE = 2;
	
	private Notebook notebookSearchProjection = new Notebook("");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notebooks_list);
		
  	init();
		loadList();
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		showNotify();
		loadList();
	}
	
	private void loadList() {
		try {
			notebooks = Notebook.query(this, notebookSearchProjection);
			
			adapter = new NotebooksListAdapter(this, R.layout.notebooks_list_row, notebooks);
			this.setListAdapter(adapter);
			
		} finally {
		}
	}
	
  protected void onListItemClick(ListView l, View v, int position, long id) {
	  super.onListItemClick(l, v, position, id);
	  application.setCurrentNotebook(notebooks.get(position));
	  Intent intent = new Intent();
	  intent.setClass(NotebooksListActivity.this, NotesListActivity.class);
	  intent.setAction(Action.VIEW_NOTEBOOK_NOTES);
	  startActivity(intent);
  }
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
  	super.onCreateOptionsMenu(menu);
  	
  	MenuInflater inflater = getMenuInflater();
  	inflater.inflate(R.menu.notebooks_list_menu, menu);
  	
		return true;
	}
  
  @Override
	public boolean onOptionsItemSelected(MenuItem item) {
  	switch (item.getItemId()) {
  		case R.id.notebooks_list_menu_addNotebook:
  			addDialog.show();
  			break;
  
    }
		return true;
	}

	@Override
	public void onClick(View v) {
		title = title_text.getText().toString();
		addDialog.dismiss();
		
		Notebook notebook = new Notebook(title, User.getLoggedInUser(this));
		Notebook.save(this, notebook);
//		showNotify();
		loadList();
		
	}
	
	private void showDialog() {
		Log.i(TAG, application.getCurrentNotebook().isUploaded() ? "true" : "false");
		
		if(application.getCurrentNotebook().isUploaded()) 
			items[1] = "Sync";
		else
			items[1] = "Upload";
		
		Log.i(TAG, application.getCurrentNotebook().getUserId()+"");
		Log.i(TAG, application.getCurrentNotebook().getId()+"");
		Builder builder = new Builder(this);
		builder.setTitle(application.getCurrentNotebook().getDescription());
		builder.setItems(items, this);
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void init() {
		listView = getListView();
		searchParameter = "";
		
		application = (PlugApplication) getApplicationContext();
		
		addDialog = new NoteAddTitle(this);
		title_text = (EditText) addDialog.findViewById(R.id.add_title);
		searchEditText = (EditText) findViewById(R.id.notebooks_search_editText);
		addBttn = (Button) addDialog.findViewById(R.id.add_bttn);	
		addBttn.setOnClickListener(this);
		
//		itemLongClickListener = new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				application.setCurrentNotebook(notebooks.get(arg2));
//				Log.i(TAG, application.getCurrentNotebook().getDescription());
//				showDialog(); return true; }
//			
//		};
	
		searchEditText.addTextChangedListener(this);
		listView.setOnItemLongClickListener(this);
	}

  @Override
  public void onClick(DialogInterface dialog, int action) {
    if(action == OPEN) {
  	  Intent intent = new Intent();
      intent.setClass(NotebooksListActivity.this, NotesListActivity.class);
      intent.setAction(Action.VIEW_NOTEBOOK_NOTES);
      startActivity(intent);				
  	} else if (action == DELETE) {
  		Notebook.delete(NotebooksListActivity.this, application.getCurrentNotebook());
  		loadList();
  	} else if (action == UPLOAD) {
  		if(!application.getCurrentNotebook().isUploaded())
  			try {
  				Notebook.upload(NotebooksListActivity.this, application.getCurrentNotebook());
  				Toast.makeText(this, "Upload succeeded!", Toast.LENGTH_LONG).show();
  			} catch (Exception e) {
  				Toast.makeText(this, "Failed to upload!", Toast.LENGTH_LONG).show();
  			}
  	}   
  }

  @Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		application.setCurrentNotebook(notebooks.get(arg2));
		Log.i(TAG, application.getCurrentNotebook().getDescription());
		showDialog();
		return true;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		searchParameter = searchEditText.getText().toString();
		notebookSearchProjection.setDescription(searchParameter);
		loadList();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	
//	public void showNotify(){
//		
//  	NotificationManager manager = (NotificationManager) this
//      	.getSystemService(Context.NOTIFICATION_SERVICE);
//  	Notification notification = new Notification(R.drawable.plug_icon_green_small,
//  	"PLUG Notes",System.currentTimeMillis());
//  	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//  	new Intent(this, NotebooksListActivity.class), 0);
//  	notification.setLatestEventInfo(this, "PLUG Notes",
//  	"by JACK", contentIntent);
//  	notification.flags = Notification.FLAG_INSISTENT;
//  	manager.notify(0, notification);
//				
//	}
}
