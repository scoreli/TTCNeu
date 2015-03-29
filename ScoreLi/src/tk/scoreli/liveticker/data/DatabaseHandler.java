/*
 * package tk.scoreli.liveticker.data;


import android.content.ContentValues;
import android.content.Context;
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

	public Mitglied neuesMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITLGIEDER_EMAIL, mitglied.getEmail());
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort());

		long id = db.insert(MITGLIEDER_TABLE, null, values);
		mitglied.setId(id);
		this.close();

		return mitglied;
	}

	public Mitglied updateMitglied(Mitglied mitglied) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITLGIEDER_EMAIL, mitglied.getEmail());
		values.put(MITGLIEDER_PASSWORT, mitglied.getPasswort());
		db.update(MITGLIEDER_TABLE, values, MITGLIEDER_ID + "=?",
				new String[] { String.valueOf(mitglied.getId()) });
		this.close();
		return mitglied;
	}

	
	
	
}
*/