package tk.scoreli.liveticker.data;

public class Mitglied {

	// private variables
	long _id;
	String uuid;

	public Mitglied() {
	}

	public Mitglied(String uuid) {
		super();
		this.uuid = uuid;
	}

	public Mitglied(long _id, String uuid) {
		super();
		this._id = _id;
		this.uuid = uuid;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}