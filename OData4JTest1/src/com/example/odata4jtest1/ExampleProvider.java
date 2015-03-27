package com.example.odata4jtest1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/*
 * This is the ExampleProvider class. It is a custom ContentProvider.
 */

public class ExampleProvider extends ContentProvider {
    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher;// this takes the uri's and adds cases
    private LocalDataHelper dbHelper = null;
    private static Cursor cursor = null;

    static{
    	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //there are no matches
    	/*
    	 * Shows all Movies
    	 */
    	sUriMatcher.addURI("com.example.test1.provider", "Movies", 1); //there is one match (case 1)

    	/*
    	 * Shows Movies with ID #
    	 */
    	sUriMatcher.addURI("com.example.test1.provider", "Movies/#", 2); //there are two matches (case 2)
    }

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		LocalDataHelper dataHelper = ((ODataApplicationContext)this.getContext().getApplicationContext()).getLocalDataHelper();
		cursor = dataHelper.query(uri, projection, selection, selectionArgs, sortOrder);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
    
    
}