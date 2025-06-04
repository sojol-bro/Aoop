package frontend.controllers;

import backend.controllers.CommentController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import backend.models.Comment;

import java.util.List;

public class CommentSectionController {

    @FXML private Label postTitle;
    @FXML private TextArea commentInput;
    @FXML private ListView<String> commentsList;

    private int postId;
    private int currentUserId;

    public void setContext(int postId, int userId, String postPreview) {
        this.postId = postId;
        this.currentUserId = userId;
        postTitle.setText(postPreview);
        loadComments();
    }

    @FXML
    public void onSubmitComment() {
        String comment = commentInput.getText().trim();
        if (!comment.isEmpty()) {
            CommentController.addComment(postId, currentUserId, comment);
            commentInput.clear();
            loadComments();
        }
    }

    private void loadComments() {
        commentsList.getItems().clear();
        List<Comment> comments = CommentController.getCommentsByPostId(postId);
        for (Comment c : comments) {
            commentsList.getItems().add("User " + c.getUserId() + ": " + c.getCommentText());
        }
    }
}
