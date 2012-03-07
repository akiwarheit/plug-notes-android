package com.plug.note;

import java.util.List;

import keendy.projects.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plug.database.model.Note;

public class NotesListAdapter extends ArrayAdapter<Note> {
  
  private Context context;
  private List<Note> notes;
  
  public NotesListAdapter(Context context, int textViewResourceId,
      List<Note> objects) {
    super(context, textViewResourceId, objects);
    this.context = context;
    this.notes = objects;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    if (v == null) {
      LayoutInflater vi = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = vi.inflate(R.layout.notes_list_row, null);
    }
    Note note = notes.get(position);
    if (note != null) {
      TextView view = (TextView) v.findViewById(R.id.list_title_textView);
      if (view != null) {
        view.setText(note.getTitle());
      }
    }
    return v;
  }
}
