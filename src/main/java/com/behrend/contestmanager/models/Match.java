package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matchId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "playerId")
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "playerId")
    private Player playerTwo;

    @Column(nullable = false)
    private int playerOneScore;

    @Column(nullable = false)
    private int playerTwoScore;

    @ManyToOne(optional = true)
    @JoinColumn(referencedColumnName = "tournamentId")
    private Tournament tournament;

    public long getMatchId() {
        return this.matchId;
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
}
