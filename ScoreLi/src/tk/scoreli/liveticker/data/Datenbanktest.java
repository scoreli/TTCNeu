package tk.scoreli.liveticker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Datenbanktest extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "mitglieder.db";
	private final static int DATABASE_VERSION = 1;

	private static final String MITGLIEDER_TABLE = "mitglieder";

	private static final String MITGLIEDER_ID = "_id";
	private static final String MITLGIEDER_EMAIL = "email";
	private static final String MITGLIEDER_PASSWORT = "passwort";

	public Datenbanktest(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDB = "CREATE TABLE MITGLIEDER" + MITGLIEDER_TABLE + "("
				+ MITGLIEDER_ID + " INTEGER PRIMARY KEY, " + MITLGIEDER_EMAIL
				+ "TEXT," + MITGLIEDER_PASSWORT + "TEXT)";
		db.execSQL(createDB);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Datenbanktest.class.getName(), "Upgrading database from version"
				+ oldVersion + "to" + newVersion
				+ ",which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS SCANITEM");
		onCreate(db);
	}

}
