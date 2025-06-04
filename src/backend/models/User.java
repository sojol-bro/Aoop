package backend.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String bio;
    private String profileImage;


    public User(int id, String username, String password, String bio, String profileImage) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.profileImage = profileImage;
    }

    // Add new getters and setters
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImage() { return profileImage; }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

// Add new constructor

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    // Getters and setters
} 

