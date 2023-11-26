package be.bstorm.chesstt_springapi.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private byte[] secret = "Yabadabadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooo".getBytes(StandardCharsets.UTF_8);
    public int expireAt = 86400 * 7; // 7 Day
    public SecretKey secretKey = new SecretKeySpec(secret, "HmacSHA384");
}
