/*
package tk.scoreli.liveticker.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Datenbankaufruftest {
	private SQLiteDatabase database;
	private Datenbanktest dbhelper;
	private static int mitgliedid =0;
	private String[] allCollums = { "_id", "email", "passwort" };

	public Datenbankaufruftest(Context context) {
		dbhelper = new Datenbanktest(context);
	}

	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}

	public Mitgliedtest createMitgliedertest(String email, String passwort) {
				
			Cursor cursor;
			String mitgliedIDString= ""+mitgliedid+"";
			ContentValues values = new ContentValues();
			values.put(mitgliedIDString,true);
			values.put("email", email);
			values.put("passwort", passwort);
			long insertId = database.insert("mitglieder", null, values);

			cursor = database.query("mitglieder", allCollums, "_id = "
					+ insertId, null, null, null, null);
			cursor.moveToFirst();
			mitgliedid++;
		
		return cursorToMitglieder(cursor);
		}		


	protected List<Mitgliedtest> getAllmitglieder() {
		List<Mitgliedtest> mitgliedereintraege = new ArrayList<Mitgliedtest>();
		mitgliedereintraege = new ArrayList<Mitgliedtest>();
		Cursor cursor = database.query("mitglieder", allCollums, null, null,
				null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() == 0)
			return mitgliedereintraege;

		while (cursor.isAfterLast() == false) {
			Mitgliedtest mitgliedeintrag = cursorToMitglieder(cursor);
			mitgliedereintraege.add(mitgliedeintrag);
			cursor.moveToNext();
		}
		cursor.close();

		return mitgliedereintraege;
	}
	
	private Mitgliedtest cursorToMitglieder(Cursor cursor) {
		Mitgliedtest mitglied = new Mitgliedtest();
		mitglied.setId(cursor.getLong(0));
		mitglied.setEmail(cursor.getString(1));
		mitglied.setPasswort(cursor.getString(2));
		return mitglied;
	}

}
*/