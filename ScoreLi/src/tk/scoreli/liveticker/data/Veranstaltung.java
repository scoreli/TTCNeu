package tk.scoreli.liveticker.data;

public class Veranstaltung {
	private String Heimmanschaft, Gastmannschaft, Spielbeginn, Sportart;
	private long id;
	private int SpielstandHeim, SpielstandGast;

	public Veranstaltung() {
	}

	public Veranstaltung(String heimmanschaft, String gastmannschaft,
			String spielbeginn, String sportart, int spielstandHeim,
			int spielstandGast) {
		super();
		Heimmanschaft = heimmanschaft;
		Gastmannschaft = gastmannschaft;
		Spielbeginn = spielbeginn;
		Sportart = sportart;
		SpielstandHeim = spielstandHeim;
		SpielstandGast = spielstandGast;
	}

	public Veranstaltung(String heimmanschaft, String gastmannschaft,
			String spielbeginn, String sportart, long id, int spielstandHeim,
			int spielstandGast) {
		super();
		Heimmanschaft = heimmanschaft;
		Gastmannschaft = gastmannschaft;
		Spielbeginn = spielbeginn;
		Sportart = sportart;
		this.id = id;
		SpielstandHeim = spielstandHeim;
		SpielstandGast = spielstandGast;
	}

	public String getHeimmanschaft() {
		return Heimmanschaft;
	}

	public void setHeimmanschaft(String heimmanschaft) {
		Heimmanschaft = heimmanschaft;
	}

	public String getGastmannschaft() {
		return Gastmannschaft;
	}

	public void setGastmannschaft(String gastmannschaft) {
		Gastmannschaft = gastmannschaft;
	}

	public String getSpielbeginn() {
		return Spielbeginn;
	}

	public void setSpielbeginn(String spielbeginn) {
		Spielbeginn = spielbeginn;
	}

	public String getSportart() {
		return Sportart;
	}

	public void setSportart(String sportart) {
		Sportart = sportart;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpielstandHeim() {
		return SpielstandHeim;
	}

	public void setSpielstandHeim(int spielstandHeim) {
		SpielstandHeim = spielstandHeim;
	}

	public int getSpielstandGast() {
		return SpielstandGast;
	}

	public void setSpielstandGast(int spielstandGast) {
		SpielstandGast = spielstandGast;
	}

}