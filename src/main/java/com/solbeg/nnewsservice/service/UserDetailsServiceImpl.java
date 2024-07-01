package com.solbeg.nnewsservice.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getFromJson(username);
    }

    private UserDetails getFromJson(String username) {
        UserDetails user;
        try {
            String answer = restTemplate.getForObject("http://localhost:8081/auth/users/{username}",
                    String.class, username);

            JSONObject jsonObject = new JSONObject(answer);

            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            JSONArray jsonArray = jsonObject.getJSONArray("authorities");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                authorities.add(new SimpleGrantedAuthority(temp.getString("authority")));
            }

            user = User.builder()
                    .username(jsonObject.getString("username"))
                    .password(jsonObject.getString("password"))
                    .authorities(authorities)
                    .disabled(!jsonObject.getBoolean("active"))
                    .build();
        } catch (JSONException | HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}

