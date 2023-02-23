package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class UserService implements UserDetails, UserDetailsService {

    private String firstName;
    private String lastName;
    private String phoneNum;


    private static long loggedIn = 0;

    public UserService(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNum = user.getPhoneNum();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public String getLastName() {
        return lastName;
    }

   public String getFirstName() {
        return firstName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public static void setLoggedIn(long userID) {
        loggedIn = userID;
    }

}
