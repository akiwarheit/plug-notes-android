package com.plug.notebook;

import java.util.List;

import keendy.projects.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plug.database.model.Notebook;

public class NotebooksListAdapter extends ArrayAdapter<Notebook> {
  
  private Context context;
  private List<Notebook> notebooks;
  
  public NotebooksListAdapter(Context context, int textViewResourceId,
      List<Notebook> objects) {
    super(context, textViewResourceId, objects);
    
    this.context = context;
    this.notebooks = objects;
    
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    
    if (v == null) {
      LayoutInflater vi = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = vi.inflate(R.layout.notebooks_list_row, null);
    }
    Notebook notebook = notebooks.get(position);
    if (notebook != null) {
      TextView view = (TextView) v
          .findViewById(R.id.notebook_list_row_description);
      if (view != null)
        view.setText(notebook.getDescription());
    }
    return v;
  }
  
}
