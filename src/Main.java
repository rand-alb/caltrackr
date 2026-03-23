
/**
 * Main menu - DailyGoalPanel and TrackingPanel work independently
 * 
 * @author Dorian Le, Maxim Ginsberg, Rand Albaroudi
 * @version 2025-11-23
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(() -> {

	    // ====== Instantiate components ======
	    JFrame mainWindow = new JFrame("CalTrackr - Calorie Tracker");
	    JPanel div = new JPanel();
	    JPanel cardPanel = new JPanel(new CardLayout());

	    JLabel logo = new JLabel("CalTrackr");
	    JLabel names = new JLabel("Rand Albaroudi, Dorian Le, Maxim Ginsberg (2025)");
	    JButton foodEntries = new JButton("Food Entries");
	    JButton tracking = new JButton("Summary/Tracking");
	    JButton personal = new JButton("Personal");

	    CardLayout cl = (CardLayout) cardPanel.getLayout();

	    // Create shared LogHistory for DailyGoalPanel and TrackingPanel
	    // This is independent of Personal panel
	    LogHistory logHistory = new LogHistory();

	    // Create Food Entries and Tracking panels (these work together)
	    DailyGoalPanel dailyGoalPanel = new DailyGoalPanel(cl, cardPanel, logHistory);
	    TrackingPanel trackingPanel = new TrackingPanel(cl, cardPanel, logHistory);

	    // Personal panel is separate - can be changed without affecting others
	    Personal accountPanel = new Personal(cl, cardPanel);

	    // Set layout
	    mainWindow.setLayout(new BorderLayout());

	    // ====== Set GUI component properties ======
	    logo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
	    names.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));

	    foodEntries.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
	    foodEntries.setPreferredSize(new Dimension(300, 90));

	    tracking.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
	    tracking.setPreferredSize(new Dimension(300, 90));

	    personal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
	    personal.setPreferredSize(new Dimension(300, 90));

	    // Add GUI components to menu and panels
	    mainWindow.add(logo, BorderLayout.NORTH);
	    mainWindow.add(cardPanel, BorderLayout.CENTER);
	    mainWindow.add(names, BorderLayout.SOUTH);

	    div.add(foodEntries);
	    div.add(tracking);
	    div.add(personal);

	    // Screens to switch between
	    cardPanel.add(div, "menu");
	    cardPanel.add(dailyGoalPanel, "foodentries");
	    cardPanel.add(trackingPanel, "tracking");
	    cardPanel.add(accountPanel, "account");

	    // ====== Button action listeners ======
	    foodEntries.addActionListener(e -> {
		cl.show(cardPanel, "foodentries");
	    });

	    tracking.addActionListener(e -> {
		cl.show(cardPanel, "tracking");
	    });

	    personal.addActionListener(e -> {
		cl.show(cardPanel, "account");
	    });

	    // Adjust GUI component position
	    logo.setHorizontalAlignment(JLabel.CENTER);
	    logo.setVerticalAlignment(JLabel.CENTER);

	    names.setHorizontalAlignment(JLabel.CENTER);
	    names.setVerticalAlignment(JLabel.CENTER);

	    // Set window and panel properties
	    div.setVisible(true);
	    mainWindow.setSize(800, 650);
	    mainWindow.setVisible(true);
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	});

    }

}
