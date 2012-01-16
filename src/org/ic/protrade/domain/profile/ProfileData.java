package org.ic.protrade.domain.profile;

import java.util.Calendar;


public class ProfileData {
    private AccountFunds ukAccountFunds;
    private AccountFunds ausAccountFunds;
    private String title, firstName, surname, username;
    private String address1, address2, address3, townCity, countyState, postCode, country;
    private String homePhone, mobilePhone, emailAddress;
    private String timeZone, currency;
    private Integer gamcareLimit, gamcareLossLimit;
    private String gamcareFrequency, gamcareLossLimitFrequency;
    private Calendar gamcareUpdateDate;
        
    public ProfileData() {        
    }

    public void setUkAccountFunds(AccountFunds ukAccountFunds) {
        this.ukAccountFunds = ukAccountFunds;
    }

    public AccountFunds getUkAccountFunds() {
        return ukAccountFunds;
    }

    public void setAusAccountFunds(AccountFunds ausAccountFunds) {
        this.ausAccountFunds = ausAccountFunds;
    }

    public AccountFunds getAusAccountFunds() {
        return ausAccountFunds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public void setUkAccountFounds(AccountFunds ukAccountFounds) {
        this.ukAccountFunds = ukAccountFounds;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getTownCity() {
        return townCity;
    }

    public void setTownCity(String townCity) {
        this.townCity = townCity;
    }

    public String getCountyState() {
        return countyState;
    }

    public void setCountyState(String countyState) {
        this.countyState = countyState;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getGamcareLimit() {
        return gamcareLimit;
    }

    public void setGamcareLimit(Integer gamcareLimit) {
        this.gamcareLimit = gamcareLimit;
    }

    public String getGamcareFrequency() {
        return gamcareFrequency;
    }

    public void setGamcareFrequency(String gamcareFrequency) {
        this.gamcareFrequency = gamcareFrequency;
    }

    public Integer getGamcareLossLimit() {
        return gamcareLossLimit;
    }

    public void setGamcareLossLimit(Integer gamcareLossLimit) {
        this.gamcareLossLimit = gamcareLossLimit;
    }

    public String getGamcareLossLimitFrequency() {
        return gamcareLossLimitFrequency;
    }

    public void setGamcareLossLimitFrequency(String gamcareLossLimitFrequency) {
        this.gamcareLossLimitFrequency = gamcareLossLimitFrequency;
    }

    public Calendar getGamcareUpdateDate() {
        return gamcareUpdateDate;
    }

    public void setGamcareUpdateDate(Calendar gamcareUpdateDate) {
        this.gamcareUpdateDate = gamcareUpdateDate;
    }
}
