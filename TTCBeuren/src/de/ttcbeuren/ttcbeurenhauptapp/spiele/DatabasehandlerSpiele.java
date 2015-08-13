package de.ttcbeuren.ttcbeurenhauptapp.spiele;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.Benutzer;

/**
 * Hier wird die SqlLite Datenbank konfiguriert. Diese wird dann erstellt. Hier
 * sind auch alle Methoden vermerkt die für die Datenbankzugriffe notwendig
 * sind. Hierbei werden jeweils Sql zugriffe vollzogen. Hier werden die Daten
 * von dem angemeldeten User gespeichert. Damit es dann für weitere Zwecke
 * verwendet kann.
 * 
 * @author philipp
 */

public class DatabasehandlerSpiele extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Spiele.db";

	// Contacts table name
	private static final String TABLE_Spiele = "Spiele";

	// Contacts Table Columns names
	private static final String SPIELE_ID = "id";
	private static final String SPIELE_punkteHeim = "punkteHeim";
	private static final String SPIELE_punkteGast = "punkteGast";
	private static final String SPIELE_spielsystem = "spielsystem";
	private static final String SPIELE_mannschaftsart = "mannschaftsart";
	private static final String SPIELE_heimverein = "heimverein";
	private static final String SPIELE_heimvereinsnummer = "heimvereinsnummer";
	private static final String SPIELE_gastverein = "gastverein";
	private static final String SPIELE_gastvereinsnummer = "gastvereinsnummer";
	private static final String SPIELE_status = "status";
	private static final String SPIELE_spielbegin = "spielbegin";
	private static final String SPIELE_spielende = "spielende";
	private static final String SPIELE_istspielbeendet = "istspielbeendet";
	/**
	 * Ist im String Beginn intigriert
	 */
	private static final String SPIELE_spieldatum = "spieldatum";

	public DatabasehandlerSpiele(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Mit dieser Methode wird die Sql-lite Datenbank gebaut bzw. die Tabelle.
	 * Hierbei wird ein Sql-Befehl erzeugt und dieser dann ausgeführt.(execSQL)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Spiele + "("
				+ SPIELE_ID + " INTEGER PRIMARY KEY," + SPIELE_punkteHeim
				+ " INTEGER, " + SPIELE_punkteGast + " INTEGER, "
				+ SPIELE_spielsystem + " TEXT, " + SPIELE_mannschaftsart
				+ " TEXT, " + SPIELE_heimverein + " TEXT, "
				+ SPIELE_heimvereinsnummer + " TEXT, " + SPIELE_gastverein
				+ " TEXT, " + SPIELE_gastvereinsnummer + " TEXT, "
				+ SPIELE_status + " TEXT, " + SPIELE_spielbegin + " TEXT, "
				+ SPIELE_spielende + " TEXT, " + SPIELE_istspielbeendet
				+ " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Spiele);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Hier wird das Mitglied in der Datenbank eingspeichert.
	 * 
	 * @param mitglied
	 */
	public void addSpiel(Spiel spiel) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(SPIELE_ID, spiel.getSpiel_id());
		values.put(SPIELE_punkteHeim, "" + spiel.getPunkteHeim()); // Ist INT
		values.put(SPIELE_punkteGast, "" + spiel.getPunkteGast()); // Ist INT
		values.put(SPIELE_spielsystem, spiel.getSpielsystem());
		values.put(SPIELE_mannschaftsart, spiel.getMannschaftsart());
		values.put(SPIELE_heimverein, spiel.getHeimverein());
		values.put(SPIELE_heimvereinsnummer, spiel.getHeimvereinsnummer());
		values.put(SPIELE_gastverein, spiel.getGastverein());
		values.put(SPIELE_gastvereinsnummer, spiel.getGastvereinsnummer());
		values.put(SPIELE_status, spiel.getStatus());
		values.put(SPIELE_spielbegin, spiel.getSpielbegindatumtime());
		values.put(SPIELE_spielende, spiel.getSpielende());
		values.put(SPIELE_istspielbeendet, "" + spiel.getIstspielbeendet());// Ist
																			// INT
		Log.d("Date",
				"Register Response: " );
		// Insert in Datenbank
		db.insert(TABLE_Spiele, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Hier wird das Mitglied mit hilfe einer SQL-Abfrage aus der Datenbank
	 * geholt und das objekt wird zurückggeben.
	 * 
	 * @return mitglied
	 */
	public Spiel getSpiel(int id) {
		// Select All Query
		SQLiteDatabase db = this.getReadableDatabase();
		Spiel uebergabeSpiel = new Spiel();
		Cursor cursor = db.query(TABLE_Spiele, new String[] { SPIELE_ID,
				SPIELE_punkteHeim, SPIELE_punkteGast, SPIELE_spielsystem,
				SPIELE_mannschaftsart, SPIELE_heimverein,
				SPIELE_heimvereinsnummer, SPIELE_gastverein,
				SPIELE_gastvereinsnummer, SPIELE_status, SPIELE_spielbegin,
				SPIELE_spielende, SPIELE_istspielbeendet }, SPIELE_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		uebergabeSpiel = new Spiel(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor
						.getString(2)), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6),
				cursor.getString(7), cursor.getString(8), cursor.getString(9),
				cursor.getString(10), Integer.parseInt(cursor.getString(12)));

		db.close(); // Closing database connection
		return uebergabeSpiel;

	}

	/**
	 * Bei dieser Methode wird mit einer SQL-Abfrage alle Veranstaltungseinträge
	 * geholt. Diese werden in einer List<> zurückgegeben.
	 * 
	 * @return
	 */

	public List<Spiel> getAllVeranstaltungen() {
		List<Spiel> spielliste = new ArrayList<Spiel>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_Spiele;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Spiel uebergabeSpiel = new Spiel(Integer.parseInt(cursor
						.getString(0)), Integer.parseInt(cursor.getString(1)),
						Integer.parseInt(cursor.getString(2)),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						Integer.parseInt(cursor.getString(12)));
				// Adding contact to list
				spielliste.add(uebergabeSpiel);
			} while (cursor.moveToNext());
		}
		db.close(); // Closing database connection
		// return contact list
		return spielliste;
	}

	/**
	 * Hier wird ein Mitglied mit hilfe einer SQL-Abfrage gelöscht. Der
	 * Eintrag/Mitglied wird mit der ID-Referenziert.
	 * 
	 * @param benutzer
	 */
	public void deleteMitglied(Benutzer benutzer) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Spiele, SPIELE_ID + " = ?",
				new String[] { String.valueOf(benutzer.get_id()) });
		db.close();
	}

	/**
	 * Mit dieser Methode werden alle Einträger der Datenbank gelöscht.
	 */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_Spiele, null, null);
		db.close();

	}
}
