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
    @JoinColumn(nullable = false, referencedColumnName = "player_id")
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "player_id")
    private Player playerTwo;

    @Column(nullable = false)
    private int playerOneScore;

    @Column(nullable = false)
    private int playerTwoScore;
}
