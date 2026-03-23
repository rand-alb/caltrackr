import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Panel that displays food items from a CSV file. Can be embedded in other
 * containers.
 * 
 * @author Maxim Ginsberg
 * @version 2025-11-23
 */
public class FoodDatabaseViewer extends JPanel {
    private static final long serialVersionUID = 1L;

    private JPanel foodPanel; // panel where food item panels go
    private JScrollPane scrollPane; // scrollable container for foodPanel
    private List<Food> foods; // list of food objects loaded from CSV

    public FoodDatabaseViewer(String csvFile) throws IOException {
	// Load food data from CSV file
	foods = FoodParser.parseFoods(csvFile);

	// Set up this panel with BorderLayout
	setLayout(new BorderLayout());

	// Make a panel and lay out components vertically
	foodPanel = new JPanel();
	foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
	foodPanel.setBackground(Color.WHITE);
	foodPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // padding around edges

	// Fill foodPanel with individual panels for each food
	populateFoodPanel();

	// Wrap foodPanel in a scroll pane so it scrolls vertically
	scrollPane = new JScrollPane(foodPanel);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	// Make scrolling smoother and faster
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);

	// Set minimum size for the scroll pane to ensure it displays properly
	scrollPane.setPreferredSize(new Dimension(750, 500));

	// Add a header panel up top showing total items count
	JPanel headerPanel = createHeaderPanel();
	add(headerPanel, BorderLayout.NORTH);

	// Add the scroll pane to the center
	add(scrollPane, BorderLayout.CENTER);
    }

    // Creates the top header with title and number of items
    private JPanel createHeaderPanel() {
	JPanel header = new JPanel();
	header.setBackground(new Color(70, 130, 180)); // steel blue background
	header.setBorder(new EmptyBorder(15, 10, 15, 10));

	JLabel titleLabel = new JLabel("Food Database - " + foods.size() + " items");
	titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	titleLabel.setForeground(Color.WHITE);
	header.add(titleLabel);

	return header;
    }

    // Goes through all foods and adds a panel for each one into foodPanel
    private void populateFoodPanel() {
	for (Food food : foods) {
	    JPanel itemPanel = createFoodItemPanel(food);
	    foodPanel.add(itemPanel);
	    foodPanel.add(Box.createRigidArea(new Dimension(0, 10))); // space between items
	}
    }

    // Creates a panel for one food, showing its name, type, and details
    private JPanel createFoodItemPanel(Food food) {
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout(10, 10));
	panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
		new EmptyBorder(15, 15, 15, 15)));
	panel.setBackground(Color.WHITE);
	panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // max height, full width

	// Header with food name on left and type on right
	JPanel headerSection = new JPanel(new BorderLayout());
	headerSection.setBackground(Color.WHITE);

	// Show the name
	JLabel nameLabel = new JLabel(food.getName());
	nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
	headerSection.add(nameLabel, BorderLayout.WEST);

	JLabel typeLabel = new JLabel(getFoodType(food));
	typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	typeLabel.setForeground(getTypeColor(food));
	headerSection.add(typeLabel, BorderLayout.EAST);

	panel.add(headerSection, BorderLayout.NORTH);

	// Text area below with the food's nutritional details
	JTextArea detailsArea = new JTextArea(getFoodDetails(food));
	detailsArea.setFont(new Font("Arial", Font.PLAIN, 13));
	detailsArea.setEditable(false);
	detailsArea.setBackground(Color.WHITE);
	detailsArea.setLineWrap(true);
	detailsArea.setWrapStyleWord(true);

	panel.add(detailsArea, BorderLayout.CENTER);

	return panel;
    }

    // Returns a string describing the type based on food subclass
    private String getFoodType(Food food) {
	if (food instanceof FruitsAndVegetables) {
	    return "FRUIT/VEGETABLE";
	} else if (food instanceof Meat) {
	    return "MEAT";
	} else if (food instanceof OtherFood) {
	    return "OTHER FOOD";
	}
	return "FOOD";
    }

    // Returns a color matching the food type for styling
    private Color getTypeColor(Food food) {
	if (food instanceof FruitsAndVegetables) {
	    return new Color(34, 139, 34); // Forest green for fruits/vegs
	} else if (food instanceof Meat) {
	    return new Color(178, 34, 34); // Firebrick red for meats
	} else if (food instanceof OtherFood) {
	    return new Color(255, 140, 0); // Dark orange for others
	}
	return Color.BLACK;
    }

    // Builds the string of details depending on the food type
    private String getFoodDetails(Food food) {
	StringBuilder details = new StringBuilder();
	details.append("Calories: ").append(food.getCalories()).append(" kcal\n");

	if (food instanceof FruitsAndVegetables) {
	    FruitsAndVegetables fv = (FruitsAndVegetables) food;
	    details.append("Natural Sugar: ").append(fv.getNatSugar()).append("g\n");
	    details.append("Carbohydrates: ").append(fv.getCarbs()).append("g\n");
	    details.append("Vitamin Rich: ").append(fv.isVitaminRich() ? "Yes" : "No").append("\n");
	    if (fv.getVitamin().length > 0) {
		details.append("Vitamins: ").append(String.join(", ", fv.getVitamin()));
	    }
	} else if (food instanceof Meat) {
	    Meat meat = (Meat) food;
	    details.append("Protein: ").append(meat.getProtein()).append("g\n");
	    details.append("Natural Fat: ").append(meat.getNatFat()).append("g\n");
	    details.append("Red Meat: ").append(meat.isRedMeat() ? "Yes" : "No");
	} else if (food instanceof OtherFood) {
	    OtherFood other = (OtherFood) food;
	    details.append("Carbohydrates: ").append(other.getCarbs()).append("g\n");
	    details.append("Fat: ").append(other.getFat()).append("g\n");
	    details.append("Sugar: ").append(other.getSugar()).append("g");
	}

	return details.toString();
    }
}
