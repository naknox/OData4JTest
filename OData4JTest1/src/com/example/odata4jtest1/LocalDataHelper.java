package com.example.odata4jtest1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;


public class LocalDataHelper {
	private DatabaseHelper _dbHelper = null;
	private Map<String, UriMatcher> _uriMatchers = new HashMap<String,UriMatcher>();
	//private Dao<Movie, Integer> _movieDao; //this should use dbHelper's movieDao!
	private Context _appContext;
	
	public LocalDataHelper(Context appContext){
		this._appContext = appContext; //gets the appcontext to start the dbHelper
		
		_dbHelper = getHelper();
		
		UriMatcher movieMatcher = new UriMatcher(UriMatcher.NO_MATCH); //there are no matches
    	/*
    	 * Shows all Movies
    	 */
		movieMatcher.addURI("com.example.test1.provider", "Movies", 1); //jk there is one match (case 1)

    	/*
    	 * Shows Movies with ID #
    	 */
		movieMatcher.addURI("com.example.test1.provider", "Movies/#", 2); //jk there are two matches (case 2)
		
		_uriMatchers.put("Movies", movieMatcher);
	}
	
	
	
	
	
	
	
	//move methods from content provider into this class
	//want a query method that goes and queries the remote db, syncs the remote with the local db, and then queries the local (in here)
	
	
	// Implements ContentProvider.query() 
    public Cursor query(Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder) { //we're setting up the query here

    	List<Movie> local =  getMovieListFromCursor(queryFromLocalDB(uri, projection, selection, selectionArgs, sortOrder));
    	List<Movie> remote = getMovieListFromCursor(queryFromRemoteDB(uri, projection, selection, selectionArgs, sortOrder));
    	
    	return getCursorForMovieList(MergeRemoteAndLocal(local, remote));
    	//return this.queryFromLocalDB(uri, projection, selection, selectionArgs, sortOrder);
    
    }
    
    private List<Movie> MergeRemoteAndLocal(List<Movie> localMovies, List<Movie> remoteMovies){
    	List<Movie> retMovies = new ArrayList<Movie>();
    	
    	for(Movie m : remoteMovies){
    		retMovies.add(m);
    	}
    	
    	for(Movie m : localMovies){
    		boolean foundById = false;
    		for(Movie tmp : retMovies){
        		if(tmp.id == m.id){
        			foundById = true;
        			break;
        		}
        	}
    		
    		if(!foundById) retMovies.add(m);
    	}
    	
    	return retMovies;
    }

    private Cursor getCursorForMovieList(List<Movie> movies){
    	String[] columnNames = {"MovieId", "MovieName"};
    	MatrixCursor cursor = new MatrixCursor(columnNames);
    	
    	for(Movie m : movies){
    		cursor.addRow(new Object[]{m.id, m.name});
    	}
    	
    	return cursor;
    }
    
    private List<Movie> getMovieListFromCursor(Cursor cursor){
    	List<Movie> movies = new ArrayList<Movie>();
    	while(cursor.moveToNext()){
    		movies.add(new Movie(cursor));
    	}

    	return movies;
    }
    
    private Cursor queryFromRemoteDB(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder){
    	String serviceUrl = "http://129.15.78.74/WebApplication1/WcfDataService1.svc/"; //this is where the outside DB is
    	
    	List<Movie> movies = new ArrayList<Movie>();
		try{
			ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl); 
			ODataConsumer consumer = builder.build();

			
			//consumer.mergeEntity("Movies", 1).properties(OProperties.string("MovieName", "MyMovie")).execute(); //this is an example of "updating" a specific index
			
			for(OEntity entity : consumer.getEntities("Movies").execute()){ 
				Movie m = new Movie(entity); //make each one into a movie
				movies.add(m);
			}

		}
		catch(Throwable t){
			t.printStackTrace();

		}
		
