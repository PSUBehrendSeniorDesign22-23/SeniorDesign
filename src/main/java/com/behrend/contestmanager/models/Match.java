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

    @Column(nullable = false)
    private int playerOneScore;

    @Column(nullable = false)
    private int playerTwoScore;

    @ManyToOne(optional = true)
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
}
