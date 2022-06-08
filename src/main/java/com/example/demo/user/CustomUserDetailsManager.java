package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

public class CustomUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(username);
        }

        var user = userRepository.findByName(username);
        List<GrantedAuthority> authorities = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList());

        return new CustomUserDetails(user, authorities);
    }

<<<<<<< HEAD:src/main/java/com/example/demo/user/CustomUserDetailsService.java
    @Transactional
    public void registerUser(String username, String password) {
        userRepository.save(buildUser(username, password, "USER"));
    }

    private User buildUser(String username, String password, String... roleNames) {
        var user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEnabled(true);

        var roles = new ArrayList<Role>();
        for (String roleName : roleNames) {
            roles.add(roleRepository.findByName(roleName));
        }

        user.setRoles(roles);

        return user;
=======
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createUser(UserDetails user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteUser(String username) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean userExists(String username) {
        // TODO Auto-generated method stub
        return false;
>>>>>>> 5221b0415939c49c60f64d5edb97829071e6a543:src/main/java/com/example/demo/user/CustomUserDetailsManager.java
    }

}
