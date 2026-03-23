import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Personal panel - Weight tracking and goal management
 * 
 * @author Dorian Le
 * @version 2025-11-23
 */
public class Personal extends JPanel {
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel parentPanel;

    // Weight tracking variables
    private double currentWeight;
    private double goalWeight;
    private boolean hasCurrentWeight;
    private boolean hasGoalWeight;

    // UI Components
    private JLabel currentWeightDisplay;
    private JLabel goalWeightDisplay;
    private JLabel differenceDisplay;
    private JTextField weightInputField;
    private JTextField goalInputField;

    public Personal(CardLayout layout, JPanel parent) {
	this.cardLayout = layout;
	this.parentPanel = parent;
	this.currentWeight = 0.0;
	this.goalWeight = 0.0;
	this.hasCurrentWeight = false;
	this.hasGoalWeight = false;

	setLayout(new BorderLayout(10, 10));
	setBorder(new EmptyBorder(20, 20, 20, 20));

	// Header
	JLabel header = new JLabel("Personal Weight Tracker");
	header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
	header.setHorizontalAlignment(JLabel.CENTER);
	add(header, BorderLayout.NORTH);

	// Main content panel
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

	// Display section
	JPanel displayPanel = createDisplayPanel();
	mainPanel.add(displayPanel, BorderLayout.NORTH);

	// Input section
	JPanel inputPanel = createInputPanel();
	mainPanel.add(inputPanel, BorderLayout.CENTER);

	add(mainPanel, BorderLayout.CENTER);

	// Bottom buttons
	JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	JButton backButton = new JButton("Back to Menu");
	backButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
	backButton.addActionListener(e -> cardLayout.show(parentPanel, "menu"));
	bottomPanel.add(backButton);

	add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createDisplayPanel() {
	JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
	panel.setBorder(BorderFactory.createTitledBorder("Current Status"));
	panel.setBackground(new Color(240, 248, 255));
	panel.setPreferredSize(new Dimension(700, 150));

	// Current weight display
	currentWeightDisplay = new JLabel("Current Weight: Not set");
	currentWeightDisplay.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
	currentWeightDisplay.setBorder(new EmptyBorder(10, 20, 5, 20));

	// Goal weight display
	goalWeightDisplay = new JLabel("Goal Weight: Not set");
	goalWeightDisplay.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
	goalWeightDisplay.setBorder(new EmptyBorder(5, 20, 5, 20));

	// Difference display
	differenceDisplay = new JLabel("Difference: --");
	differenceDisplay.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
	differenceDisplay.setBorder(new EmptyBorder(5, 20, 10, 20));

	panel.add(currentWeightDisplay);
	panel.add(goalWeightDisplay);
	panel.add(differenceDisplay);

	return panel;
    }

    private JPanel createInputPanel() {
	JPanel panel = new JPanel(new BorderLayout(10, 10));

	// Current weight section
	JPanel currentWeightPanel = new JPanel(new BorderLayout(10, 10));
	currentWeightPanel.setBorder(BorderFactory.createTitledBorder("Manage Current Weight"));
	currentWeightPanel.setPreferredSize(new Dimension(700, 120));

	JPanel currentWeightInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
	JLabel currentWeightLabel = new JLabel("Weight (lbs):");
	currentWeightLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

	weightInputField = new JTextField(10);
	weightInputField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

	JButton setWeightButton = new JButton("Set Current Weight");
	setWeightButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	setWeightButton.addActionListener(e -> setCurrentWeight());

	JButton editWeightButton = new JButton("Edit Current Weight");
	editWeightButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	editWeightButton.addActionListener(e -> editCurrentWeight());

	currentWeightInputPanel.add(currentWeightLabel);
	currentWeightInputPanel.add(weightInputField);
	currentWeightInputPanel.add(setWeightButton);
	currentWeightInputPanel.add(editWeightButton);

	currentWeightPanel.add(currentWeightInputPanel, BorderLayout.CENTER);

	// Goal weight section
	JPanel goalWeightPanel = new JPanel(new BorderLayout(10, 10));
	goalWeightPanel.setBorder(BorderFactory.createTitledBorder("Manage Goal Weight"));
	goalWeightPanel.setPreferredSize(new Dimension(700, 120));

	JPanel goalWeightInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
	JLabel goalWeightLabel = new JLabel("Goal (lbs):");
	goalWeightLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

	goalInputField = new JTextField(10);
	goalInputField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

	JButton setGoalButton = new JButton("Set Goal Weight");
	setGoalButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	setGoalButton.addActionListener(e -> setGoalWeight());

	JButton editGoalButton = new JButton("Edit Goal Weight");
	editGoalButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	editGoalButton.addActionListener(e -> editGoalWeight());

	goalWeightInputPanel.add(goalWeightLabel);
	goalWeightInputPanel.add(goalInputField);
	goalWeightInputPanel.add(setGoalButton);
	goalWeightInputPanel.add(editGoalButton);

	goalWeightPanel.add(goalWeightInputPanel, BorderLayout.CENTER);

	// Combine both sections
	JPanel combinedPanel = new JPanel(new GridLayout(2, 1, 10, 10));
	combinedPanel.add(currentWeightPanel);
	combinedPanel.add(goalWeightPanel);

	panel.add(combinedPanel, BorderLayout.NORTH);

	return panel;
    }

    private void setCurrentWeight() {
	try {
	    String input = weightInputField.getText().trim();
	    if (input.isEmpty()) {
		JOptionPane.showMessageDialog(this, "Please enter a weight value.", "Error", JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double weight = Double.parseDouble(input);
	    if (weight <= 0) {
		JOptionPane.showMessageDialog(this, "Weight must be a positive number.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    currentWeight = weight;
	    hasCurrentWeight = true;
	    updateDisplay();
	    weightInputField.setText("");

	    JOptionPane.showMessageDialog(this, "Current weight set to " + weight + " lbs", "Success",
		    JOptionPane.INFORMATION_MESSAGE);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void editCurrentWeight() {
	if (!hasCurrentWeight) {
	    JOptionPane.showMessageDialog(this, "No current weight set. Please set a weight first.", "Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}

	try {
	    String input = weightInputField.getText().trim();
	    if (input.isEmpty()) {
		JOptionPane.showMessageDialog(this, "Please enter a new weight value.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double weight = Double.parseDouble(input);
	    if (weight <= 0) {
		JOptionPane.showMessageDialog(this, "Weight must be a positive number.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double oldWeight = currentWeight;
	    currentWeight = weight;
	    updateDisplay();
	    weightInputField.setText("");

	    JOptionPane.showMessageDialog(this,
		    "Current weight updated!\nOld: " + oldWeight + " lbs\nNew: " + weight + " lbs", "Success",
		    JOptionPane.INFORMATION_MESSAGE);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void setGoalWeight() {
	try {
	    String input = goalInputField.getText().trim();
	    if (input.isEmpty()) {
		JOptionPane.showMessageDialog(this, "Please enter a goal weight value.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double weight = Double.parseDouble(input);
	    if (weight <= 0) {
		JOptionPane.showMessageDialog(this, "Goal weight must be a positive number.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    goalWeight = weight;
	    hasGoalWeight = true;
	    updateDisplay();
	    goalInputField.setText("");

	    JOptionPane.showMessageDialog(this, "Goal weight set to " + weight + " lbs", "Success",
		    JOptionPane.INFORMATION_MESSAGE);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void editGoalWeight() {
	if (!hasGoalWeight) {
	    JOptionPane.showMessageDialog(this, "No goal weight set. Please set a goal first.", "Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}

	try {
	    String input = goalInputField.getText().trim();
	    if (input.isEmpty()) {
		JOptionPane.showMessageDialog(this, "Please enter a new goal weight value.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double weight = Double.parseDouble(input);
	    if (weight <= 0) {
		JOptionPane.showMessageDialog(this, "Goal weight must be a positive number.", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    double oldGoal = goalWeight;
	    goalWeight = weight;
	    updateDisplay();
	    goalInputField.setText("");

	    JOptionPane.showMessageDialog(this,
		    "Goal weight updated!\nOld: " + oldGoal + " lbs\nNew: " + weight + " lbs", "Success",
		    JOptionPane.INFORMATION_MESSAGE);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void updateDisplay() {
	// Update current weight display
	if (hasCurrentWeight) {
	    currentWeightDisplay.setText("Current Weight: " + String.format("%.1f", currentWeight) + " lbs");
	} else {
	    currentWeightDisplay.setText("Current Weight: Not set");
	}

	// Update goal weight display
	if (hasGoalWeight) {
	    goalWeightDisplay.setText("Goal Weight: " + String.format("%.1f", goalWeight) + " lbs");
	} else {
	    goalWeightDisplay.setText("Goal Weight: Not set");
	}

	// Update difference display
	if (hasCurrentWeight && hasGoalWeight) {
	    double difference = currentWeight - goalWeight;
	    String differenceText;
	    Color differenceColor;

	    if (difference > 0) {
		// Need to lose weight
		differenceText = "Difference: " + String.format("%.1f", Math.abs(difference)) + " lbs to lose";
		differenceColor = new Color(178, 34, 34); // Red
	    } else if (difference < 0) {
		// Need to gain weight
		differenceText = "Difference: " + String.format("%.1f", Math.abs(difference)) + " lbs to gain";
		differenceColor = new Color(255, 140, 0); // Orange
	    } else {
		// At goal weight
		differenceText = "Difference: At goal weight! ✓";
		differenceColor = new Color(34, 139, 34); // Green
	    }

	    differenceDisplay.setText(differenceText);
	    differenceDisplay.setForeground(differenceColor);
	} else {
	    differenceDisplay.setText("Difference: Set both weights to see difference");
	    differenceDisplay.setForeground(Color.BLACK);
	}
    }

    // Getters for potential future use
    public double getCurrentWeight() {
	return currentWeight;
    }

    public double getGoalWeight() {
	return goalWeight;
    }

    public boolean hasCurrentWeight() {
	return hasCurrentWeight;
    }

    public boolean hasGoalWeight() {
	return hasGoalWeight;
    }
}
