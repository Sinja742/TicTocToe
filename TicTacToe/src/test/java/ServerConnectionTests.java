import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

class ServerConnectionTests {

    private final ServerConnection serverConnection = new ServerConnection();

    @Test
    void connectTest() throws IOException {
//        Given
        serverConnection.initializeServer();
//        When
        boolean connected = serverConnection.connect();
        Socket socket = serverConnection.getSocket();
//        Then
        assertNotNull(socket);
        assertTrue(connected);

        ServerSocket serverSocket = serverConnection.getServerSocket();
        serverSocket.close();

    }

    @Test
    void connectTest_false() {
//        Given
//        When
        boolean connected = serverConnection.connect();
//        Then
        assertFalse(connected);
    }

    @Test
    void initializeServerTest() throws IOException {
//        Given
//        When
        serverConnection.initializeServer();
        ServerSocket serverSocket = serverConnection.getServerSocket();
//        Then
        assertNotNull(serverSocket);
        serverSocket.close();
    }
}