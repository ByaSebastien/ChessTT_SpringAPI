package be.bstorm.chesstt_springapi.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordGeneratorUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    public String generate(int size) {

        StringBuilder password = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < size; i++){
            int randomIndex = r.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }
        return password.toString();
    }
}
