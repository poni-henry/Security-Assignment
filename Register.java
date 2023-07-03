import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Register {
    public static void main(String[] args) {
        System.out.println(signUp("mike", "password"));
    }

    public static boolean signUp(String username, String password) {
        String filePath = "credentials.txt";
        HashFunction hash = new HashFunction();
        hash.update(password.getBytes());
        String record = username + ":" + password;
//        String record = username + ":" + Arrays.toString(hash.digest());
        boolean registeredSuccessfully = false;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.newLine();
            bw.append(record);
            bw.close();
            registeredSuccessfully = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return registeredSuccessfully;
    }
}
