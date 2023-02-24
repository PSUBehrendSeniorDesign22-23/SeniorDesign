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
    @JoinColumn(name = "player_one_id", nullable = false, referencedColumnName = "player_id")
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_two_id", nullable = false, referencedColumnName = "player_id")
    private Player playerTwo;


    // Player scores default to -1 on object instantiation
    // This value is set prior to saving to the database

    @Column(nullable = false)
    private int playerOneScore = -1;

    @Column(nullable = false)
    private int playerTwoScore = -1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id")
    private Tournament tournament;

    public long getMatchId() {
        return this.id;
    }

    public Player getPlayerOne() {
        return this.playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return this.playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getPlayerOneScore() {
        return this.playerOneScore;
    }

    public void setPlayerOneScore(int score) {
        this.playerOneScore = score;
    }

    public int getPlayerTwoScore() {
        return this.playerTwoScore;
    }

    public void setPlayerTwoScore(int score) {
        this.playerTwoScore = score;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
