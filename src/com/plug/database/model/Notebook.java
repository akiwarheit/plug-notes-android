package com.plug.database.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.plug.PlugApplication;
import com.plug.database.provider.NotebooksProvider;
import com.plug.database.provider.NotesProvider;
import com.plug.utils.NotebooksSorter;
import com.plug.web.Response;
import com.plug.web.WebService;

public class Notebook {
	
	public static String DESCRIPTION = "description";
	
	@SerializedName("id") 												private long id;
	@SerializedName("user_id") 										private long userId;
	@SerializedName("description")								private String description;
	@SerializedName("created_at") 								private String railsCreated;
	@SerializedName("updated_at")									private String railsUpdated;	
	@SerializedName("created_android") 						private String androidCreated;
	@SerializedName("updated_android")						private String androidUpdated;	
	private List<Note> notes;
	private boolean isUploaded = false;

	public Notebook() {
  	Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();	
	}
	
	public Notebook(String description) {
		this.description = description;
		Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
		notes = new ArrayList<Note>();
	}
	
	public Notebook(String description, User user) {
		this.description = description;
		Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
		notes = new ArrayList<Note>();
		
		this.userId = user.getId();
	}
	
	public Notebook(String description, List<Note> notes) {
		this.description = description;
		Date date = new Date(System.currentTimeMillis()); 
	  this.androidCreated = date.toString();
	  this.androidUpdated = date.toString();
		this.notes = notes;
	}
	
	public long getUserId() {
  	return userId;
  }

	public void setUserId(long userId) {
  	this.userId = userId;
  }

	public void setAndroidCreated(String androidCreated) {
  	this.androidCreated = androidCreated;
  }

	public void setAndroidUpdated(String androidUpdated) {
  	this.androidUpdated = androidUpdated;
  }

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Note> getNotes() {
		return notes;
	}
	
	public Date getAndroidUpdated() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd k:m:s");
		Date date = null;
		try {
	    date = dateFormat.parse(androidUpdated);
    } catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    }
    if(date!=null)
      Log.i("Date Parser", date.toString());
    else
    	Log.e("Date Parser", "NULL");
  	return date;
  }

	public void setAndroidUpdated(Date androidUpdated) {
  	this.androidUpdated = androidUpdated.toString();
  }

	public void setRailsCreated(String railsCreated) {
  	this.railsCreated = railsCreated;
  }

	public void setRailsUpdated(String railsUpdated) {
  	this.railsUpdated = railsUpdated;
  }

	public void setAndroidCreated(Date androidCreated) {
  	this.androidCreated = androidCreated.toString();
  }

	public String getRailsCreated() {
  	return railsCreated;
  }

	public String getRailsUpdated() {
  	return railsUpdated;
  }

	public Date getAndroidCreated() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd k:m:s");
		Date date = null;
		try {
	    date = dateFormat.parse(androidCreated);
    } catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    }
  	return date;
  }

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	public boolean isUploaded() {
  	return isUploaded;
  }

	public void setUploaded(boolean isUploaded) {
  	this.isUploaded = isUploaded;
  }

	public long getId() {
  	return id;
  }

	public void setId(long id) {
  	this.id = id;
  }

	public List<Note> loadNotes(Context context) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotesProvider provider = application.getNotesProvider();
		Note predicate = new Note();
		predicate.setNotebook(this);
		List<Note> queriedNotes = provider.db().queryByExample(predicate);
		this.notes = queriedNotes;
		
		return notes;
	}
	
	public static void save(Context context, Notebook notebook) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = application.getNotebookProvider();
		provider.store(notebook);
		provider.db().commit();
	}
	
	public static Notebook find(Context context, Notebook notebook) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = application.getNotebookProvider();
		return (Notebook) provider.db().queryByExample(notebook).next();
	}
	
	public static void delete(Context context, Notebook notebook) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = application.getNotebookProvider();
		provider.delete(notebook);
		provider.db().commit();
	}
	
	public static List<Notebook> query(Context context, Notebook predicate) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = application.getNotebookProvider();
		final Notebook parameter = predicate;
		
		Predicate<Notebook> constraint = new Predicate<Notebook>() {
      private static final long serialVersionUID = 1L;
			@Override
			public boolean match(Notebook notebook) {
				return notebook.getDescription().toLowerCase().contains(parameter.getDescription().toLowerCase());
			}
		};
		
		NotebooksSorter sort = new NotebooksSorter();
		sort.sortByAscending(true);
		
		
