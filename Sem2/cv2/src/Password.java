import java.io.Serializable;
import java.security.*;
import java.util.*;

public class Password implements Serializable {
    private String hashedPassword;
    private String salt;

    public Password() {

    }

    public Password(String hashedPassword, String salt) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public void hashPassword(String password) throws NoSuchAlgorithmException {
        this.salt = generateSalt();
        byte[] hash = MessageDigest.getInstance("SHA3-256").digest((password + salt).getBytes());
        this.hashedPassword = Base64.getEncoder().encodeToString(hash);
    }

    private String generateSalt() throws NoSuchAlgorithmException {
        byte[] saltBytes = new byte[32];
        new SecureRandom().getInstance("NativePRNG").nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return this.salt;
    }
}
