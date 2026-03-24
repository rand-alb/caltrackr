/**
 * Entry Class
 * 
 * @author Dorian Le
 * @version 2025-11-22
 */
public class Entry
{
	private int date;
	private int totalCalories;
	private int goal;
	private boolean goalMet;
	private boolean bulking;

	// Class constructor
	public Entry(int date, int totalCalories, int goal, boolean bulking) {
		this.date = date;
		this.totalCalories = totalCalories;
		this.goal = goal;
		this.bulking = bulking;

		if (bulking)
		{
			this.goalMet = totalCalories >= goal;
		} else
		{
			this.goalMet = totalCalories <= goal;
		}
	}

	// Getters
	public int getDate()
	{
		return this.date;
	}

	public int getTotalCalories()
	{
		return this.totalCalories;
	}

	public int getGoal()
	{
		return this.goal;
	}

	public boolean getGoalMet()
	{
		return this.goalMet;
	}

	public boolean isBulking()
	{
		return this.bulking;
	}

}