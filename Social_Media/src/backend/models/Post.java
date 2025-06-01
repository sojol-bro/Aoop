package models;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private int userId;
    private String content;
    private String imagePath;
    private LocalDateTime createdAt;

    public Post(int id, int userId, String content, String imagePath, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters and setters
}
