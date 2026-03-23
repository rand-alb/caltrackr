import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * Panel for viewing summary and tracking history
 * 
 * @author Maxim Ginsberg
 * @version 2025-11-23
 */
public class TrackingPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel parentPanel;
    private LogHistory logHistory;
    private JPanel entriesPanel;

    public TrackingPanel(CardLayout layout, JPanel parent, LogHistory logHistory) {
	this.cardLayout = layout;
	this.parentPanel = parent;
	this.logHistory = logHistory;

	setLayout(new BorderLayout(10, 10));
	setBorder(new EmptyBorder(20, 20, 20, 20));

	// Header
	JLabel header = new JLabel("Summary & Tracking");
	header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
	header.setHorizontalAlignment(JLabel.CENTER);
	add(header, BorderLayout.NORTH);

	// Main content area
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

	// Statistics panel
	JPanel statsPanel = createStatsPanel();
	mainPanel.add(statsPanel, BorderLayout.NORTH);

	// Entries list
	entriesPanel = new JPanel();
	entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
	entriesPanel.setBackground(Color.WHITE);

	JScrollPane scrollPane = new JScrollPane(entriesPanel);
	scrollPane.setPreferredSize(new Dimension(700, 400));
	mainPanel.add(scrollPane, BorderLayout.CENTER);

	add(mainPanel, BorderLayout.CENTER);

	// Bottom buttons
	JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	JButton backButton = new JButton("Back to Menu");
	JButton refreshButton = new JButton("Refresh");

	backButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
	refreshButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));

	backButton.addActionListener(e -> cardLayout.show(parentPanel, "menu"));
	refreshButton.addActionListener(e -> refreshDisplay());

	bottomPanel.add(refreshButton);
	bottomPanel.add(backButton);
	add(bottomPanel, BorderLayout.SOUTH);

	// Initial display
	refreshDisplay();
    }

    private JPanel createStatsPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(BorderFactory.createTitledBorder("Overall Statistics"));
	panel.setBackground(new Color(240, 248, 255));
	panel.setPreferredSize(new Dimension(700, 100));

	return panel;
    }

    private void refreshDisplay() {
	// Clear existing entries
	entriesPanel.removeAll();

	// Update statistics
	updateStatistics();

	// Display all entries
	if (logHistory.getEntryCount() == 0) {
	    JLabel noEntries = new JLabel("No entries logged yet. Go to 'Set Daily Goal' to start tracking!");
	    noEntries.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
	    noEntries.setForeground(Color.GRAY);
	    noEntries.setBorder(new EmptyBorder(20, 20, 20, 20));
	    entriesPanel.add(noEntries);
	} else {
	    for (Entry entry : logHistory.getAllEntries()) {
		JPanel entryPanel = createEntryPanel(entry);
		entriesPanel.add(entryPanel);
	    }
	}

	entriesPanel.revalidate();
	entriesPanel.repaint();
    }

    private void updateStatistics() {
	// Get the stats panel
	JPanel mainPanel = (JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
	JPanel statsPanel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);

	statsPanel.removeAll();

	// Create stats display
	JPanel statsContent = new JPanel();
	statsContent.setLayout(new BoxLayout(statsContent, BoxLayout.Y_AXIS));
	statsContent.setBackground(new Color(240, 248, 255));
	statsContent.setBorder(new EmptyBorder(10, 20, 10, 20));

	int totalEntries = logHistory.getEntryCount();
	int goalsMetCount = logHistory.getGoalMetCount();
	double avgCalories = logHistory.getAverageCalories();

	JLabel totalLabel = new JLabel("Total Entries: " + totalEntries);
	totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

	JLabel goalsLabel = new JLabel("Goals Met: " + goalsMetCount + " / " + totalEntries);
	goalsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

	JLabel avgLabel = new JLabel("Average Calories: " + String.format("%.0f", avgCalories));
	avgLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

	JLabel successLabel = new JLabel("");
	if (totalEntries > 0) {
	    double successRate = (goalsMetCount * 100.0) / totalEntries;
	    successLabel.setText("Success Rate: " + String.format("%.1f%%", successRate));
	    successLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

	    if (successRate >= 75) {
		successLabel.setForeground(new Color(0, 128, 0));
	    } else if (successRate >= 50) {
		successLabel.setForeground(new Color(255, 140, 0));
	    } else {
		successLabel.setForeground(new Color(178, 34, 34));
	    }
	}

	statsContent.add(totalLabel);
	statsContent.add(goalsLabel);
	statsContent.add(avgLabel);
	if (totalEntries > 0) {
	    statsContent.add(successLabel);
	}

	statsPanel.add(statsContent, BorderLayout.CENTER);
	statsPanel.revalidate();
	statsPanel.repaint();
    }

    private JPanel createEntryPanel(Entry entry) {
	JPanel panel = new JPanel(new BorderLayout(10, 10));
	panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
		new EmptyBorder(15, 15, 15, 15)));
	panel.setBackground(Color.WHITE);
	panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

	// Left side - date
	JLabel dateLabel = new JLabel("" + entry.getDate());
	dateLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
	dateLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

	// Center - calories info
	JPanel centerPanel = new JPanel();
	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
	centerPanel.setBackground(Color.WHITE);

	JLabel caloriesLabel = new JLabel("Calories: " + entry.getTotalCalories() + " / " + entry.getGoal() + " kcal");
	caloriesLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

	int difference = entry.getTotalCalories() - entry.getGoal();
	String diffText = difference >= 0 ? "+" + difference : "" + difference;
	JLabel diffLabel = new JLabel("Difference: " + diffText + " kcal");
	diffLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 13));
	diffLabel.setForeground(difference >= 0 ? new Color(178, 34, 34) : new Color(34, 139, 34));

	centerPanel.add(caloriesLabel);
	centerPanel.add(diffLabel);

	// Right side - goal met status
	JLabel statusLabel = new JLabel(entry.getGoalMet() ? "✓ Goal Met" : "✗ Goal Not Met");
	statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
	statusLabel.setForeground(entry.getGoalMet() ? new Color(34, 139, 34) : new Color(178, 34, 34));
	statusLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

	panel.add(dateLabel, BorderLayout.WEST);
	panel.add(centerPanel, BorderLayout.CENTER);
	panel.add(statusLabel, BorderLayout.EAST);

	return panel;
    }
}
