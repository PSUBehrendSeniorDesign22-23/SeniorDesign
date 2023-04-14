package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "defender_id", nullable = false, referencedColumnName = "player_id")
    private Player defender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "challenger_id", nullable = false, referencedColumnName = "player_id")
    private Player challenger;


    // Player scores default to -1 on object instantiation
    // This value is set prior to saving to the database

    @Column(nullable = false)
    private int defenderScore = -1;

    @Column(nullable = false)
    private int challengerScore = -1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id")
    private Tournament tournament;

    public long getMatchId() {
        return this.id;
    }

    public Player getDefender() {
        return this.defender;
    }

    public void setDefender(Player playerOne) {
        this.defender = playerOne;
    }

    public Player getChallenger() {
        return this.challenger;
    }

    public void setChallenger(Player playerTwo) {
        this.challenger = playerTwo;
    }

    public int getDefenderScore() {
        return this.defenderScore;
    }

    public void setDefenderScore(int score) {
        this.defenderScore = score;
    }

    public int getChallengerScore() {
        return this.challengerScore;
    }

    public void setChallengerScore(int score) {
        this.challengerScore = score;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
