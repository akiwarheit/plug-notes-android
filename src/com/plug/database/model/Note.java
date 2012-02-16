package com.plug.database.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.db4o.query.Predicate;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.plug.PlugApplication;
import com.plug.database.provider.NotesProvider;
import com.plug.image.handler.Image;
import com.plug.utils.NotesSorter;
import com.plug.web.Response;
import com.plug.web.WebService;

public class Note {

	@SerializedName("id") 					 	private long id;
	@SerializedName("user_id") 					private long userId;
	@SerializedName("notebook_id")				private long notebookId;
	@SerializedName("title") 					private String title;
	@SerializedName("content") 					private String content;
	@SerializedName("android_created_at") 		private String androidCreated;
	@SerializedName("android_updated_at")		private String androidUpdated;	
	@SerializedName("created_at") 				private String railsCreated;
	@SerializedName("updated_at")				private String railsUpdated;
	private List<Image>  images;
	private ArrayList<Bitmap> attachedImages;
	private ArrayList<String> paths;
	private Notebook notebook;
	
	private boolean isUploaded = false;
	
	public Note(String title) {
	  this.title = title;
	  this.attachedImages = new ArrayList<Bitmap>();
	  this.paths = new ArrayList<String>();
	  Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
	}
	
	public Note(String title, String content) {
	  this.title = title;
	  this.content = content;
	  this.paths = new ArrayList<String>();
	  this.attachedImages = new ArrayList<Bitmap>();
	  Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
  }

	public Note() {
	  Date date = new Date(System.currentTimeMillis()); 
	  this.attachedImages = new ArrayList<Bitmap>();
	  this.paths = new ArrayList<String>();
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
  }
	
	public long getUserId() {
  	return userId;
  }

	public void setUserId(long userId) {
  	this.userId = userId;
  }

	public String getRailsCreated() {
  	return railsCreated;
  }

	public void setRailsCreated(String railsCreated) {
  	this.railsCreated = railsCreated;
  }

	public String getRailsUpdated() {
  	return railsUpdated;
  }

	public void setRailsUpdated(String railsUpdated) {
  	this.railsUpdated = railsUpdated;
  }

	public long getNotebookId() {
  	return notebookId;
  }

	public void setNotebookId(long notebookId) {
  	this.notebookId = notebookId;
  }

	public Note(long id, String title, String content) {
	  this.id = id;
	  this.title = title;
	  this.content = content;
	  this.attachedImages = new ArrayList<Bitmap>();
	  this.paths = new ArrayList<String>();
	  Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
  }

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public boolean isUploaded() {
  	return isUploaded;
  }

	public void setUploaded(boolean isUploaded) {
  	this.isUploaded = isUploaded;
  }
	
	public void setAndroidCreated(Date created) {
		this.androidCreated = created.toString();
	}
	
	public Date getAndroidCreated() {
		return new Date(androidCreated);
	}
	
	public void setAndroidUpdated(Date updated) {
		this.androidUpdated = updated.toString();
	}
	
	public Date getAndroidUpdated() {
		return new Date(androidUpdated);
	}
	
	public Notebook getNotebook() {
		return notebook;
	}
	
	public void setNotebook(Notebook notebook) {
		this.notebook = notebook;
	}
	
	/** Custom methods */
	public String getCreatedAt() {
		return androidCreated;
	}
	
	public String getUpdatedAt() {
		return androidUpdated;
	}
	
	
	
	public ArrayList<Bitmap> getAttachedImages() {
		return attachedImages;
	}
	
	public void setAttachedImages(ArrayList<Bitmap> images){
		this.attachedImages = images;
	}

	public void attachImage(Bitmap attachedImages) {
		this.attachedImages.add(attachedImages);
	}
	
	public int getListSize(){
		return this.attachedImages.size();
	}
	
	public ArrayList<String> getPaths() {
		return paths;
	}

	public void setPaths(String path) {
		this.paths.add(path);
	}
	
	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}
	
	public static void store(Context context, Note note) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		provider.store(note);
		provider.db().commit();
