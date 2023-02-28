package com.behrend.contestmanager.models;

import java.util.List;

import javax.persistence.*;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 64)
    private String skipperName;

    // True default value is 1000
    // This is set prior to saving a player to the database
    // It is set to null for the sake of update operations
    // When rank is null, an update operation knows to skip
    // updating this field, and a creation operation knows to
    // set the value to 1000
    @Column
    private Integer rank = null;

    @ManyToMany(mappedBy = "players")
    private List<Tournament> tournaments;

    @OneToMany(mappedBy = "playerOne")
    private List<Match> matchesOne;

    @OneToMany(mappedBy = "playerTwo")
    private List<Match> matchesTwo;

    public long getPlayerId() {
        return id;
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
}
