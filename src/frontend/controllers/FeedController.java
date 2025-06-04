package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import backend.models.Post;
import backend.models.User;
import backend.controllers.PostController;

import java.util.List;

public class FeedController {

    @FXML private TextArea postContent;
    @FXML private VBox feedBox;

    private int currentUserId = 1; // Simulate login

    @FXML
    public void initialize() {
        loadFeed();
    }

    @FXML
    public void handleCreatePost() {
        String content = postContent.getText();
        if (!content.isEmpty()) {
            boolean success = PostController.createPost(currentUserId, content, null);
            if (success) {
                postContent.clear();
                loadFeed();
            }
        }
    }

    private void loadFeed() {
        feedBox.getChildren().clear();
        List<Post> posts = PostController.getAllPosts();

        for (Post post : posts) {
            VBox postCard = new VBox(5);
            postCard.setStyle("-fx-border-color: gray; -fx-padding: 10;");

            Label author = new Label("User ID: " + post.getUserId());
            Label content = new Label(post.getContent());
            Label date = new Label(post.getCreatedAt().toString());

            postCard.getChildren().addAll(author, content, date);

            // Delete button if current user owns post
            if (post.getUserId() == currentUserId) {
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    PostController.deletePost(post.getId(), currentUserId);
                    loadFeed();
                });
                postCard.getChildren().add(deleteBtn);
            }

            feedBox.getChildren().add(postCard);
        }
    }
}
