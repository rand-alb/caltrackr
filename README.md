# Caltrackr — Calorie & Nutrition Tracker

A Java desktop application for logging daily food intake and reviewing nutrition history. Built as the **CP213 group project** at Wilfrid Laurier University.

> **Tech:** Java · Swing · CSV · Eclipse
> **Built:** November 2025 (CP213 — Introduction to Object-Oriented Programming)

---

## About

Caltrackr lets users log the foods they eat throughout the day, see daily calorie and nutrition totals, and review what they ate on previous days. Foods are loaded from a CSV nutrition database and modelled using an inheritance hierarchy — each food category (fruit, vegetable, meat, other) carries its own nutrient profile, demonstrating polymorphism in a practical setting.

## Features

- **Food Logging** — Log meals from a CSV-backed food database, with calorie and macronutrient information tracked per entry.
- **Daily Tracking** — View totals for calories, sugar, protein, fat, and carbohydrates as you log throughout the day.
- **Historical Review** — Persist logged days to `loggedDays.txt` and revisit previous entries to see eating patterns over time.
- **Category-Aware Foods** — Foods are typed by category (fruit, vegetable, meat, otherFood), with category-specific nutritional fields modelled through inheritance.
- **Extensible Food Database** — Add new foods by editing `foods_sample.csv` — no code changes required.

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Java |
| GUI | Java Swing |
| Data Storage | CSV (food database), Plain text (log history) |
| IDE | Eclipse (project files included) |

## Object-Oriented Design

Caltrackr was built as a CP213 demonstration of object-oriented principles:

- **Inheritance** — A base `Food` class is extended by category-specific subclasses (e.g. `Fruit`, `Vegetable`, `Meat`, `OtherFood`), each carrying nutrition fields relevant to that category.
- **Polymorphism** — The logging system handles any food category through the same interface, so adding a new category is a matter of subclassing rather than rewriting existing logic.
- **Encapsulation** — Nutritional data and logging logic are kept in dedicated classes, separated from the Swing UI.

## Food Database Format

`foods_sample.csv` follows a category-aware schema. Columns vary by food type — for example, fruits and vegetables include vitamin lists, while meats include protein and fat content. A few sample rows:

```csv
Apple,fruit,95,19,true,Vitamin C|Vitamin K|Potassium,25,,,,,
Carrot,vegetable,25,5,true,Vitamin A|Vitamin K,6,,,,,
Beef,meat,271,,,,,true,26,17,,,
Chocolate,otherFood,210,,,,27,,13,13,27,
```

## Project Structure

```
Caltrackr/
├── src/                  # Java source files (food classes, GUI, logging)
├── foods_sample.csv      # Food database (categories + nutrition fields)
├── loggedDays.txt        # Persisted log of past days
├── .classpath            # Eclipse classpath configuration
└── .project              # Eclipse project metadata
```

## Authors
Dorian Le
Max Ginsberg
Rand Albaroudi
