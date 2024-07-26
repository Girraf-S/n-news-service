package com.solbeg.nnewsservice.security.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.solbeg.nnewsservice.security.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImplDeserializer extends JsonDeserializer<UserDetailsImpl> {
    @Override
    public UserDetailsImpl deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String username = jsonNode.get("username").asText();
        String password = "********";
        boolean active = jsonNode.get("active").asBoolean();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(jsonNode.isArray()){
            for (JsonNode node:
                 jsonNode) {
                authorities.add(new SimpleGrantedAuthority(node.asText()));
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
