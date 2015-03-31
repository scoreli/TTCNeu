package tk.scoreli.liveticker.data;

public class Mitgliedtest {
	private String email, passwort;
	private long _id;
	
	public Mitgliedtest(String email, String passwort, int _id) {
		super();
		this.email = email;
		this.passwort = passwort;
		this._id = _id;
	}
public Mitgliedtest(){
	super();
}
public Mitgliedtest(String email, String passwort) {
	super();
	this.email = email;
	this.passwort = passwort;
}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public long getId() {
		return _id;
	}

	public void setId(long newId) {
		this._id = newId;
	}
	



}
