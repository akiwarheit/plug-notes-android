package com.plug.database.provider;

import java.util.List;

import android.content.Context;

import com.plug.database.Db4oHelper;
import com.plug.database.model.Notebook;

public class NotebooksProvider extends Db4oHelper {
  
  private static NotebooksProvider provider = null;
  
  public NotebooksProvider(Context context) {
    super(context);
  }
  
  public static NotebooksProvider getInstance(Context context) {
    if (provider == null)
      provider = new NotebooksProvider(context);
    
    return provider;
  }
  
  public void store(Notebook notebook) {
    db().store(notebook);
  }
  
  public void delete(Notebook notebook) {
    db().delete(notebook);
  }
  
  public List<Notebook> findAll() {
    return db().query(Notebook.class);
  }
  
}
