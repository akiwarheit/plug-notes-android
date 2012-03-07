package com.plug.note;

import java.util.List;

import keendy.projects.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.plug.Action;
import com.plug.PlugApplication;
import com.plug.database.model.Note;
import com.plug.database.model.Notebook;

public class NotesListActivity extends ListActivity implements OnClickListener,
    TextWatcher, OnItemLongClickListener {
  
  private PlugApplication application;
  
  private static final String TAG = "List Activity: ";
  
  private NotesListAdapter adapter;
  private List<Note> notes;
  
  private Note predicate = new Note("");
  
  private ListView listView;
  // private OnItemLongClickListener itemLongClickListener;
  private final CharSequence[] items = { "Open", "Upload", "Delete" };
  private final int OPEN = 0, UPLOAD = 1, DELETE = 2;
  
  private String searchParameter;
  // private TextWatcher searchTextWatcher;
  private EditText searchEditText;
  private String action;
  
  AlertDialog dialog;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.notes_list);
    
    final Intent intent = getIntent();
    action = intent.getAction();
    init();
    loadList();
  }
  
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Note note = notes.get(position);
    
    Log.i(TAG, "" + note.getTitle());
    Log.i(TAG, "" + note.getContent());
    application.setCurrentNote(note);
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_EDIT);
    intent.setClass(NotesListActivity.this, NoteEditorActivity.class);
    startActivity(intent);
  }
  
  public void onResume() {
    super.onResume();
    
    Log.i(TAG, "Resuming");
    loadList();
  }
  
  private void loadList() {
    if (action.equals(Action.VIEW_NOTEBOOK_NOTES)) {
      predicate.setNotebook(Notebook.find(this,
          application.getCurrentNotebook()));
      notes = Note.query(this, predicate);
      Log.i(TAG, predicate.getNotebook().getDescription());
    }
    if (action.equals(Action.VIEW_ALL_NOTES)) {
      // predicate.setNotebook(predicateNotebook);
      // notes = Note.query(this, predicate);
      // notes = Note.all(this);
      notes = Note.query(this, predicate.getTitle());
    }
    
    adapter = new NotesListAdapter(this, R.layout.notes_list_row, notes);
    this.setListAdapter(adapter);
    
  }
  
  private void init() {
    application = (PlugApplication) getApplicationContext();
    
    searchEditText = (EditText) findViewById(R.id.notebooks_search_editText);
    
    listView = getListView();
    
    // itemLongClickListener = new OnItemLongClickListener() {
    // @Override
    // public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
    // long arg3) {
    // application.setCurrentNote(notes.get(arg2));
    // showDialog();
    // Log.i(TAG, application.getCurrentNote().getTitle());
    // return true;
    // }
    //
    // };
    
    // searchTextWatcher = new TextWatcher() {
    // @Override
    // public void afterTextChanged(Editable arg0) {
    // searchParameter = searchEditText.getText().toString();
    // predicate.setTitle(searchParameter);
    // loadList();
    // }
    //
    // @Override
    // public void beforeTextChanged(CharSequence s, int start, int count,
    // int after) {
    // }
    //
    // @Override
    // public void onTextChanged(CharSequence s, int start, int before, int
    // count) {
    // }
    // };
    
    listView.setOnItemLongClickListener(this);
    searchEditText.addTextChangedListener(this);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.notes_list_menu, menu);
    
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.notes_list_menu_addNotes:
        if (action.equals(Action.VIEW_NOTEBOOK_NOTES)) {
          Intent intent = new Intent();
          intent.setClass(NotesListActivity.this, NoteEditorActivity.class);
          intent.setAction(Action.INSERT_NOTE_IN_NOTEBOOK);
          startActivity(intent);
        }
        if (action.equals(Action.VIEW_ALL_NOTES)) {
          Intent intent = new Intent();
          intent.setClass(NotesListActivity.this, NoteEditorActivity.class);
          intent.setAction(Intent.ACTION_INSERT);
          startActivity(intent);
        }
        break;
    
    }
    return true;
  }
  
  private void showDialog() {
    Log.i(TAG, application.getCurrentNote().isUploaded() ? "true" : "false");
    
    if (application.getCurrentNote().isUploaded())
      items[1] = "Sync";
    else
      items[1] = "Upload";
    
    Log.i(TAG, application.getCurrentNote().getUserId() + "");
    Log.i(TAG, application.getCurrentNote().getId() + "");
    Builder builder = new Builder(this);
    builder.setTitle(application.getCurrentNote().getTitle());
    builder.setItems(items, this);
    
    dialog = builder.create();
    dialog.show();
  }
  
  @Override
  public void onClick(DialogInterface dialog, int action) {
    if (action == OPEN) {
      Intent intent = new Intent();
      intent.setClass(NotesListActivity.this, NoteEditorActivity.class);
      intent.setAction(Intent.ACTION_EDIT);
      startActivity(intent);
    } else if (action == DELETE) {
      Note.delete(NotesListActivity.this, application.getCurrentNote());
      loadList();
    } else if (action == UPLOAD) {
      if (!application.getCurrentNote().isUploaded()
          && application.getCurrentNotebook().isUploaded())
        try {
          Note.upload(this, application.getCurrentNote());
          Toast.makeText(this, "Upload succeeded!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
          Toast.makeText(this, "Failed to upload!", Toast.LENGTH_LONG).show();
          
        }
    }
  }
  
  @Override
  public void afterTextChanged(Editable arg0) {
    searchParameter = searchEditText.getText().toString();
    predicate.setTitle(searchParameter);
    loadList();
  }
  
  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }
  
  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }
  
  @Override
  public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
      long arg3) {
    application.setCurrentNote(notes.get(arg2));
    showDialog();
    Log.i(TAG, application.getCurrentNote().getTitle());
    return true;
  }
}
