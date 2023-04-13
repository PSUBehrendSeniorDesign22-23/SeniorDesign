package com.behrend.contestmanager.models;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");


    private String role;
    Role(String role){
        this.role = role;
    }
}
