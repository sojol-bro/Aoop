package server;

import org.glassfish.tyrus.server.Server;

public class WebSocketLauncher {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/ws", WebSocketServer.class);

        try {
            server.start();
            System.out.println("WebSocket server started at ws://localhost:8025/ws/chat");
            System.in.read(); // Wait for user input to exit
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
