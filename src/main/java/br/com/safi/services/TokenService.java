package br.com.safi.services;

import br.com.safi.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${safi.jwt.expiration}")
    private String expiration;

    @Value("${safi.jwt.secret}")
    private String secret;

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception ex) {
            return false;
        }
    }

    public String generateTokenJwt(Authentication authentication) throws JsonProcessingException {
        User user = (User) authentication.getPrincipal();
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));
        String json = new ObjectMapper().writer().writeValueAsString(user);
        return Jwts.builder()
                .setIssuer("safi-api")
                .setSubject(json)
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Long getUserId(String token) {
        var body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        var subject = body.getSubject();
        return Long.parseLong(subject);
    }
}
