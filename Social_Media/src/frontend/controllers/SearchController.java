@FXML private TextField searchTextField;
@FXML private ListView<String> resultsListView;
@FXML private RadioButton searchUsersRadio;
@FXML private RadioButton searchPostsRadio;

@FXML
public void onSearch() {
    String keyword = searchTextField.getText().trim();
    if (keyword.isEmpty()) return;

    resultsListView.getItems().clear();

    if (searchUsersRadio.isSelected()) {
        List<String> users = SearchController.searchUsers(keyword);
        resultsListView.getItems().addAll(users);
    } else if (searchPostsRadio.isSelected()) {
        List<String> posts = SearchController.searchPosts(keyword);
        resultsListView.getItems().addAll(posts);
    }
}
