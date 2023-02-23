package com.behrend.contestmanager.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Roles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(nullable = false, length = 45)
    private String name;

    public void setName(String rName){
        this.name = rName;
    }

}
