package com.plug.utils;

import java.util.Arrays;

import com.db4o.query.QueryComparator;
import com.plug.database.model.Notebook;

public class NotebooksSorter implements QueryComparator<Notebook> {

	/**
   * 
   */
  private static final long serialVersionUID = 1L;
//	private boolean ascending = true;
	
	@Override
  public int compare(Notebook n1, Notebook n2) {
		
	
	  return sort(n1.getDescription(),n2.getDescription());
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
