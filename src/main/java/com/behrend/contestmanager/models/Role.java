package com.behrend.contestmanager.models;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
    public void setName(String roleName){
        this.name = roleName;
    }
}