//		Log.i("Saving notebook", Note.find(context, note).getNotebook().getDescription());
	}
	
	public static Note find(Context context, Note note) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		return (Note) provider.db().queryByExample(note).next();
	}
	
	public static void update(Context context, Note note, String title, String content) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		Note currentNote;
		Date date = new Date(System.currentTimeMillis()); 
		currentNote = (Note) provider.db().queryByExample(note).next();	
		currentNote.setTitle(title);
		currentNote.setContent(content);
		currentNote.setAndroidUpdated(date);
		provider.store(currentNote);
		provider.db().commit();
	}
	
	public static void update(Context context, Note note, String title, String content, Notebook notebook) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		Note currentNote;
		Date date = new Date(System.currentTimeMillis()); 
		currentNote = (Note) provider.db().queryByExample(note).next();	
		currentNote.setTitle(title);
		currentNote.setContent(content);
		currentNote.setNotebook(notebook);
		currentNote.setAndroidUpdated(date);
		provider.store(currentNote);
		provider.db().commit();
	}
	
	public static void update(Context context, Note note, Note updatedNote) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		Date date = new Date(System.currentTimeMillis()); 
		Note currentNote = (Note) provider.db().queryByExample(note).next();	
		currentNote.setTitle(updatedNote.getTitle());
		currentNote.setContent(updatedNote.getContent());
		currentNote.setNotebook(updatedNote.getNotebook());
		currentNote.setAndroidUpdated(date);
		currentNote.setRailsCreated(updatedNote.getRailsCreated());
		currentNote.setRailsUpdated(updatedNote.getRailsUpdated());
		currentNote.setUploaded(updatedNote.isUploaded());
		currentNote.setPaths(updatedNote.getPaths());
		provider.store(currentNote);
		provider.db().commit();
	}
	
	public static void delete(Context context, Note note) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		provider.delete(note);
		provider.db().commit();
	}
	
	public static List<Note> query(Context context, Note predicate) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		final Context finalContext = context;
		final Note parameter = predicate;
		
		Predicate<Note> constraint = new Predicate<Note> () {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(Note note) {

				if(!parameter.getNotebook().equals(null)) {
  				if(note.getNotebook().equals(Notebook.find(finalContext, parameter.getNotebook())))
  					if(note.getTitle().toLowerCase().contains(parameter.getTitle().toLowerCase()))
    					return true;
				} 
				if(parameter.getNotebook().equals(null)) {
					if(note.getTitle().toLowerCase().contains(parameter.getTitle().toLowerCase()))
  					return true;
				}
  			
  			return false;
			}
		};
		
		NotesSorter notesSorter = new NotesSorter();
		
		List<Note> results = provider.db().query(constraint, notesSorter);
		return results;
	}
	
	public static List<Note> all(Context context) {
  	PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();	
		List<Note> result = provider.findAll();
		return  result;
	}
	
	public static List<Note> query(Context context, String predicate) {
		PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = plug.getNotesProvider();
		
		final String finalPredicate = predicate.toLowerCase();
		
		Predicate<Note> constraint = new Predicate<Note> () {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(Note note) {
				
        if(note.getTitle().toLowerCase().contains(finalPredicate))
					return true;
  			
  			return false;
			}
		};
		
		NotesSorter notesSorter = new NotesSorter();
		
		List<Note> results = provider.db().query(constraint, notesSorter);
		return results;
	}
	
	public static void upload(Context context, Note note) throws ClientProtocolException, IOException {
		
		Log.i("NOTE UPLOADER: ", "Starting to upload...");
		
		MultipartEntity entity = new MultipartEntity();
		HttpResponse response;
		Note updatedNote = new Note();
		Response container = new Response();
		Gson gson = new Gson();
    StringBody title = new StringBody(note.getTitle());
    StringBody content = new StringBody(note.getContent());
    StringBody user = new StringBody(""+note.getUserId());
    StringBody format = new StringBody("json");
    
		Log.i("NOTE UPLOADER: ", "Finished initialization...");
		
		Notebook currentNotebook = Notebook.find(context, note.getNotebook());

    StringBody notebook = new StringBody(""+currentNotebook.getId());
    entity.addPart("notebook_id", notebook);
		Log.i("Notebook desc.: ", currentNotebook.getDescription());
		Log.i("Notebook ID: ", ""+currentNotebook.getId());
		Log.i("NOTE UPLOADER: ", "NOTEBOOK HAS BEEN UPLOADED...");

		
 		Log.i("NOTE UPLOADER: ", "Printing entities...");	   
 		
		entity.addPart("title", title);
		entity.addPart("content", content);
		entity.addPart("user_id", user);
		entity.addPart("format", format);
		
		Log.i("NOTE UPLOADER: ", "Added entities...");	
		Log.i("NOTE UPLOADER: ", "Requesting POST...");		
		response = WebService.postRequest(entity, WebService.POST_NOTE_URL);
		String json = EntityUtils.toString(response.getEntity());
		Log.i("Note Uploader", ""+json);
		
		container = gson.fromJson(json, Response.class);
		
		updatedNote = container.note;
		
		if(updatedNote != null) {
			updatedNote.setUploaded(true);
			updatedNote.setNotebook(Notebook.find(context,note.getNotebook()));
			Note.update(context, Note.find(context, note), updatedNote);
		}
	}
}
