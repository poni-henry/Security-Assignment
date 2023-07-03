import java.io.*;


public class ChatClient {
    public ChatClient(String username,
                      String password,
                      String serverHost, int serverPort,
                      byte[] myPrivateKey, byte[] serverPublicKey)
            throws IOException {

        SecureSocket432 sock = new SecureSocket432(serverHost, serverPort,
                myPrivateKey, serverPublicKey);

        OutputStream out = sock.getOutputStream();
        sendAuth(username, password, out);

        new ReceiverThread(sock.getInputStream());

        for (; ; ) {
            int c = System.in.read();
            if (c == -1) break;
            out.write(c);
            if (c == '\n') out.flush();
        }
        out.close();
    }

    public static void main(String[] argv) {
        String username = argv[0];
        String password = argv[1];
        String hostname = (argv.length <= 2) ? "localhost" : argv[1];
        try {
            if (Authentication.authenticated(username, password)) {
                new ChatClient(username, password, hostname, ChatServer.portNum, null, null);
            } else {
                System.err.println("User credentials do not match our records ");
            }
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private void sendAuth(String username, String password, OutputStream out) throws IOException {
        // create an AuthInfo object to authenticate the local user,
        // and send the AuthInfo to the server


        AuthInfo auth = new AuthInfo(username, password);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(auth);
        oos.flush();
    }

    class ReceiverThread extends Thread {
        // gather incoming messages, and display them

        private final InputStream in;

        ReceiverThread(InputStream inStream) {
            in = inStream;
            start();
        }

        public void run() {
            try {
                ByteArrayOutputStream baos;  // queues up stuff until carriage-return
                baos = new ByteArrayOutputStream();
                for (; ; ) {
                    int c = in.read();
                    if (c == -1) {
                        spew(baos);
                        break;
                    }
                    baos.write(c);
                    if (c == '\n') spew(baos);
                }
            } catch (IOException ignored) {

            }
        }

        private void spew(ByteArrayOutputStream baos) throws IOException {
            byte[] message = baos.toByteArray();
            baos.reset();
            System.out.write(message);
        }
    }
}
