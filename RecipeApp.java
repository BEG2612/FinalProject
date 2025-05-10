import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// JavaFX UI Controls
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// JavaFX Layouts
import javafx.scene.layout.GridPane;

// JavaFX Geometry
import javafx.geometry.Pos;


public class RecipeApp extends Application {

    private RecipeManager recipeManager = new RecipeManager();
    private ObservableList<Recipe> observableRecipes = FXCollections.observableArrayList();
    private ListView<Recipe> recipeListView = new ListView<>();
    private TextArea recipeDetailsArea = new TextArea();

    private String formatRecipeDetails(Recipe recipe) {
        return "Title: " + recipe.title + "\n"
            + "Ingredients: " + recipe.ingredients + "\n"
            + "Instructions: " + recipe.instructions + "\n"
            + "Rating: " + recipe.rating + "/5\n"
            + (recipe instanceof MealRecipe ? (
                "Prep Time: " + ((MealRecipe) recipe).getPrepTime() + " mins\n"
            + "Calories: " + ((MealRecipe) recipe).getCalories() + "\n"
            ) : "");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Baker's Buddy");
        primaryStage.getIcons().add(new Image("file:icon.jpg"));

        // Layouts
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:rgb(247, 203, 93);");
        VBox rightPane = new VBox(10);
        rightPane.setPadding(new Insets(10));
        rightPane.setStyle("-fx-background-color:rgb(247, 203, 93); -fx-border-color:rgb(99, 59, 59); -fx-border-width: 1;");

        // ListView
        recipeListView.setItems(observableRecipes);
        recipeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);
                if (empty || recipe == null) {
                    setText(null);
                } else {
                    setText(recipe.title);
                }
            }
        });
        recipeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedRecipe) -> {
            if (selectedRecipe != null) {
                recipeDetailsArea.setText(formatRecipeDetails(selectedRecipe));
            }
        });       

        recipeDetailsArea.setEditable(false);
        recipeDetailsArea.setWrapText(true);
        recipeDetailsArea.setPrefHeight(150);

        VBox centerPane = new VBox(10, recipeListView, recipeDetailsArea);
        centerPane.setPadding(new Insets(10));
        root.setCenter(centerPane);



        // Add, Edit, or Remove Recipe Buttons
        Button addButton = new Button("    Add Recipe   ");
        Button removeButton = new Button("Remove Recipe");
        Button editButton = new Button("    Edit Recipe   ");
        addButton.setStyle("-fx-background-color: #6495ed; -fx-text-fill: white;");
        addButton.setOnAction(e -> showAddRecipeDialog());
        removeButton.setStyle("-fx-background-color:rgb(255, 0, 0); -fx-text-fill: white;");
        removeButton.setOnAction(e -> {
            Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                recipeManager.deleteRecipe(selected);
                observableRecipes.remove(selected);
                recipeDetailsArea.clear();
            } else {
                showError("Please select a recipe to delete.");
            }
        });        
        editButton.setStyle("-fx-background-color:rgb(121, 166, 7); -fx-text-fill: black;");
        editButton.setOnAction(e -> {
            Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected instanceof MealRecipe) {
                showEditRecipeDialog((MealRecipe) selected);
            } else if (selected == null) {
                showError("Please select a recipe to edit.");
            }
        });
        
        

        rightPane.getChildren().addAll(addButton, editButton, removeButton);

        root.setRight(rightPane);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddRecipeDialog() {
        Dialog<MealRecipe> dialog = new Dialog<>();
        dialog.setTitle("Add a New Recipe");

        // Add icon to Window
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons()
            .add(new Image("file:icon.jpg"));

        // Fields
        TextField titleField = new TextField();
        TextField ingredientsField = new TextField();
        TextField instructionsField = new TextField();
        TextField prepTimeField = new TextField();
        TextField caloriesField = new TextField();
        TextField ratingField = new TextField();

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:rgb(247, 203, 93);");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Ingredients:"), 0, 1);
        grid.add(ingredientsField, 1, 1);
        grid.add(new Label("Instructions:"), 0, 2);
        grid.add(instructionsField, 1, 2);
        grid.add(new Label("Prep Time (mins):"), 0, 3);
        grid.add(prepTimeField, 1, 3);
        grid.add(new Label("Calories:"), 0, 4);
        grid.add(caloriesField, 1, 4);
        grid.add(new Label("Rating (0-5):"), 0, 5);
        grid.add(ratingField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Buttons
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    // Parse fields safely
                    String title = titleField.getText();
                    String ingredients = ingredientsField.getText();
                    String instructions = instructionsField.getText();
                    int prepTime = Integer.parseInt(prepTimeField.getText());
                    int calories = Integer.parseInt(caloriesField.getText());
                    int rating = Integer.parseInt(ratingField.getText());
                    if (rating < 0 || rating > 5) {
                    showError("Rating must be between 0 and 5.");
                    return null;
                    }


                    MealRecipe newRecipe = new MealRecipe(
                            title, ingredients, instructions, rating, prepTime, calories
                    );
                    return newRecipe;
                } catch (NumberFormatException e) {
                    showError("Please enter valid numbers for Prep Time, Calories, and Rating.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(recipe -> {
            recipeManager.addRecipe(recipe);
            observableRecipes.add(recipe);
        });
    }

    private void showEditRecipeDialog(MealRecipe recipe) {
        Dialog<MealRecipe> dialog = new Dialog<>();
        dialog.setTitle("Edit Recipe");
        
        // Add icon to Window
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons()
            .add(new Image("file:icon.jpg"));
    
    
        TextField titleField = new TextField(recipe.title);
        TextField ingredientsField = new TextField(recipe.ingredients);
        TextField instructionsField = new TextField(recipe.instructions);
        TextField prepTimeField = new TextField(String.valueOf(recipe.getPrepTime()));
        TextField caloriesField = new TextField(String.valueOf(recipe.getCalories()));
        TextField ratingField = new TextField(String.valueOf(recipe.rating));
    
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:rgb(247, 203, 93);");
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
    
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Ingredients:"), 0, 1);
        grid.add(ingredientsField, 1, 1);
        grid.add(new Label("Instructions:"), 0, 2);
        grid.add(instructionsField, 1, 2);
        grid.add(new Label("Prep Time (mins):"), 0, 3);
        grid.add(prepTimeField, 1, 3);
        grid.add(new Label("Calories:"), 0, 4);
        grid.add(caloriesField, 1, 4);
        grid.add(new Label("Rating (0-5):"), 0, 5);
        grid.add(ratingField, 1, 5);
    
        dialog.getDialogPane().setContent(grid);
    
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    recipe.title = titleField.getText();
                    recipe.ingredients = ingredientsField.getText();
                    recipe.instructions = instructionsField.getText();
                    recipe.rating = Integer.parseInt(ratingField.getText());
                    recipe.setCalories(Integer.parseInt(caloriesField.getText()));
                    recipe.setPrepTime(Integer.parseInt(prepTimeField.getText()));
                    return recipe;
                } catch (NumberFormatException e) {
                    showError("Please enter valid numbers for Prep Time, Calories, and Rating.");
                }
            }
            return null;
        });
    
        dialog.showAndWait().ifPresent(updated -> {
            recipeListView.refresh();
            recipeDetailsArea.setText(formatRecipeDetails(updated));
        });
    }    

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}