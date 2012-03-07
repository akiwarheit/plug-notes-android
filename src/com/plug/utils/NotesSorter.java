package com.plug.utils;

import java.util.Arrays;

import com.db4o.query.QueryComparator;
import com.plug.database.model.Note;

public class NotesSorter implements QueryComparator<Note> {

	/**
   * 
   */
  private static final long serialVersionUID = 1L;
//	private boolean ascending = true;
	
	@Override
  public int compare(Note note1, Note note2) {
	  return sort(note1.getTitle(), note2.getTitle());
  }
	
	private int sort(String string1, String string2) {
		int result = 0;
		String[] descriptions = {string1, string2};
		
		Arrays.sort(descriptions);

		if(descriptions[0].equals(string1))
			result = -1;
		else
			result = 1;
		
		
		return result;
	}
	
//	public void sortByAscending(boolean ascending) {
//		this.ascending = ascending;
//	}
}
