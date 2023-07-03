import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Authentication {
    private static HashMap<String, String> userCredentials() throws IOException {
        String filePath = "credentials.txt";
        HashMap<String, String> credentials = new HashMap<>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":", 2);
            if (parts.length >= 2) {
                String key = parts[0];
                String value = parts[1];
                credentials.put(key, value);
            }
        }
        reader.close();
        return credentials;
    }

    public static boolean authenticated(String username, String password) {
        boolean authenticated = false;
        try {
            for (var record : userCredentials().entrySet()) {
                if (Objects.equals(record.getKey(), username) && Objects.equals(record.getValue(), password)) {
                    authenticated = true;
                    break;
                }
                

//                if (Objects.equals(record.getKey(), username)) {
//                    HashFunction hash = new HashFunction();
//                    hash.update(password.getBytes());
//                    byte[] passwordHash = hash.digest();
//                    byte[] oldPasswordHash = record.getValue().getBytes();
//                    if (MessageDigest.isEqual(passwordHash, oldPasswordHash)) {
//                        authenticated = true;
//                        break;
//                    }
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authenticated;
    }
}
