package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Tournament")
public class Tournament {
    
    @Id
    private long tournamentID;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 128)
    private String location;

    

}
