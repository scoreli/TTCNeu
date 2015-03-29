
 package tk.scoreli.liveticker.data;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "mitglieder.db";
	private final static int DATABASE_VERSION = 1;

	private static final String MITGLIEDER_TABLE = "mitglieder";

	private static final String MITGLIEDER_ID = "_id";
	private static final String MITLGIEDER_EMAIL = "email";
	private static final String MITGLIEDER_PASSWORT = "passwort";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDB = "CREATE TABLE" + MITGLIEDER_ID + "(" + MITGLIEDER_ID
				+ " INTEGER PRIMARY KEY, " + MITLGIEDER_EMAIL + "TEXT,"
				+ MITGLIEDER_PASSWORT + "TEXT)";
		db.execSQL(createDB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + MITGLIEDER_TABLE);
		onCreate(db);
	}

	public Mitgliedtest neuesMitglied(Mitgliedtest mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITLGIEDER_EMAIL, mitglied.getEmail());
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort());

		long id = db.insert(MITGLIEDER_TABLE, null, values);
		mitglied.setId(id);
		this.close();

		return mitglied;
	}

	public Mitgliedtest updateMitglied(Mitgliedtest mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITLGIEDER_EMAIL, mitglied.getEmail());
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort());
		db.update(MITGLIEDER_TABLE, values, MITGLIEDER_ID + "=?",
				new String[] { String.valueOf(mitglied.getId()) });
		this.close();
		return mitglied;
	}
public Mitgliedtest speicherMitglied(Mitgliedtest mitglied){
	if(mitglied.getId()>0){
		updateMitglied(mitglied);
	}
	else {
		neuesMitglied(mitglied);
	}
return mitglied;
}


public List<Mitgliedtest> getAllMitglieder(){
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = null;
	cursor= db.query(MITGLIEDER_TABLE, null, null, null, null, null, null);
	List<Mitgliedtest> mitgliederListe = new ArrayList<Mitgliedtest>();
	cursor.moveToFirst();
	int emailIndex = cursor.getColumnIndex(MITLGIEDER_EMAIL);
	int passwortIndex = cursor.getColumnIndex(MITGLIEDER_PASSWORT);
	int idIndex = cursor.getColumnIndex(MITGLIEDER_ID);
	do {
		String email = cursor.getString(emailIndex);
		String passwort= cursor.getString(passwortIndex);
		long id = cursor.getLong(idIndex);
		Mitgliedtest mitglied = new Mitgliedtest();
		mitglied.setEmail(email);
		mitglied.setPasswort(passwort);
		mitglied.setId(id);
		mitgliederListe.add(mitglied);
		
	} while (cursor.moveToNext());
	cursor.close();
	this.close();
	
	return mitgliederListe;
}

public Mitgliedtest getNotizById(long id) {
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = null;
	cursor = db.query(MITGLIEDER_TABLE, null, MITGLIEDER_ID + "=?",
			new String[] { String.valueOf(id) }, null, null, null);
	cursor.moveToFirst();
	int idIndex = cursor.getColumnIndex(MITGLIEDER_ID);
	int emailIndex = cursor.getColumnIndex(MITLGIEDER_EMAIL);
	int passwortIndex = cursor.getColumnIndex(MITGLIEDER_PASSWORT);
	//int imageIndex = cursor.getColumnIndex(NOTIZ_IMAGE);

	String email = cursor.getString(emailIndex);
	String passwort= cursor.getString(passwortIndex);
//	String imagePath = cursor.getString(imageIndex);
	long newId = cursor.getLong(idIndex);
	
	Mitgliedtest mitglied = new Mitgliedtest();
	mitglied.setEmail(email);
	mitglied.setPasswort(passwort);
	mitglied.setId(newId);
	cursor.close();
	this.close();
	return mitglied;
}

public void deleteMitglied(Mitgliedtest notiz) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(MITGLIEDER_TABLE, MITGLIEDER_ID + "=?",
			new String[] { String.valueOf(notiz.getId()) });
	this.close();
}

public Cursor getAllNotizenCursor(){
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(MITGLIEDER_TABLE, null, null, null, null, null, null);
	return cursor;
}

	
	
	
}
