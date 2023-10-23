package guru.qa.niffler.utils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class OauthUtils {

    public static final SecureRandom secureRandom = new SecureRandom();

    @Nonnull
    public static String codeVerifier() {
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    @Nonnull
    public static String codeChallenge(String codeVerifier) {
        byte[] bytes;
        MessageDigest messageDigest;
        try {
            bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new RuntimeException();
        }
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
