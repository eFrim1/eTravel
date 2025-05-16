package com.etravel.userservice.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtExpirationMs}") private long expires;
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateToken(Authentication auth) {
        var algorithm = JWSAlgorithm.HS256;

        Instant now = Instant.now();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("eTravel")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusMillis(expires)))
                .subject(auth.getName())
                .claim("role", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .build();

        System.out.println(claims.toJSONObject());
        JWSHeader header = new JWSHeader(algorithm);

        var jwt = new SignedJWT(header, claims);

        var jwk = new OctetSequenceKey.Builder(jwtSecret.getBytes())
                .algorithm(algorithm)
                .build();
        SecretKey key = jwk.toSecretKey();

        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        }catch(JOSEException e){
            throw new RuntimeException("Unable to generate JWT token" + e);
        }
        return jwt.serialize();
    }
}