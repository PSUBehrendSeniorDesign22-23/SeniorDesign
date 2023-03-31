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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private ERole name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
    public void setName(ERole roleName){
        this.name = roleName;
    }
}
