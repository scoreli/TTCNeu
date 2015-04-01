package tk.scoreli.liveticker.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabasehandlerSpiele extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Spiele.db";

	// Contacts table name
	private static final String TABLE_Veranstalungen = "veranstaltungen";

	// Contacts Table Columns names
	private static final String Veranstaltung_ID = "id";
	private static final String Veranstaltung_Sportart = "Sportart";
	private static final String Veranstaltung_Heimmanschaft = "Heimmanschaft";
	private static final String Veranstaltung_Gastmanschaft = "Gastmannschaft";
	private static final String Veranstaltung_SpielstandHeim = "SpielstandHeim";
	private static final String Veranstaltung_SpielstandGast = "SpielstandGast";
	private static final String Veranstaltung_Spielbeginn = "Spielbeginn";

	public DatabasehandlerSpiele(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Veranstalungen
				+ "(" + Veranstaltung_ID + " INTEGER PRIMARY KEY,"
				+ Veranstaltung_Sportart + " TEXT,"
				+ Veranstaltung_Heimmanschaft + " TEXT, "
				+ Veranstaltung_Gastmanschaft + " TEXT, "
				+ Veranstaltung_SpielstandHeim + " INTEGER, "
				+ Veranstaltung_SpielstandGast + " INTEGER, "
				+ Veranstaltung_Spielbeginn + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Veranstalungen);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addVeranstaltung(Veranstaltung veranstaltung) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Veranstaltung_Sportart, veranstaltung.getSportart()); // Sportart
		values.put(Veranstaltung_Heimmanschaft,
				veranstaltung.getHeimmanschaft()); // Heimmannschaft
		values.put(Veranstaltung_Gastmanschaft,
				veranstaltung.getGastmannschaft()); // Gastmannschaft
		values.put(Veranstaltung_SpielstandHeim,
				veranstaltung.getSpielstandHeim()); // SpielstandHeim
		values.put(Veranstaltung_SpielstandGast,
				veranstaltung.getSpielstandGast()); // SpielstandGast
		values.put(Veranstaltung_Spielbeginn, veranstaltung.getSpielbeginn());// Spielbeginn

		// Inserting Row
		db.insert(TABLE_Veranstalungen, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public Veranstaltung getVeranstaltung(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_Veranstalungen, new String[] {
				Veranstaltung_Heimmanschaft, Veranstaltung_Gastmanschaft,
				Veranstaltung_Spielbeginn, Veranstaltung_Sportart,
				Veranstaltung_ID, Veranstaltung_SpielstandHeim,
				Veranstaltung_SpielstandGast, }, Veranstaltung_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Veranstaltung veranstaltung = new Veranstaltung(cursor.getString(0),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor
						.getString(5)), Integer.parseInt(cursor.getString(6)));

		// return contact
		return veranstaltung;
	}
/*
 * HIer weitermachen für Veranstaltung
 * 
 */
	/*
	 * get Mitglied ist noch nicht ganz gut ... schmeist eine exeption
	 */
	public String getMitglied(String email) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_Veranstalungen, null,
				Veranstaltung_Sportart + "=?", new String[] { email }, null,
				null, null, null);
		if (cursor.getCount() < 1) // UserName Not Exist
			return "NOT EXIST";
		cursor.moveToFirst();
		String password = cursor.getString(cursor
				.getColumnIndex(Veranstaltung_Heimmanschaft));
		return password;

	}

	// Getting All Contacts
	public List<Mitglied> getAllMitglieder() {
		List<Mitglied> mitgliederListe = new ArrayList<Mitglied>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_Veranstalungen;

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
		values.put(Veranstaltung_Sportart, mitglied.getEmail());
		values.put(Veranstaltung_Heimmanschaft, mitglied.getPasswort());

		// updating row
		return db.update(TABLE_Veranstalungen, values, Veranstaltung_ID
				+ " = ?", new String[] { String.valueOf(mitglied.getID()) });
	}

	// Deleting single contact
	public void deleteMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Veranstalungen, Veranstaltung_ID + " = ?",
				new String[] { String.valueOf(mitglied.getID()) });
		db.close();
	}

	// Getting contacts Count
	public int getMitgliederCount() {
		String countQuery = "SELECT  * FROM " + TABLE_Veranstalungen;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}