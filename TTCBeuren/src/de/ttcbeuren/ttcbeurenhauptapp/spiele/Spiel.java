package de.ttcbeuren.ttcbeurenhauptapp.spiele;

public class Spiel {
	private int spiel_id, punkteHeim, punkteGast;
	private String spielsystem, mannschaftsart, heimverein, heimvereinsnummer,
			gastverein, gastvereinsnummer, status, spielbegindatumtime,
			spielende;

	private int istspielbeendet;

	public Spiel() {
	}

	public Spiel(int spiel_id, int punkteHeim, int punkteGast,
			String spielsystem, String mannschaftsart, String heimverein,
			String heimvereinsnummer, String gastverein,
			String gastvereinsnummer, String status,
			String spielbegindatumtime, String spielende, int istspielbeendet) {
		super();
		this.spiel_id = spiel_id;
		this.punkteHeim = punkteHeim;
		this.punkteGast = punkteGast;
		this.spielsystem = spielsystem;
		this.mannschaftsart = mannschaftsart;
		this.heimverein = heimverein;
		this.heimvereinsnummer = heimvereinsnummer;
		this.gastverein = gastverein;
		this.gastvereinsnummer = gastvereinsnummer;
		this.status = status;
		this.spielbegindatumtime = spielbegindatumtime;
		this.spielende = spielende;
		this.istspielbeendet = istspielbeendet;
	}

	public Spiel(int spiel_id, int punkteHeim, int punkteGast,
			String spielsystem, String mannschaftsart, String heimverein,
			String heimvereinsnummer, String gastverein,
			String gastvereinsnummer, String status,
			String spielbegindatumtime, int istspielbeendet) {
		super();
		this.spiel_id = spiel_id;
		this.punkteHeim = punkteHeim;
		this.punkteGast = punkteGast;
		this.spielsystem = spielsystem;
		this.mannschaftsart = mannschaftsart;
		this.heimverein = heimverein;
		this.heimvereinsnummer = heimvereinsnummer;
		this.gastverein = gastverein;
		this.gastvereinsnummer = gastvereinsnummer;
		this.status = status;
		this.spielbegindatumtime = spielbegindatumtime;
		this.istspielbeendet = istspielbeendet;
	}

	public Spiel(int punkteHeim, int punkteGast, String spielsystem,
			String mannschaftsart, String heimverein, String heimvereinsnummer,
			String gastverein, String gastvereinsnummer, String status,
			String spielbegindatumtime, String spielende, int istspielbeendet) {
		super();
		this.punkteHeim = punkteHeim;
		this.punkteGast = punkteGast;
		this.spielsystem = spielsystem;
		this.mannschaftsart = mannschaftsart;
		this.heimverein = heimverein;
		this.heimvereinsnummer = heimvereinsnummer;
		this.gastverein = gastverein;
		this.gastvereinsnummer = gastvereinsnummer;
		this.status = status;
		this.spielbegindatumtime = spielbegindatumtime;
		this.spielende = spielende;
		this.istspielbeendet = istspielbeendet;
	}

	public Spiel(int punkteHeim, int punkteGast, String spielsystem,
			String mannschaftsart, String heimverein, String heimvereinsnummer,
			String gastverein, String gastvereinsnummer, String status,
			String spielbegindatumtime, int istspielbeendet) {
		super();
		this.punkteHeim = punkteHeim;
		this.punkteGast = punkteGast;
		this.spielsystem = spielsystem;
		this.mannschaftsart = mannschaftsart;
		this.heimverein = heimverein;
		this.heimvereinsnummer = heimvereinsnummer;
		this.gastverein = gastverein;
		this.gastvereinsnummer = gastvereinsnummer;
		this.status = status;
		this.spielbegindatumtime = spielbegindatumtime;
		this.istspielbeendet = istspielbeendet;
	}

	public int getSpiel_id() {
		return spiel_id;
	}

	public void setSpiel_id(int spiel_id) {
		this.spiel_id = spiel_id;
	}

	public int getPunkteHeim() {
		return punkteHeim;
	}

	public void setPunkteHeim(int punkteHeim) {
		this.punkteHeim = punkteHeim;
	}

	public int getPunkteGast() {
		return punkteGast;
	}

	public void setPunkteGast(int punkteGast) {
		this.punkteGast = punkteGast;
	}

	public String getSpielsystem() {
		return spielsystem;
	}

	public void setSpielsystem(String spielsystem) {
		this.spielsystem = spielsystem;
	}

	public String getMannschaftsart() {
		return mannschaftsart;
	}

	public void setMannschaftsart(String mannschaftsart) {
		this.mannschaftsart = mannschaftsart;
	}

	public String getHeimverein() {
		return heimverein;
	}

	public void setHeimverein(String heimverein) {
		this.heimverein = heimverein;
	}

	public String getHeimvereinsnummer() {
		return heimvereinsnummer;
	}

	public void setHeimvereinsnummer(String heimvereinsnummer) {
		this.heimvereinsnummer = heimvereinsnummer;
	}

	public String getGastverein() {
		return gastverein;
	}

	public void setGastverein(String gastverein) {
		this.gastverein = gastverein;
	}

	public String getGastvereinsnummer() {
		return gastvereinsnummer;
	}

	public void setGastvereinsnummer(String gastvereinsnummer) {
		this.gastvereinsnummer = gastvereinsnummer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int isIstspielbeendet() {
		return istspielbeendet;
	}

	public void setIstspielbeendet(int istspielbeendet) {
		this.istspielbeendet = istspielbeendet;
	}

	public int getIstspielbeendet() {
		return istspielbeendet;
	}

	public String getSpielbegindatumtime() {
		return spielbegindatumtime;
	}

	public void setSpielbegindatumtime(String spielbegindatumtime) {
		this.spielbegindatumtime = spielbegindatumtime;
	}

	public String getSpielende() {
		return spielende;
	}

	public void setSpielende(String spielende) {
		this.spielende = spielende;
	}

	@Override
	public String toString() {
		return mannschaftsart+": "+heimverein + " " + heimvereinsnummer + " : " + gastverein + " "
				+ gastvereinsnummer + " " + punkteHeim + ":" + punkteGast;
	}

}
