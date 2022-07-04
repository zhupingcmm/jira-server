package com.mf.jira.server.util;

import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.config.AppConfig;
import com.mf.jira.server.exception.JiraException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.val;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String createToken(AppConfig appConfig, String userName, String password) {
        Map <String, Object> map = new HashMap<>();
        map.put("username",userName);
        map.put("password", password);
        return Jwts.builder()
                .setId(appConfig.getJwt().getJwtId())
                .setSubject(userName)
                .addClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + appConfig.getJwt().getTokenExpireTime()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            if ( e instanceof ExpiredJwtException) {
                throw new JiraException(ResponseEnum.TOKEN_EXPIRED);
            }
        }
        return false;
    }

    public static Claims parseClaims(String token) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            return claims;
        } catch (Exception e) {
            throw new JiraException(1,e.getMessage());
        }
    }


}
