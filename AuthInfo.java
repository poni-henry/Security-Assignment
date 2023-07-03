import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;


public class AuthInfo implements Serializable {
    // The fields of this object are set by the client, and used by the
    // server to validate the client's identity.  The client constructs this
    // object (by calling the constructor).  The client software (in another
    // source code file) then sends the object across to the server.  Finally,
    // the server verifies the object by calling isValid().

    private final String username;
    private final String password;

    public AuthInfo(String name, String password) {
        // This is called by the client to initialize the object.
        this.username = name;
        this.password = password;
    }

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

    public boolean isValid() {
        // This is called by the server to make sure the user is who he/she
        // claims to be.

        // Presently, this is totally insecure -- the server just accepts the
        // client's assertion without checking anything.  Homework assignment 1
        // is to make this more secure.
        boolean authenticated = false;
        try {
            for (var record : userCredentials().entrySet()) {
                if (Objects.equals(record.getKey(), username) && Objects.equals(record.getValue(), password)) {
                    System.out.println("Username: " + username + " password: " + password);
                    authenticated = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return authenticated;
    }

    public String getUserName() {
        return isValid() ? username : null;
    }
}
