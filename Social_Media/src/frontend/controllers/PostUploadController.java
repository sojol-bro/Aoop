package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import models.Post;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class PostUploadController {

    @FXML private TextArea contentField;
    @FXML private Label imageNameLabel;

    private File selectedImage;
    private final int userId = 1;  // Replace with logged-in user's ID

    public void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImage = file;
            imageNameLabel.setText(file.getName());
        }
    }

    public void submitPost() {
        String content = contentField.getText();
        String imagePath = null;

        if (selectedImage != null) {
            try {
                Path destinationDir = Paths.get("uploads");
                if (!Files.exists(destinationDir)) Files.createDirectory(destinationDir);

                Path dest = destinationDir.resolve(selectedImage.getName());
                Files.copy(selectedImage.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                imagePath = dest.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PostController.createPost(userId, content, imagePath);
        contentField.clear();
        imageNameLabel.setText("No file chosen");
    }
}
