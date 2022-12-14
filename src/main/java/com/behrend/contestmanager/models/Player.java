package com.behrend.contestmanager.models;

import java.util.List;

import javax.persistence.*;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private long id;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(length = 64)
    private String skipperName;

    @Column
    private Integer rank = 1000;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 11)
    private String phoneNum;

    @ManyToMany(mappedBy = "players")
    private List<Tournament> tournaments;

    @OneToMany(mappedBy = "playerOne")
    private List<Match> matchesOne;

    @OneToMany(mappedBy = "playerTwo")
    private List<Match> matchesTwo;

    public long getPlayerId() {
        return id;
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
}
