import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Panel for setting daily calorie goal and tracking foods
 * 
 * @author Dorian Le, Maxim Ginsberg, Rand Albaroudi
 * @version 2025-11-23
 */
public class DailyGoalPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel parentPanel;
    private FoodEntryManager currentEntry;
    private LogHistory logHistory;
    private List<Food> availableFoods;

    private JTextField dateField;
    private JTextField goalField;
    private JComboBox<String> modeComboBox;
    private JTextArea summaryArea;
    private JPanel foodListPanel;

    public DailyGoalPanel(CardLayout layout, JPanel parent, LogHistory logHistory) {
	this.cardLayout = layout;
	this.parentPanel = parent;
	this.logHistory = logHistory;

	// Load available foods from CSV
	try {
	    availableFoods = FoodParser.parseFoods("foods_sample.csv");
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(this, "Error loading foods: " + e.getMessage(), "Error",
		    JOptionPane.ERROR_MESSAGE);
	    availableFoods = new java.util.ArrayList<>();
	}

	setLayout(new BorderLayout(10, 10));
	setBorder(new EmptyBorder(20, 20, 20, 20));

	// Header
	JLabel header = new JLabel("Food Entries");
	header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
	header.setHorizontalAlignment(JLabel.CENTER);
	add(header, BorderLayout.NORTH);

	// Main content
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

	// Setup section
	JPanel setupPanel = createSetupPanel();
	mainPanel.add(setupPanel, BorderLayout.NORTH);

	// Food selection section
	JPanel foodPanel = createFoodPanel();
	mainPanel.add(foodPanel, BorderLayout.CENTER);

	// Summary section
	JPanel summaryPanel = createSummaryPanel();
	mainPanel.add(summaryPanel, BorderLayout.SOUTH);

	add(mainPanel, BorderLayout.CENTER);

	// Bottom buttons
	JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	JButton backButton = new JButton("Back to Menu");
	JButton saveButton = new JButton("Save Entry to Log");

	backButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
	saveButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));

	backButton.addActionListener(e -> cardLayout.show(parentPanel, "menu"));
	saveButton.addActionListener(e -> saveEntryToLog());

	bottomPanel.add(backButton);
	bottomPanel.add(saveButton);
	add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSetupPanel() {
	JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
	panel.setBorder(BorderFactory.createTitledBorder("Entry Setup"));

	panel.add(new JLabel("Date (YYYYMMDD):"));
	dateField = new JTextField("20251123");
	panel.add(dateField);

	panel.add(new JLabel("Daily Calorie Goal:"));
	goalField = new JTextField("2000");
	panel.add(goalField);

	panel.add(new JLabel("Mode:"));
	modeComboBox = new JComboBox<>(new String[] { "Cutting (stay under)", "Bulking (exceed goal)" });
	panel.add(modeComboBox);

	JButton startButton = new JButton("Start New Entry");
	startButton.addActionListener(e -> startNewEntry());
	panel.add(new JLabel());
	panel.add(startButton);

	return panel;
    }

    private JPanel createFoodPanel() {
	JPanel panel = new JPanel(new BorderLayout(10, 10));
	panel.setBorder(BorderFactory.createTitledBorder("Browse & Add Foods"));

	// Create scrolling panel with all foods displayed
	JPanel availableFoodsPanel = new JPanel();
	availableFoodsPanel.setLayout(new BoxLayout(availableFoodsPanel, BoxLayout.Y_AXIS));
	availableFoodsPanel.setBackground(Color.WHITE);

	// Populate with all foods (similar to ScrollPannelGUI)
	for (Food food : availableFoods) {
	    JPanel foodCard = createAvailableFoodCard(food);
	    availableFoodsPanel.add(foodCard);
	    availableFoodsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	}

	JScrollPane availableScrollPane = new JScrollPane(availableFoodsPanel);
	// Increased from 350x300 to 450x450 for better visibility
	availableScrollPane.setPreferredSize(new Dimension(450, 450));
	availableScrollPane.setBorder(BorderFactory.createTitledBorder("Available Foods"));
	// Smooth scrolling
	availableScrollPane.getVerticalScrollBar().setUnitIncrement(16);

	// Food list display (your added foods)
	foodListPanel = new JPanel();
	foodListPanel.setLayout(new BoxLayout(foodListPanel, BoxLayout.Y_AXIS));
	foodListPanel.setBackground(Color.WHITE);

	JScrollPane addedScrollPane = new JScrollPane(foodListPanel);
	// Increased from 350x300 to 450x450 for better visibility
	addedScrollPane.setPreferredSize(new Dimension(450, 450));
	addedScrollPane.setBorder(BorderFactory.createTitledBorder("Added Foods"));
	// Smooth scrolling
	addedScrollPane.getVerticalScrollBar().setUnitIncrement(16);

	// Split view - available foods on left, added foods on right
	JPanel splitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
	splitPanel.add(availableScrollPane);
	splitPanel.add(addedScrollPane);

	panel.add(splitPanel, BorderLayout.CENTER);

	return panel;
    }

    private JPanel createAvailableFoodCard(Food food) {
	JPanel card = new JPanel(new BorderLayout(5, 5));
	card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
	card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
		new EmptyBorder(10, 10, 10, 10)));
	card.setBackground(Color.WHITE);

	// Food info section
	JPanel infoPanel = new JPanel();
	infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	infoPanel.setBackground(Color.WHITE);

	JLabel nameLabel = new JLabel(food.getName().toUpperCase());
	nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

	JLabel caloriesLabel = new JLabel(food.getCalories() + " calories");
	caloriesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	caloriesLabel.setForeground(Color.GRAY);

	// Add type indicator
	String type = "";
	Color typeColor = Color.BLACK;
	if (food instanceof FruitsAndVegetables) {
	    type = "Fruit/Vegetable";
	    typeColor = new Color(34, 139, 34);
	} else if (food instanceof Meat) {
	    type = "Meat";
	    typeColor = new Color(178, 34, 34);
	} else if (food instanceof OtherFood) {
	    type = "Other Food";
	    typeColor = new Color(255, 140, 0);
	}

	JLabel typeLabel = new JLabel(type);
	typeLabel.setFont(new Font("Arial", Font.ITALIC, 11));
	typeLabel.setForeground(typeColor);

	infoPanel.add(nameLabel);
	infoPanel.add(caloriesLabel);
	infoPanel.add(typeLabel);

	// Add button
	JButton addButton = new JButton("Add");
	addButton.setPreferredSize(new Dimension(70, 30));
	addButton.addActionListener(e -> addFoodFromCard(food));

	card.add(infoPanel, BorderLayout.CENTER);
	card.add(addButton, BorderLayout.EAST);

	return card;
    }

    private JPanel createSummaryPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(BorderFactory.createTitledBorder("Current Summary"));

	summaryArea = new JTextArea(6, 50);
	summaryArea.setEditable(false);
	summaryArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
	summaryArea.setText("Start a new entry to begin tracking.");

	JScrollPane scrollPane = new JScrollPane(summaryArea);
	panel.add(scrollPane, BorderLayout.CENTER);

	return panel;
    }

    private void startNewEntry() {
	try {
	    int date = Integer.parseInt(dateField.getText().trim());
	    int goal = Integer.parseInt(goalField.getText().trim());

	    // *** ADDED: Validate that calorie goal is positive ***
	    if (goal <= 0) {
		JOptionPane.showMessageDialog(this, "Calorie goal must be a positive number.\nYou entered: " + goal,
			"Invalid Goal", JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    // *** END OF ADDITION ***

	    boolean bulking = modeComboBox.getSelectedIndex() == 1;

	    currentEntry = new FoodEntryManager(date, goal, bulking);
	    foodListPanel.removeAll();
	    updateSummary();

	    JOptionPane.showMessageDialog(this, "New entry started for date " + date + "!\nCalorie Goal: " + goal,
		    "Success", JOptionPane.INFORMATION_MESSAGE);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "Please enter valid numbers for date and goal.", "Error",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    private void addFoodFromCard(Food food) {
	if (currentEntry == null) {
	    JOptionPane.showMessageDialog(this, "Please start a new entry first!", "Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}

	currentEntry.addFood(food);
	addFoodToDisplay(food);
	updateSummary();
    }

    private void addFoodToDisplay(Food food) {
	JPanel foodItem = new JPanel(new BorderLayout(5, 5));
	foodItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	foodItem.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	foodItem.setBackground(Color.WHITE);

	JLabel foodLabel = new JLabel(food.getName() + " - " + food.getCalories() + " cal");
	foodLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

	JButton removeButton = new JButton("Remove");
	removeButton.addActionListener(e -> {
	    currentEntry.removeFood(food);
	    foodListPanel.remove(foodItem);
	    foodListPanel.revalidate();
	    foodListPanel.repaint();
	    updateSummary();
	});

	foodItem.add(foodLabel, BorderLayout.CENTER);
	foodItem.add(removeButton, BorderLayout.EAST);

	foodListPanel.add(foodItem);
	foodListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	foodListPanel.revalidate();
	foodListPanel.repaint();
    }

    private void updateSummary() {
	if (currentEntry != null) {
	    summaryArea.setText(currentEntry.getSummary());
	}
    }

    private void saveEntryToLog() {
	if (currentEntry == null) {
	    JOptionPane.showMessageDialog(this, "No entry to save. Start a new entry first!", "Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}

	Entry entry = currentEntry.createEntry();
	logHistory.addEntry(entry);

	JOptionPane.showMessageDialog(this,
		"Entry saved to log!\n\nDate: " + entry.getDate() + "\nCalories: " + entry.getTotalCalories()
			+ "\nGoal: " + entry.getGoal() + "\nGoal Met: " + (entry.getGoalMet() ? "Yes" : "No"),
		"Success", JOptionPane.INFORMATION_MESSAGE);

	// Reset for new entry
	currentEntry = null;
	foodListPanel.removeAll();
	foodListPanel.revalidate();
	foodListPanel.repaint();
	summaryArea.setText("Entry saved! Start a new entry to continue tracking.");
    }
}
