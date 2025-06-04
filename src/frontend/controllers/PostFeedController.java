// Pseudo-code for a post item container

VBox postBox = new VBox();

Label postContent = new Label(post.getContent());

Button likeButton = new Button("Like (" + LikesController.countLikes(post.getId()) + ")");
likeButton.setOnAction(e -> {
    boolean liked = LikesController.toggleLike(currentUserId, post.getId());
    int newCount = LikesController.countLikes(post.getId());
    likeButton.setText((liked ? "Unlike" : "Like") + " (" + newCount + ")");
});

VBox commentsBox = new VBox();
List<String> comments = CommentsController.getComments(post.getId());
for (String comment : comments) {
    commentsBox.getChildren().add(new Label(comment));
}

TextField commentField = new TextField();
commentField.setPromptText("Write a comment...");

Button submitComment = new Button("Comment");
submitComment.setOnAction(e -> {
    String commentText = commentField.getText();
    if (!commentText.isEmpty()) {
        CommentsController.addComment(currentUserId, post.getId(), commentText);
        commentsBox.getChildren().add(new Label("You: " + commentText));
        commentField.clear();
    }
});

postBox.getChildren().addAll(postContent, likeButton, commentsBox, commentField, submitComment);
