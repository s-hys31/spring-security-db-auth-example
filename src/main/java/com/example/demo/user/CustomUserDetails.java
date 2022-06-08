package com.example.demo.user;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

public class CustomUserDetails implements UserDetails {

    private User user;
    private Collection<GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public CustomUserDetails() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        var expired = user.getExpiredDate();
        if (expired == null) {
            return true;
        }

        var now = new Date();
        if (now.compareTo(expired) == -1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (user.getLockedAt() == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        var expired = user.getCredentialsExpiredDate();
        if (expired == null) {
            return true;
        }

        var now = new Date();
        if (now.compareTo(expired) == -1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}
