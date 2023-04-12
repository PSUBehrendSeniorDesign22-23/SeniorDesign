package com.behrend.contestmanager.models;

public enum ERole {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");


    private String role;
    ERole(String role){
        this.role = role;
    }
}
