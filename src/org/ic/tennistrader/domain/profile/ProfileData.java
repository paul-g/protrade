package org.ic.tennistrader.domain.profile;


public class ProfileData {
    private AccountFunds ukAccountFounds;
    private AccountFunds ausAccountFunds;
    
    public ProfileData() {        
    }

    public void setUkAccountFunds(AccountFunds ukAccountFunds) {
        this.ukAccountFounds = ukAccountFunds;
    }

    public AccountFunds getUkAccountFunds() {
        return ukAccountFounds;
    }

    public void setAusAccountFunds(AccountFunds ausAccountFunds) {
        this.ausAccountFunds = ausAccountFunds;
    }

    public AccountFunds getAusAccountFunds() {
        return ausAccountFunds;
    }
    
}
