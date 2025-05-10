public class MealRecipe extends Recipe {
    private int prepTime;
    private int calories;

    public MealRecipe(String title, String ingredients, String instructions,
                        int rating, int prepTime, int calories) {
        super(title, ingredients, instructions, rating);
        this.prepTime = prepTime;
        this.calories = calories;
    }

    @Override
    public void displayRecipe() {
        System.out.println("Meal: " + title);
        System.out.println("Ingredients: " + ingredients);
        System.out.println("Instructions: " + instructions);
        System.out.println("Prep Time: " + prepTime + " mins");
        System.out.println("Calories: " + calories);
        System.out.println("Rating: " + rating + "/5");
        System.out.println("---------------------------");
    }
    public int getPrepTime() {
        return prepTime;
    }
    
    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
    
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }    
}
