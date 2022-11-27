package com.behrend.contestmanager.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Match")
public class Match {
    @Id
    private long matchId;
}
