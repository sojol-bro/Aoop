package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import websocket.WebSocketLauncher;

import java.sql.Connection;
import java.sql.DriverManager;

public class MainApp extends Application {

    public static Connection databaseConnection;

    @Override
    public void start(Stage primaryStage) {
        // 1. Start WebSocket server in a background thread
        new Thread(() -> {
            try {
                WebSocketLauncher.main(null);
            } catch (Exception e) {
                System.err.println("WebSocket server failed to start: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // 2. Initialize MySQL database (XAMPP)
        try {
            databaseConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/socialmedia", "root", ""
            );
            System.out.println("✅ Database connected successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
            return;
        }

        // 3. Load initial scene (Login UI)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Social Media Platform");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("❌ Failed to load Login UI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
