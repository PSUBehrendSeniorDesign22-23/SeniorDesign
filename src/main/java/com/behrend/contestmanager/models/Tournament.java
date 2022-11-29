package com.behrend.contestmanager.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Tournament {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private long id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 128)
    private String location;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "ruleset_id", name="RULESET_ID")
    private Ruleset ruleset;

    @ManyToMany
    @JoinTable(name = "tournament_player", 
                joinColumns = @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id"),
                inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "player_id"))
    private List<Player> players;

    public long getTournamentId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Ruleset getRuleset() {
        return this.ruleset;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
    }
}
