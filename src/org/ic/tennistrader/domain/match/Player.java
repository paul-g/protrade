package org.ic.tennistrader.domain.match;

public class Player {
    private String firstname;
    private String lastname;
    private Statistics statistics = null;

    private String country;
    private String dob;
    private String height;
    private String wonLost;
    private String titles;
    private String rank;
    private String age;
    private String plays;

    public Player() {
        this.firstname = "";
        this.lastname = "";
    }

    public Player(String firstname, String lastname) {
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

    public String toString() {
    	if (lastname.equals("Tsonga"))
    		return "Jo-Wilfried Tsonga";
        return firstname + " " + lastname;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWonLost() {
        return wonLost;
    }

    public void setWonLost(String wonLost) {
        this.wonLost = wonLost;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPlays() {
        return plays;
    }

    public void setPlays(String plays) {
        this.plays = plays;
    }
}
