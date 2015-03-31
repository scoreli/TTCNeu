package tk.scoreli.liveticker.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databasehandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Mitglieder.db";

	// Contacts table name
	private static final String TABLE_MITGLIEDER = "mitglieder";

	// Contacts Table Columns names
	private static final String MITGLIEDER_ID = "id";
	private static final String MITGLIEDER_EMAIL = "name";
	private static final String MITGLIEDER_PASSWORT = "passwort";

	public Databasehandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MITGLIEDER + "("
				+ MITGLIEDER_ID + " INTEGER PRIMARY KEY," + MITGLIEDER_EMAIL
				+ " TEXT," + MITGLIEDER_PASSWORT + " TEXT" + ")";
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
		values.put(MITGLIEDER_EMAIL, mitglied.getEmail()); // Contact Name
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort()); // Contact
																	// Phone

		// Inserting Row
		db.insert(TABLE_MITGLIEDER, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public Mitglied getMitglied(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MITGLIEDER, new String[] {
				MITGLIEDER_ID, MITGLIEDER_EMAIL, MITGLIEDER_PASSWORT },
				MITGLIEDER_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Mitglied mitglied = new Mitglied(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return mitglied;
	}
/*
 * get Mitglied ist noch nicht ganz gut ... schmeist eine exeption
 * 
 */
	public String getMitglied(String email){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MITGLIEDER, null,
				MITGLIEDER_EMAIL + "=?", new String[] { email},
				null, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex(MITGLIEDER_PASSWORT));
        return password;
		
		
	}
	// Getting All Contacts
	public List<Mitglied> getAllMitglieder() {
		List<Mitglied> mitgliederListe = new ArrayList<Mitglied>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MITGLIEDER;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Mitglied mitglied = new Mitglied();
				mitglied.setID(Integer.parseInt(cursor.getString(0)));
				mitglied.setEmail(cursor.getString(1));
				mitglied.setPasswort(cursor.getString(2));
				// Adding contact to list
				mitgliederListe.add(mitglied);
			} while (cursor.moveToNext());
		}

		// return contact list
		return mitgliederListe;
	}

	// Updating single contact
	public int updateMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITGLIEDER_EMAIL, mitglied.getEmail());
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort());

		// updating row
		return db.update(TABLE_MITGLIEDER, values, MITGLIEDER_ID + " = ?",
				new String[] { String.valueOf(mitglied.getID()) });
	}

	// Deleting single contact
	public void deleteMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MITGLIEDER, MITGLIEDER_ID + " = ?",
				new String[] { String.valueOf(mitglied.getID()) });
		db.close();
	}

	// Getting contacts Count
	public int getMitgliederCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MITGLIEDER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}