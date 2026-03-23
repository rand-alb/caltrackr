import java.util.ArrayList;
import java.util.List;

/**
 * Manages a daily food entry by tracking foods added/removed and calculating
 * total calories. Can be used to create Entry objects for logging.
 * 
 * @author Maxim Ginsberg
 * @version 2025-11-23
 */
public class FoodEntryManager {
    private int date;
    private ArrayList<Food> foodsList;
    private int totalCalories;
    private int dailyGoal;
    private boolean isBulking;

    /**
     * Constructor for a new food entry manager
     * 
     * @param date      The date for this entry (e.g., 20251123)
     * @param dailyGoal Daily calorie goal (must be positive)
     * @param isBulking True if bulking (goal is to exceed calories), false if
     *                  cutting
     * @throws IllegalArgumentException if dailyGoal is negative or zero
     */
    public FoodEntryManager(int date, int dailyGoal, boolean isBulking) {
	if (dailyGoal <= 0) {
	    throw new IllegalArgumentException("Daily calorie goal must be positive. Provided: " + dailyGoal);
	}

	this.date = date;
	this.dailyGoal = dailyGoal;
	this.isBulking = isBulking;
	this.foodsList = new ArrayList<>();
	this.totalCalories = 0;
    }

    /**
     * Add a food item to the entry
     * 
     * @param food The food to add
     * @return true if added successfully
     */
    public boolean addFood(Food food) {
	if (food == null) {
	    return false;
	}

	foodsList.add(food);
	totalCalories += food.getCalories();
	return true;
    }

    /**
     * Remove a food item by index
     * 
     * @param index The index of the food to remove
     * @return true if removed successfully, false if index invalid
     */
    public boolean removeFoodByIndex(int index) {
	if (index < 0 || index >= foodsList.size()) {
	    return false;
	}

	Food removed = foodsList.remove(index);
	totalCalories -= removed.getCalories();
	return true;
    }

    /**
     * Remove a specific food item (first occurrence)
     * 
     * @param food The food object to remove
     * @return true if found and removed, false otherwise
     */
    public boolean removeFood(Food food) {
	if (foodsList.remove(food)) {
	    totalCalories -= food.getCalories();
	    return true;
	}
	return false;
    }

    /**
     * Remove all foods with a specific name
     * 
     * @param foodName The name of foods to remove
     * @return number of items removed
     */
    public int removeFoodsByName(String foodName) {
	int removed = 0;
	for (int i = foodsList.size() - 1; i >= 0; i--) {
	    if (foodsList.get(i).getName().equalsIgnoreCase(foodName)) {
		totalCalories -= foodsList.get(i).getCalories();
		foodsList.remove(i);
		removed++;
	    }
	}
	return removed;
    }

    /**
     * Clear all foods from the entry
     */
    public void clearAllFoods() {
	foodsList.clear();
	totalCalories = 0;
    }

    /**
     * Get a copy of the foods list (prevents external modification)
     * 
     * @return Copy of the foods list
     */
    public List<Food> getFoodsList() {
	return new ArrayList<>(foodsList);
    }

    /**
     * Get total number of food items
     * 
     * @return Number of foods in entry
     */
    public int getFoodCount() {
	return foodsList.size();
    }

    /**
     * Get current total calories
     * 
     * @return Total calories from all foods
     */
    public int getTotalCalories() {
	return totalCalories;
    }

    /**
     * Get the daily goal
     * 
     * @return Daily calorie goal
     */
    public int getDailyGoal() {
	return dailyGoal;
    }

    /**
     * Set a new daily goal
     * 
     * @param dailyGoal New goal value (must be positive)
     * @throws IllegalArgumentException if dailyGoal is negative or zero
     */
    public void setDailyGoal(int dailyGoal) {
	if (dailyGoal <= 0) {
	    throw new IllegalArgumentException("Daily calorie goal must be positive. Provided: " + dailyGoal);
	}
	this.dailyGoal = dailyGoal;
    }

    /**
     * Get the date
     * 
     * @return Date of this entry
     */
    public int getDate() {
	return date;
    }

    /**
     * Check if currently bulking
     * 
     * @return true if bulking, false if cutting
     */
    public boolean isBulking() {
	return isBulking;
    }

    /**
     * Set bulking status
     * 
     * @param isBulking true for bulking, false for cutting
     */
    public void setBulking(boolean isBulking) {
	this.isBulking = isBulking;
    }

    /**
     * Check if goal has been met based on current mode
     * 
     * @return true if goal is met
     */
    public boolean isGoalMet() {
	if (isBulking) {
	    return totalCalories > dailyGoal;
	} else {
	    return totalCalories < dailyGoal;
	}
    }

    /**
     * Get calories remaining to meet goal (positive = need more, negative =
     * exceeded)
     * 
     * @return Difference between goal and current calories
     */
    public int getCaloriesRemaining() {
	if (isBulking) {
	    return dailyGoal - totalCalories; // need this many more
	} else {
	    return dailyGoal - totalCalories; // this many under goal
	}
    }

    /**
     * Create an Entry object from current state
     * 
     * @return Entry object representing this food entry
     */
    public Entry createEntry() {
	return new Entry(date, totalCalories, dailyGoal, isBulking);
    }

    /**
     * Get a summary of the current entry
     * 
     * @return String summary with all relevant info
     */
    public String getSummary() {
	StringBuilder summary = new StringBuilder();
	summary.append("=== Entry Summary for ").append(date).append(" ===\n");
	summary.append("Mode: ").append(isBulking ? "Bulking" : "Cutting").append("\n");
	summary.append("Daily Goal: ").append(dailyGoal).append(" calories\n");
	summary.append("Current Total: ").append(totalCalories).append(" calories\n");
	summary.append("Goal Met: ").append(isGoalMet() ? "Yes" : "No").append("\n");

	int remaining = getCaloriesRemaining();
	if (isBulking) {
	    if (remaining > 0) {
		summary.append("Need ").append(remaining).append(" more calories\n");
	    } else {
		summary.append("Exceeded goal by ").append(Math.abs(remaining)).append(" calories\n");
	    }
	} else {
	    if (remaining > 0) {
		summary.append("Can eat ").append(remaining).append(" more calories\n");
	    } else {
		summary.append("Exceeded goal by ").append(Math.abs(remaining)).append(" calories\n");
	    }
	}

	summary.append("\nFoods Logged (").append(foodsList.size()).append(" items):\n");
	for (int i = 0; i < foodsList.size(); i++) {
	    Food food = foodsList.get(i);
	    summary.append(i + 1).append(". ").append(food.getName()).append(" - ").append(food.getCalories())
		    .append(" cal\n");
	}

	return summary.toString();
    }

    /**
     * Get detailed view with all food information
     * 
     * @return Detailed string of all foods
     */
    public String getDetailedView() {
	StringBuilder view = new StringBuilder();
	view.append("=== Detailed Food Entry for ").append(date).append(" ===\n\n");

	for (int i = 0; i < foodsList.size(); i++) {
	    view.append("[").append(i).append("] ").append(foodsList.get(i).toString()).append("\n");
	}

	view.append("\nTotal Calories: ").append(totalCalories).append(" / ").append(dailyGoal).append("\n");

	return view.toString();
    }

    @Override
    public String toString() {
	return "FoodEntryManager [date=" + date + ", totalCalories=" + totalCalories + ", dailyGoal=" + dailyGoal
		+ ", isBulking=" + isBulking + ", foodCount=" + foodsList.size() + "]";
    }
}
