package tk.scoreli.liveticker.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabasehandlerUUID extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UUID.db";

	// Contacts table name
	private static final String TABLE_MITGLIEDER = "UUID";

	// Contacts Table Columns names
	private static final String MITGLIEDER_ID = "id";
	private static final String MITGLIEDER_EMAIL = "UUID";

	public DatabasehandlerUUID(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MITGLIEDER + "("
				+ MITGLIEDER_ID + " INTEGER PRIMARY KEY," + MITGLIEDER_EMAIL
				+ " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MITGLIEDER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITGLIEDER_EMAIL, mitglied.getUuid()); // UUID

		// Inserting Row
		db.insert(TABLE_MITGLIEDER, null, values);
		db.close(); // Closing database connection
	}

	public Mitglied getMitglied() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MITGLIEDER;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		Mitglied mitglied = new Mitglied();
		mitglied.set_id(Integer.parseInt(cursor.getString(0)));
		mitglied.setUuid(cursor.getString(1));
		return mitglied;

	}

	// Deleting single contact
	public void deleteMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MITGLIEDER, MITGLIEDER_ID + " = ?",
				new String[] { String.valueOf(mitglied.get_id()) });
		db.close();
	}
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_MITGLIEDER, null, null);
		db.close();

		
	}
}
