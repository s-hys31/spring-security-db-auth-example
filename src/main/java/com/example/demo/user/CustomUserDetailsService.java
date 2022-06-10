package com.example.demo.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(username);
        }

        var user = userRepository.findByName(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList());

        return buildUserDetails(user, authorities);
    }

    @Transactional
    public void registerUser(String username, String password, String... roles) {
        userRepository.save(buildUser(username, password, roles));
    }

    private User buildUser(String username, String password, String... roleNames) {
        var user = new User();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);

        var roles = new ArrayList<Role>();
        for (String roleName : roleNames) {
            roles.add(roleRepository.findByName(roleName));
        }

        user.setRoles(roles);

        return user;
    }

    private UserDetails buildUserDetails(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                user.isEnabled(), isAccountNonExpired(user), isCredentialsNonExpired(user), isAccountNonLocked(user),
                authorities);
    }

    private boolean isAccountNonExpired(User user) {
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

    private boolean isCredentialsNonExpired(User user) {
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

    private boolean isAccountNonLocked(User user) {
        if (user.getLockedAt() == null) {
            return true;
        }

        return false;
    }

}
