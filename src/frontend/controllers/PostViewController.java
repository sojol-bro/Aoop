package frontend.controllers;

import backend.PostController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import models.Post;

import java.io.File;
import java.util.List;

public class PostViewController {

    @FXML private TextArea contentArea;
    @FXML private ListView<String> timelineList;

    private String selectedImagePath = null;
    private int currentUserId;

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        loadTimeline();
    }

    @FXML
    public void onUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
        }
    }

    @FXML
    public void onPost() {
        String content = contentArea.getText().trim();
        if (!content.isEmpty()) {
            PostController.createPost(currentUserId, content, selectedImagePath);
            contentArea.clear();
            selectedImagePath = null;
            loadTimeline();
        }
    }

    private void loadTimeline() {
        List<Post> posts = PostController.getTimelinePosts(currentUserId);
        timelineList.getItems().clear();

        for (Post post : posts) {
            int likeCount = LikeController.getLikeCount(post.getId());
            boolean hasLiked = LikeController.hasUserLikedPost(currentUserId, post.getId());
            String likeStatus = hasLiked ? "❤️" : "🤍";
            String item = String.format("[%s] %s\nLikes: %d %s", post.getCreatedAt(), post.getContent(), likeCount, likeStatus);
            timelineList.getItems().add(item);
        }
    }

}