//		Query query = provider.db().query();
//		query.descend(Notebook.DESCRIPTION).orderAscending();
//		query.constrain(constraint);
		List<Notebook> results = provider.db().query(constraint, sort);
//		List<Notebook> results = provider.db().query(new Predicate<Notebook>() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public boolean match(Notebook notebook) {
//				return notebook.getDescription().toLowerCase().contains(parameter.getDescription().toLowerCase());
//			}
//		}); 
		return results;
	}
	
	public static List<Notebook> query(Context context, String parameter) {
		PlugApplication application = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = application.getNotebookProvider();
		
		final String lowerCaseParameter = parameter.toLowerCase();
		
		Predicate<Notebook> predicate = new Predicate<Notebook>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(Notebook notebook) {
				return notebook.getDescription().toLowerCase().contains(lowerCaseParameter);
			}
			
		};
		
		
		Query query = provider.db().query();
		query.descend(Notebook.DESCRIPTION).orderAscending();
		query.constrain(predicate);
		List<Notebook> results = query.execute();
//		.query(new Predicate<Notebook>() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public boolean match(Notebook notebook) {
//				return notebook.getDescription().toLowerCase().contains(lowerCaseParameter);
//			}
//			
//		});
		
//		List<Notebook> results = provider.db().query(new Predicate<Notebook>() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public boolean match(Notebook notebook) {
//				return notebook.getDescription().toLowerCase().contains(lowerCaseParameter);
//			}
//			
//		}); 
		return results;
	}
	
	public static List<Notebook> all(Context context) {
  	PlugApplication plug = (PlugApplication) context.getApplicationContext();
		NotebooksProvider provider = plug.getNotebookProvider();	
		List<Notebook> result = provider.findAll();
		return  result;
	}
	
	public static void update(Context context, Notebook notebook, Notebook updatedNotebook) {
		Notebook currentNotebook = Notebook.find(context, notebook);
		Date date = new Date(System.currentTimeMillis()); 
		currentNotebook.setId(updatedNotebook.getId());
		currentNotebook.setUserId(updatedNotebook.getUserId());
		currentNotebook.setDescription(updatedNotebook.getDescription());
		currentNotebook.setAndroidUpdated(date);
		currentNotebook.setRailsCreated(updatedNotebook.getRailsCreated());
		currentNotebook.setRailsUpdated(updatedNotebook.getRailsUpdated());
		currentNotebook.setUploaded(updatedNotebook.isUploaded);
		Notebook.save(context, currentNotebook);
	}
	
	public static void upload(Context context, Notebook notebook) throws ClientProtocolException, IOException {
//		notebook.getAndroidCreated();
		MultipartEntity entity = new MultipartEntity();
		HttpResponse response;
		Notebook updatedNotebook = new Notebook();
		Response container = new Response();
		Gson gson = new Gson();
    StringBody description = new StringBody(notebook.getDescription());
    StringBody user = new StringBody(""+notebook.getUserId());
    StringBody format = new StringBody("json");
//	    StringBody androidCreated = new StringBody(notebook.getAndroidCreated().toString());
//	    StringBody androidUpdated = new StringBody(notebook.getAndroidUpdated().toString());
		entity.addPart("description", description);
		entity.addPart("user_id", user);
		entity.addPart("format", format);
//  		entity.addPart("android_created", androidCreated);
//  		entity.addPart("android_updated", androidUpdated);
		response = WebService.postRequest(entity, WebService.POST_NOTEBOOK_URL);
		String json = EntityUtils.toString(response.getEntity());
		Log.i("Note Uploader", json);
		container = gson.fromJson(json, Response.class);
		updatedNotebook = container.notebook;
		if(updatedNotebook != null) {
			Log.i("Notebook ID", ""+updatedNotebook.getId());
			Log.i("Notebook userID", ""+updatedNotebook.getUserId());
			Log.i("Notebook Title", ""+updatedNotebook.getDescription());
			Log.i("Notebook CreatedAt", ""+updatedNotebook.getRailsCreated());
			Log.i("Notebook UpdatedAt", ""+updatedNotebook.getRailsUpdated());
			updatedNotebook.setUploaded(true);
			Notebook.update(context, Notebook.find(context, notebook), updatedNotebook);
		}
	}

}
