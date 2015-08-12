package de.ttcbeuren.ttcbeurenhauptapp;

import java.sql.Date;
import java.sql.Time;

public class Spiel {
	private int spiel_id, punkteHeim, punkteGast;
	private String spielsystem, mannschaftsart, heimverein, heimvereinsnummer,
			gastverein, gastvereinsnummer, status;
	private Time spielbegin, spielende;
	private int istspielbeendet;
	private Date spieldatum;

	public Spiel(int spiel_id, int punkteHeim, int punkteGast,
			String spielsystem, String mannschaftsart, String heimverein,
			String heimvereinsnummer, String gastverein,
			String gastvereinsnummer, String status, Time spielbegin,
			Time spielende, int istspielbeendet, Date spieldatum) {
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
		this.spielbegin = spielbegin;
		this.spielende = spielende;
		this.istspielbeendet = istspielbeendet;
		this.spieldatum = spieldatum;
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

	public Time getSpielbegin() {
		return spielbegin;
	}

	public void setSpielbegin(Time spielbegin) {
		this.spielbegin = spielbegin;
	}

	public Time getSpielende() {
		return spielende;
	}

	public void setSpielende(Time spielende) {
		this.spielende = spielende;
	}

	public int isIstspielbeendet() {
		return istspielbeendet;
	}

	public void setIstspielbeendet(int istspielbeendet) {
		this.istspielbeendet = istspielbeendet;
	}

	public Date getSpieldatum() {
		return spieldatum;
	}

	public void setSpieldatum(Date spieldatum) {
		this.spieldatum = spieldatum;
	}

	@Override
	public String toString() {
		return heimverein + " " + heimvereinsnummer + " gegen " + gastverein
				+ " " + gastvereinsnummer + " " + punkteHeim + ":" + punkteGast
				+ " Spielsystem: " + spielsystem + " " + mannschaftsart
				+ "Spielbeginn:" + spielbegin + spieldatum + "Spielende:"
				+ spielende;
	}

}
