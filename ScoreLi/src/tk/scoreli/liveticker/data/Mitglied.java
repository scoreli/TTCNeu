package tk.scoreli.liveticker.data;

public class Mitglied {
	 
    //private variables
    long _id;
    String email;
    String passwort;
     
    // Empty constructor
    public Mitglied(){
         
    }
    // constructor
    public Mitglied(int id, String name, String passwort){
        this._id = id;
        this.email = name;
        this.passwort = passwort;
    }
     
    // constructor
    public Mitglied(String name, String passwort){
        this.email = name;
        this.passwort = passwort;
    }
    // getting ID
    public long getID(){
        return this._id;
    }
     
    // setting id
    public void setID(long id){
        this._id = id;
    }
     
    // getting name
    public String getEmail(){
        return this.email;
    }
     
    // setting name
    public void setEmail(String name){
        this.email = name;
    }
     
    // getting phone number
    public String getPasswort(){
        return this.passwort;
    }
     
    // setting phone number
    public void setPasswort(String phone_number){
        this.passwort = phone_number;
    }
}