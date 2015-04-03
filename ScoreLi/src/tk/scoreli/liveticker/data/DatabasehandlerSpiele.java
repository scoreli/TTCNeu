package tk.scoreli.liveticker.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		String CREATE_Veranstaltung_TABLE = "CREATE TABLE "
				+ TABLE_Veranstalungen + "(" + Veranstaltung_ID
				+ " INTEGER PRIMARY KEY," + Veranstaltung_Sportart + " TEXT,"
				+ Veranstaltung_Heimmanschaft + " TEXT, "
				+ Veranstaltung_Gastmanschaft + " TEXT, "
				+ Veranstaltung_SpielstandHeim + " INTEGER, "
				+ Veranstaltung_SpielstandGast + " INTEGER, "
				+ Veranstaltung_Spielbeginn + " TEXT" + ")";
		db.execSQL(CREATE_Veranstaltung_TABLE);
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

	// Getting single Veranstaltung
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

		Veranstaltung veranstaltung = new Veranstaltung(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getString(2),
				cursor.getString(3), Integer.parseInt(cursor.getString(4)),
				Integer.parseInt(cursor.getString(5)), cursor.getString(6));

		// return contact
		return veranstaltung;
	}

	/*
	 * HIer weitermachen für Veranstaltung
	 */

	// Getting All Contacts
	public List<Veranstaltung> getAllVeranstaltungen() {
		List<Veranstaltung> veranstaltungliste = new ArrayList<Veranstaltung>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_Veranstalungen;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Veranstaltung veranstaltung = new Veranstaltung(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), Integer.parseInt(cursor
								.getString(4)), Integer.parseInt(cursor
								.getString(5)), cursor.getString(6));

				// Adding contact to list
				veranstaltungliste.add(veranstaltung);
			} while (cursor.moveToNext());
		}

		// return contact list
		return veranstaltungliste;
	}

	// Updating single contact
	public int updateVeranstaltung(Veranstaltung veranstaltung) {
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

		// updating row
		return db.update(TABLE_Veranstalungen, values, Veranstaltung_ID
				+ " = ?",
				new String[] { String.valueOf(veranstaltung.getId()) });
	}

	// Deleting single contact
	public void deleteVeranstaltung(Veranstaltung veranstaltung) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Veranstalungen, Veranstaltung_ID + " = ?",
				new String[] { String.valueOf(veranstaltung.getId()) });
		db.close();
	}

	// Getting contacts Count
	public int getVeranstaltungCount() {
		String countQuery = "SELECT  * FROM " + TABLE_Veranstalungen;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}