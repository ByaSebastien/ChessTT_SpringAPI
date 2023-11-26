package be.bstorm.chesstt_springapi.utils;


import be.bstorm.chesstt_springapi.configs.JwtConfig;
import be.bstorm.chesstt_springapi.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final JwtConfig config;
    private final JwtParser parser;
    private final JwtBuilder builder;

    public JwtUtils(JwtConfig config) {
        this.config = config;
        this.parser = Jwts.parserBuilder().setSigningKey(config.secretKey).build();
        this.builder = Jwts.builder().signWith(config.secretKey);
    }

    public String generateToken(User user) {
        return builder
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + config.expireAt * 1000L))
                .compact();
    }

    public Claims getClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    public Long getId(String token) {
        return getClaims(token).get("id", Long.class);
    }

    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean isValid(String token) {
        Claims claims = getClaims(token);
        Date now = new Date();
        return getRole(token) != null && now.after(claims.getIssuedAt()) && now.before(claims.getExpiration());
    }
}
