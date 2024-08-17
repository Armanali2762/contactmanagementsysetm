package com.e_commerce.made2automade.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.e_commerce.made2automade.Dao.UserRepositry;
import com.e_commerce.made2automade.Entities.User;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepositry userRepositry;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositry.getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with email: " + username);
        }

        return new CustomUserDetails(user);
    }
}
