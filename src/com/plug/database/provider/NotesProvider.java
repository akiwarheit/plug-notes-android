package com.plug.database.provider;

import java.util.List;

import android.content.Context;

import com.plug.database.Db4oHelper;
import com.plug.database.model.Note;

public class NotesProvider extends Db4oHelper {
  
  private static NotesProvider provider = null;
  
  public NotesProvider(Context context) {
    super(context);
  }
  
  public static NotesProvider getInstance(Context context) {
    if (provider == null)
      provider = new NotesProvider(context);
    
    return provider;
  }
  
  public void store(Note note) {
    db().store(note);
  }
  
  public void delete(Note note) {
    db().delete(note);
  }
  
  public List<Note> findAll() {
    return db().query(Note.class);
  }
}