		return getCursorForMovieList(movies);
    }
    
    private UriMatcher ResolveMatcher(Uri uri){
    	List<String> pathSegments = uri.getPathSegments();
    	UriMatcher matcher = this._uriMatchers.get(pathSegments.get(0));
    	
    	return matcher;
    }
    private Cursor queryFromLocalDB(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) { //we're setting up the query here

        	//LocalDataHelper dataHelper = ((ODataApplicationContext)this.getContext().getApplicationContext()).getLocalDataHelper(); 
        	//dataHelper.query(uri, projection, selection, selectionArgs, sortOrder); //IMPLEMENT THIS
        	
            /*
             * Choose table to query and sort order based on code returned for incoming URI.
             */
        	Cursor cursor = null;
        	Dao<Movie, Integer> movieDao;
        	QueryBuilder<Movie, Integer> qb;
        	//(Assists in building sql query (SELECT) statements for a particular table in a particular database.)
        	CloseableIterator<Movie> iterator;
        	//(Assists in building sql query (SELECT) statements for a particular table in a particular database.)
        	AndroidDatabaseResults results;
        	//(Android implementation of our results object.)
        	
        	UriMatcher matcher = this.ResolveMatcher(uri);
        	try{
    	        switch (matcher.match(uri)) { //depending on how the uri is matched;
    	
    	
    	            // If the incoming URI was for all of Movies
    	            case 1:
    	            	movieDao = getHelper().getMovieDao();
    	            	
    	            	qb = movieDao.queryBuilder();
    	            	
    	            	
    	            	cursor = getCursorForMovieList(movieDao.query(qb.prepare()));
    	                break;
    	
    	            // If the incoming URI was for a single row in Movies
    	            case 2:
    	
    	                /*
    	                 * Because this URI was for a single row, the _ID value part is
    	                 * present. Get the last path segment from the URI; this is the _ID value.
    	                 * Then, append the value to the WHERE clause for the query
    	                 */
    	            	movieDao = getHelper().getMovieDao();
    	            	
    	            	qb = movieDao.queryBuilder();
    	            	
    	            	qb.where().eq("id", Integer.parseInt( uri.getLastPathSegment())); //check this stuff

    	            	cursor = getCursorForMovieList(movieDao.query(qb.prepare()));
    	                break;
    	
    	            default:
    	            	
    	                // If the URI is not recognized, you should do some error handling here.
    	        }
        	}catch(SQLException sqlEx){
        		
        	}
            
            return cursor;
        }
	
  
    private Cursor queryFromLocalDBOld(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) { //we're setting up the query here

        	//LocalDataHelper dataHelper = ((ODataApplicationContext)this.getContext().getApplicationContext()).getLocalDataHelper(); 
        	
            /*
             * Choose table to query and sort order based on code returned for incoming URI.
             */
        	Cursor cursor = null;
        	Dao<Movie, Integer> movieDao;
        	QueryBuilder<Movie, Integer> qb;
        	//(Assists in building sql query (SELECT) statements for a particular table in a particular database.)
        	CloseableIterator<Movie> iterator;
        	//(Assists in building sql query (SELECT) statements for a particular table in a particular database.)
        	AndroidDatabaseResults results;
        	//(Android implementation of our results object.)
        	
        	UriMatcher matcher = this.ResolveMatcher(uri);
        	try{
    	        switch (matcher.match(uri)) { //depending on how the uri is matched;
    	
    	
    	            // If the incoming URI was for all of Movies
    	            case 1:
    	            	movieDao = getHelper().getMovieDao();
    	            	
    	            	qb = movieDao.queryBuilder();
    	            	
    	            	
    	            	PreparedQuery<Movie> pQuery = qb.prepare();
    	            	
    	            	// when you are done, prepare your query and build an iterator
    	            	iterator = movieDao.iterator(pQuery);
    	            	
    	            	   // get the raw results which can be cast under Android
    	            	 results =
    	            	       (AndroidDatabaseResults)iterator.getRawResults();
    	            	   cursor = results.getRawCursor();
    	                break;
    	
    	            // If the incoming URI was for a single row in Movies
    	            case 2:
    	
    	                /*
    	                 * Because this URI was for a single row, the _ID value part is
    	                 * present. Get the last path segment from the URI; this is the _ID value.
    	                 * Then, append the value to the WHERE clause for the query
    	                 */
    	            	movieDao = getHelper().getMovieDao();
    	            	
    	            	qb = movieDao.queryBuilder();
    	            	
    	            	qb.where().eq("id", Integer.parseInt( uri.getLastPathSegment())); //check this stuff
    	            	// when you are done, prepare your query and build an iterator
    	            	iterator = movieDao.iterator(qb.prepare());
    	            	
    	            	   // get the raw results which can be cast under Android
    	            	 results =
    	            	       (AndroidDatabaseResults)iterator.getRawResults();
    	            	   cursor = results.getRawCursor();
    	                break;
    	
    	            default:
    	            	
    	                // If the URI is not recognized, you should do some error handling here.
    	        }
        	}catch(SQLException sqlEx){
        		
        	}
            
            return cursor;
        }
	
	
	
	
	
	
	public DatabaseHelper getHelper() {
		if(_dbHelper == null){
			_dbHelper = DatabaseHelper.getHelper(this._appContext);
		}
		return _dbHelper;
	}
	
	
}
