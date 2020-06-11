package ClientSide;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.start();

    }
}
