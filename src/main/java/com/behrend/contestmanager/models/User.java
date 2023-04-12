package com.behrend.contestmanager.models;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @OneToOne(optional = true, mappedBy = "user")
    private Player player;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles",
                joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    public List<ERole> roles;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 11)
    private String phoneNum;

    @Column(nullable = true, length = 128)
    private String address;

    @Column(nullable = true, length = 64)
    private String password;

    public List<ERole> getRoles() {
        return this.roles;
    }

    public void setRoles(List<ERole> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public long getUserId() { return id;}
}
