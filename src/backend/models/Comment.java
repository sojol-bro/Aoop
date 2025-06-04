package backend.models;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int postId;
    private int userId;
    private String commentText;
    private LocalDateTime commentedAt;

    public Comment() {
        // Default constructor
    }

    public Comment(int id, int postId, int userId, String commentText, LocalDateTime commentedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
        this.commentedAt = commentedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", commentText='" + commentText + '\'' +
                ", commentedAt=" + commentedAt +
                '}';
    }
}
