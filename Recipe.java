public abstract class Recipe {
    protected String title;
    protected String ingredients;
    protected String instructions;
    protected int rating;

    public Recipe(String title, String ingredients, String instructions,
                  int rating) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.rating = rating;
    }

    public abstract void displayRecipe();
}