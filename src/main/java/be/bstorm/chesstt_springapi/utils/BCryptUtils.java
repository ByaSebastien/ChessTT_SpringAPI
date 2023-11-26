package be.bstorm.chesstt_springapi.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptUtils {

    private final PasswordEncoder passwordEncoder;

    public BCryptUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(String password){
        return passwordEncoder.encode(password);
    }

    public boolean verify(String password,String hashedPassword){
        return passwordEncoder.matches(password,hashedPassword);
    }
}
