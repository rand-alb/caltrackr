import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages a history of daily food entries
 * 
 * @author Maxim Ginsberg
 * @version 2025-11-23
 */
public class LogHistory {
    private ArrayList<Entry> entries;

    /**
     * Constructor initializes empty log history
     */
    public LogHistory() {
	this.entries = new ArrayList<>();
    }

    /**
     * Add an entry to the log
     * 
     * @param entry The entry to add
     * @return true if added successfully
     */
    public boolean addEntry(Entry entry) {
	if (entry == null) {
	    return false;
	}
	entries.add(entry);
	return true;
    }

    /**
     * Get all entries
     * 
     * @return Copy of entries list
     */
    public List<Entry> getAllEntries() {
	return new ArrayList<>(entries);
    }

    /**
     * Get entry by date
     * 
     * @param date The date to search for
     * @return Entry if found, null otherwise
     */
    public Entry getEntryByDate(int date) {
	for (Entry entry : entries) {
	    if (entry.getDate() == date) {
		return entry;
	    }
	}
	return null;
    }

    /**
     * Get total number of entries
     * 
     * @return Number of entries in log
     */
    public int getEntryCount() {
	return entries.size();
    }

    /**
     * Get number of days where goal was met
     * 
     * @return Count of successful days
     */
    public int getGoalMetCount() {
	int count = 0;
	for (Entry entry : entries) {
	    if (entry.getGoalMet()) {
		count++;
	    }
	}
	return count;
    }

    /**
     * Get average calories across all entries
     * 
     * @return Average calories, or 0 if no entries
     */
    public double getAverageCalories() {
	if (entries.isEmpty()) {
	    return 0;
	}

	int total = 0;
	for (Entry entry : entries) {
	    total += entry.getTotalCalories();
	}
	return (double) total / entries.size();
    }

    /**
     * Clear all entries
     */
    public void clearHistory() {
	entries.clear();
    }

    /**
     * Get a summary of the log history
     * 
     * @return String with statistics
     */
    public String getHistorySummary() {
	StringBuilder summary = new StringBuilder();
	summary.append("=== Log History Summary ===\n");
	summary.append("Total Entries: ").append(entries.size()).append("\n");
	summary.append("Goals Met: ").append(getGoalMetCount()).append(" / ").append(entries.size()).append("\n");

	if (!entries.isEmpty()) {
	    summary.append("Average Calories: ").append(String.format("%.0f", getAverageCalories())).append("\n");
	    summary.append("Success Rate: ").append(String.format("%.1f", (getGoalMetCount() * 100.0 / entries.size())))
		    .append("%\n");
	}

	return summary.toString();
    }

    /**
     * Get detailed view of all entries
     * 
     * @return Formatted string of all entries
     */
    public String getDetailedHistory() {
	StringBuilder details = new StringBuilder();
	details.append("=== Complete Log History ===\n\n");

	if (entries.isEmpty()) {
	    details.append("No entries logged yet.\n");
	} else {
	    for (int i = 0; i < entries.size(); i++) {
		Entry entry = entries.get(i);
		details.append("[").append(i + 1).append("] Date: ").append(entry.getDate()).append("\n");
		details.append("    Calories: ").append(entry.getTotalCalories()).append(" / ").append(entry.getGoal())
			.append("\n");
		details.append("    Goal Met: ").append(entry.getGoalMet() ? "Yes" : "No").append("\n\n");
	    }
	}

	return details.toString();
    }

    @Override
    public String toString() {
	return "LogHistory [entries=" + entries.size() + ", goalsMetCount=" + getGoalMetCount() + "]";
    }
}
