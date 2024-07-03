package com.solbeg.nnewsservice.service;

import com.solbeg.nnewsservice.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserFromJson(username);
    }

    private UserDetailsImpl getUserFromJson(String username) {
        return restTemplate.getForObject("http://localhost:8081/auth/users/{username}",
                UserDetailsImpl.class, username);
    }
}

