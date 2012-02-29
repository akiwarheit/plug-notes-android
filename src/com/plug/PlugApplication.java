package com.plug;

import android.app.Application;

import com.plug.database.model.Note;
import com.plug.database.model.Notebook;
import com.plug.database.model.User;
import com.plug.database.provider.NotebooksProvider;
import com.plug.database.provider.NotesProvider;
import com.plug.database.provider.UserProvider;

/**
 * 
 * @author Keendy
 * In loving memory of Dennis Ritchie
 * Thank you for making the world an easier place. You are the one true brogrammer.
 * 
 */

public class PlugApplication extends Application {
			
	private User currentUser = null;
	private Note currentNote = null;
	private Notebook currentNotebook = null;
	
//	private Db4oHelper dbHelper = new Db4oHelper(this);
	private NotesProvider notesProvider = NotesProvider.getInstance(this);
	private NotebooksProvider notebookProvider = NotebooksProvider.getInstance(this);
	private UserProvider userProvider = UserProvider.getInstance(this);

	@Override
	public void onCreate() {
		super.onCreate();
//		userProvider.store(currentUser);
	}
	
	public Note getCurrentNote() {
		return currentNote;
	}

	public void setCurrentNote(Note currentNote) {
		this.currentNote = currentNote;
	}

	public NotesProvider getNotesProvider() {
  	return notesProvider;
  }

	public NotebooksProvider getNotebookProvider() {
		return notebookProvider;
	}

	public void setNotebookProvider(NotebooksProvider notebookProvider) {
		this.notebookProvider = notebookProvider;
	}
	
	public Notebook getCurrentNotebook() {
		return this.currentNotebook;
	}
	
	public void setCurrentNotebook(Notebook notebook) {
		this.currentNotebook = notebook;
	}

	public UserProvider getUserProvider() {
		return userProvider;
	}

	public void setUserProvider(UserProvider userProvider) {
		this.userProvider = userProvider;
	}

	public User getCurrentUser() {
//		currentUser = User.getLoggedInUser(this);
		
//		if(currentUser.getEmail() != null)
    	return currentUser;
//		else
//			return null;
  }

	public void setCurrentUser(User currentUser) {
  	this.currentUser = currentUser;
  }

	public void setNotesProvider(NotesProvider notesProvider) {
  	this.notesProvider = notesProvider;
  }
	
}
