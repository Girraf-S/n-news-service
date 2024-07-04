package com.solbeg.nnewsservice.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImplDeserializer extends JsonDeserializer<UserDetailsImpl> {
    @Override
    public UserDetailsImpl deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String username = jsonNode.get("username").asText();
        String password = "********";//jsonNode.get("password").asText();
        boolean active = jsonNode.get("active").asBoolean();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        JsonNode arrNode = jsonNode.get("authorities");
        if(arrNode.isArray()){
            for (JsonNode objNode:
                    arrNode) {
                authorities.add(new SimpleGrantedAuthority(objNode.get("authority").asText()));
            }
        }

        return UserDetailsImpl.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .isActive(active)
                .build();
    }
}
