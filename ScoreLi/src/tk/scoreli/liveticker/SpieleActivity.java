package tk.scoreli.liveticker;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Veranstaltung;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SpieleActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	private ListView Veranstaltungsliste;
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	 public static final String KEY = "ID_Veranstaltung";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spiele);

		/*
		 * Komischerweise führt Android automatisch die toString methode aus und
		 * gibt die Veranstaltung als String aus.
		 * http://app-makers.blogspot.de/2010/05/eine-listview-mit-inhalt-fullen.html
		 * http://www.appartig.net/?e=18
		 */
		try {
			Veranstaltungsliste = (ListView) findViewById(R.id.listView1);
			ListAdapter listenAdapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1,
					db.getAllVeranstaltungen());
			Veranstaltungsliste.setAdapter(listenAdapter);
			Veranstaltungsliste.setOnItemClickListener(this);
			Veranstaltungsliste.setOnItemLongClickListener(this);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();

		}

	
	}
/*
 * Hier fehlt noch das die Id von der Veranstaltung angezeigt wird.
 */
	@Override
    public void onItemClick(AdapterView<?> lV, View view, int pos, long id) { 
		//Hier wird das Objekt geholt der Liste und unten die Id geholt
		Veranstaltung veranstaltung= (Veranstaltung) Veranstaltungsliste.getItemAtPosition(pos);
		Intent i=new Intent(SpieleActivity.this,
			UpdateSpielActivity.class);
		i.putExtra(KEY, veranstaltung.getId());
		startActivity(i);
		
    } 
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		return false;
	}
	/*
	 * 
	 * 
	 * private static class VeranstaltungsListAdapter extends CursorAdapter {
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { Veranstaltung veranstaltung = getItem(position); return
	 * super.getView(position, convertView, parent); }
	 * 
	 * public VeranstaltungsListAdapter(Context context, int textViewResourceId,
	 * List<Veranstaltung> objects) { super(context, textViewResourceId,
	 * objects); // TODO Auto-generated constructor stub }
	 * 
	 * @Override public void bindView(View arg0, Context arg1, Cursor arg2) { //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public View newView(Context arg0, Cursor arg1, ViewGroup arg2)
	 * { // TODO Auto-generated method stub return null; }
	 * 
	 * }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spiele, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.spiel_neueVeranstaltung) {
			startActivity(new Intent(SpieleActivity.this,
					NeuesSpielActivity.class));
			
			return true;
		}
	
		return super.onOptionsItemSelected(item);
	}
	

}