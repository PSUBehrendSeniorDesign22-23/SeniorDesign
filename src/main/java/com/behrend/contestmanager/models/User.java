package com.behrend.contestmanager.models;
import com.behrend.contestmanager.models.Roles;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private long id;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 11)
    private String phoneNum;

    @Column(nullable = false, length = 64)
    private String address;

    @Column(nullable = false, length = 64)
    private String password;



}
