package org.ic.tennistrader.domain.match;

public class Player {    
    String firstname;
    String lastname;
    Statistics statistics;
    
    public Player(){
        this.firstname = "";
        this.lastname = "";
    }
    
    public Player(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String toString(){
        return firstname + " " + lastname;
    }
}
