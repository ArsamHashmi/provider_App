package com.axiomindustries.provider002;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.axiomindustries.provider002.database.ContractNotification;
import com.axiomindustries.provider002.database.DatabaseHelper;

public class MyProvider extends ContentProvider {

	static final String PROVIDER_NAME = "com.axiomindustries.provider002.Notification";
	static final String URL = "content://" + PROVIDER_NAME + "/cte";

	static final int uriCode = 1;
	static final UriMatcher uriMatcher;
	public static final Uri CONTENT_URI = Uri.parse(URL);
	private static HashMap<String, String> values;
	private SQLiteDatabase db;

	static {
		  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		  uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
		  uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);
	  }

	@Override
	public boolean onCreate() {
		 Context context = getContext();
		  DatabaseHelper dbHelper = new DatabaseHelper(context);
		  db = dbHelper.getWritableDatabase();
		  return db != null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		  qb.setTables(ContractNotification.TABLE_NAME);

		if (uriMatcher.match(uri) == uriCode) {
			qb.setProjectionMap(values);
		} else {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		  if (sortOrder == null || sortOrder.equals("")) {
		   sortOrder = ContractNotification.COLUMN_NOTIFICATION_TIME;
		  }
		  Cursor c = qb.query(db, projection, selection, selectionArgs, null,
		    null, sortOrder);
		  c.setNotificationUri(getContext().getContentResolver(), uri);
		  return c;
	}

	@Override
	public String getType(Uri uri) {
		if (uriMatcher.match(uri) == uriCode)
			return "vnd.android.cursor.dir/cte";
		throw new IllegalArgumentException("Unsupported URI: " + uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		 long rowID = db.insert(ContractNotification.TABLE_NAME, "", values);
		  if (rowID > 0) {
		   Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
		   getContext().getContentResolver().notifyChange(_uri, null);
		   return _uri;
		  }
		  throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count;
		if (uriMatcher.match(uri) == uriCode) {
			count = db.delete(ContractNotification.TABLE_NAME, selection, selectionArgs);
		} else {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		  getContext().getContentResolver().notifyChange(uri, null);
		  return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	 	int count;
		if (uriMatcher.match(uri) == uriCode) {
			count = db.update(ContractNotification.TABLE_NAME, values, selection, selectionArgs);
		}
		else
			throw new IllegalArgumentException("Unknown URI " + uri);

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
