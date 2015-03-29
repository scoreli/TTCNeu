package tk.scoreli.liveticker.data;

public class Mitgliedtest {
	private String email, passwort;

	public static long id = 1;

	/*public Mitgliedtest(String email, String passwort) {
		super();
		this.email = email;
		this.passwort = passwort;
		id++;
	}
*/
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
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
