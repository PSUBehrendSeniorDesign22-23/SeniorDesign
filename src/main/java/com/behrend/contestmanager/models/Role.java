package com.behrend.contestmanager.models;

import javax.persistence.*;
import java.util.Set;

import java.util.List;

@Entity
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
    public void setName(String rName){
        this.name = rName;
    }

    public void setName(String ruleName){
        this.name = ruleName;
    }
}
