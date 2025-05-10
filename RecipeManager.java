import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    private List<Recipe> recipes;
    private String filePath; // Optional: used for file storage

    public RecipeManager() {
        recipes = new ArrayList<>();
        filePath = "recipes.txt"; // Default path
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    public void editRecipe(int index, Recipe newRecipe) {
        if (index >= 0 && index < recipes.size()) {
            recipes.set(index, newRecipe);
        }
    }

    public List<Recipe> searchRecipe(String keyword) {
        List<Recipe> results = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.title.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }

    public List<Recipe> getAllRecipes() {
        return recipes;
    }
}