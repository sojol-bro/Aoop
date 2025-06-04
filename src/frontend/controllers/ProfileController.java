package frontend.controllers;

import backend.ProfileController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import backend.models.User;

import java.io.File;

public class ProfileController {

    @FXML private ImageView profileImage;
    @FXML private Label usernameLabel;
    @FXML private TextArea bioTextArea;
    @FXML private TextField locationField;

    private int currentUserId;
    private String currentProfilePicPath;

    public void setUserId(int userId) {
        this.currentUserId = userId;
        loadProfile(userId);
    }

    private void loadProfile(int userId) {
        User user = ProfileController.getProfile(userId);
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            bioTextArea.setText(user.getBio());
            locationField.setText(user.getLocation());

            currentProfilePicPath = user.getProfilePic();
            if (currentProfilePicPath != null) {
                File file = new File(currentProfilePicPath);
                if (file.exists()) {
                    profileImage.setImage(new Image(file.toURI().toString()));
                }
            }
        }
    }

    @FXML
    public void onChangePictureClicked() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Profile Picture");
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            currentProfilePicPath = "uploads/profile_pics/" + selectedFile.getName();
            File dest = new File(currentProfilePicPath);
            selectedFile.renameTo(dest); // or copy the file here
            profileImage.setImage(new Image(dest.toURI().toString()));
        }
    }

    @FXML
    public void onSaveProfileClicked() {
        String bio = bioTextArea.getText();
        String location = locationField.getText();
        if (ProfileController.updateProfile(currentUserId, bio, location, currentProfilePicPath)) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile.");
        }
    }
    @FXML private Button followButton;
    @FXML private Label followersLabel, followingLabel;

    private boolean isFollowing = false;
    private int viewedUserId;

    public void setUserId(int userId) {
        this.currentUserId = userId;
        loadProfile(userId);
    }

    public void viewOtherUserProfile(int userIdToView) {
        this.viewedUserId = userIdToView;
        this.isFollowing = FollowController.isFollowing(currentUserId, viewedUserId);
        updateFollowButton();
        loadProfile(userIdToView);
    }

    @FXML
    public void onFollowClicked() {
        if (isFollowing) {
            FollowController.unfollow(currentUserId, viewedUserId);
        } else {
            FollowController.follow(currentUserId, viewedUserId);
        }
        isFollowing = !isFollowing;
        updateFollowButton();
        updateFollowCounts();
    }

    private void updateFollowButton() {
        followButton.setText(isFollowing ? "Unfollow" : "Follow");
    }

    private void updateFollowCounts() {
        int followers = FollowController.getFollowerCount(viewedUserId);
        int following = FollowController.getFollowingCount(viewedUserId);
        followersLabel.setText("Followers: " + followers);
        followingLabel.setText("Following: " + following);
    }


}
