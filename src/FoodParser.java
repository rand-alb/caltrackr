
/**
 * Reads a CSV file and builds Food objects from it.
 * Figures out which subclass to use based on the category column.
 * @author Rand Albaroudi
 * @version 2025-11-22
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodParser
{
	// takes a csv file path and returns a list of Food objects
	public static List<Food> parseFoods(String csvFile) throws IOException
	{
		// list where all the foods will be stored
		List<Food> foods = new ArrayList<>();

		// try-with-resources so the file closes automatically
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
		{
			// first line is the header so just skip it
			String header = br.readLine();

			String line;
			// read each line until there are no more
			while ((line = br.readLine()) != null)
			{
				// skip lines that are just blank
				if (line.trim().isEmpty())
				{
					continue;
				}

				// split the line by commas, keep empty strings too
				String[] parts = line.split(",", -1);
				if (parts.length < 3) {
					System.out.println("Skipping malformed line (not enough columns): " + line);
					continue;
				}
				String name = parts[0].trim();
				String category = parts[1].trim().toLowerCase();

				try {
					// if calories column is empty, just use 0
				int calories = !parts[2].trim().isEmpty() ? Integer.parseInt(parts[2].trim()) : 0;

				// pick what kind of Food to make based on the category
				switch (category)
				{
				case "fruit":
				case "vegetable":
				{
					if (parts.length < 7) {
						System.out.println("Skipping malformed fruit/veg line: " + line);
						break;
					}
					int natSugar = !parts[3].trim().isEmpty() ? Integer.parseInt(parts[3].trim()) : 0;
					boolean vitaminRich = !parts[4].trim().isEmpty() && Boolean.parseBoolean(parts[4].trim());
					// vitamins are separated by | so split that, or empty array if none
					String[] vitamins = parts[5].trim().isEmpty() ? new String[0] : parts[5].split("\\|");
					int carbs = !parts[6].trim().isEmpty() ? Integer.parseInt(parts[6].trim()) : 0;

					// build a FruitsAndVegetables object and add it
					FruitsAndVegetables fv = new FruitsAndVegetables(name, calories, natSugar, vitaminRich, vitamins,
							carbs);
					foods.add(fv);
					break;
				}
				case "meat":
				{
					if (parts.length < 10) {
						System.out.println("Skipping malformed meat line: " + line);
						break;
					}
					boolean redMeat = !parts[7].trim().isEmpty() && Boolean.parseBoolean(parts[7].trim());
					int protein = !parts[8].trim().isEmpty() ? Integer.parseInt(parts[8].trim()) : 0;
					int natFat = !parts[9].trim().isEmpty() ? Integer.parseInt(parts[9].trim()) : 0;

					// build a Meat object and add it
					Meat meat = new Meat(name, calories, redMeat, protein, natFat);
					foods.add(meat);
					break;
				}
				case "otherfood":
				{
					if (parts.length < 12) {
						System.out.println("Skipping malformed otherfood line: " + line);
						break;
					}
					int carbs = !parts[7].trim().isEmpty() ? Integer.parseInt(parts[7].trim()) : 0;
					int fat = !parts[10].trim().isEmpty() ? Integer.parseInt(parts[10].trim()) : 0;
					int sugar = !parts[11].trim().isEmpty() ? Integer.parseInt(parts[11].trim()) : 0;

					// build an OtherFood object and add it
					OtherFood otherFood = new OtherFood(name, calories, carbs, fat, sugar);
					foods.add(otherFood);
					break;
				}
				default:
					// if the category isn't one we know, just mention it and skip
					System.out.println("Skipping unknown category: " + category);
				}
				} catch (NumberFormatException e) {
					System.out.println("Skipping line due to numerical parsing error: " + line);
				}
			}
		}

		// send back the full list of foods we parsed
		return foods;
	}

	// read the sample csv and print everything
	public static void main(String[] args) throws IOException
	{
		List<Food> foods = parseFoods("foods_sample.csv");
		for (Food food : foods)
		{
			System.out.println(food);
		}
	}
}
