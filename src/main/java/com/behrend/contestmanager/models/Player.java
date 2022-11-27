package com.behrend.contestmanager.models;

import javax.persistence.*;

@Entity
public class Player {
    @Id
    private long playerID;

    @Column(nullable = false, length = 64)
    private String firstName;

    public long getPlayerID() {
        return playerID;
    }

    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSkipperName() {
        return skipperName;
    }

    public void setSkipperName(String skipperName) {
        this.skipperName = skipperName;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(length = 64)
    private String skipperName;

    @Column
    private Integer rank;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 11)
    private String phoneNum;

}
