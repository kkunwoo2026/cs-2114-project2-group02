import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main console runner for the PocketLedger application.
 *
 * @author Andrew Park (andrewp04)
 * @version Oct 11, 2026
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User userLedger;

    // ----------------------------------------------------------
    /**
     * Starts the PocketLedger app
     * 
     * @param args
     *            command line arg
     */
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("       Welcome to PocketLedger!          ");
        System.out.println("=========================================");

        YearMonth month = promptYearMonth();
        userLedger = new User(month);
        System.out.println("\nLedger successfully initialized for " + month
            + ".");

        setupInitialBudget();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddExpense();
                    break;
                case "2":
                    handleDeleteExpense();
                    break;
                case "3":
                    handleViewExpenses();
                    break;
                case "4":
                    handleViewSummary();
                    break;
                case "5":
                    handleCategorySpending();
                    break;
                case "6":
                    setupInitialBudget();
                    break;
                case "7":
                    System.out.println(
                        "\nThank you for using PocketLedger. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println(
                        "\n[!] Invalid option. Please enter a number between 1 and 7.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the menu option
     */
    private static void printMenu() {
        System.out.println("\n-----------------------------------------");
        System.out.println("               MAIN MENU                 ");
        System.out.println("-----------------------------------------");
        System.out.println("1. Add an Expense");
        System.out.println("2. Delete an Expense");
        System.out.println("3. View All Expenses (Sorted by Date)");
        System.out.println("4. View Monthly Summary & Remaining Budget");
        System.out.println("5. Check Spending by Category");
        System.out.println("6. Update Budget & Fixed Expenses");
        System.out.println("7. Exit");
        System.out.print("Select an option (1-7): ");
    }

    /**
     * Prompts the user for a valid year month in YYYY-MM format
     * 
     * @return the parsed year month instance
     */
    private static YearMonth promptYearMonth() {
        while (true) {
            System.out.print("Enter target month (YYYY-MM, e.g., 2026-10): ");
            String input = scanner.nextLine().trim();
            try {
                return YearMonth.parse(input);
            }
            catch (DateTimeParseException e) {
                System.out.println("[!] Invalid format. Please use YYYY-MM.");
            }
        }
    }

    /**
     * Prompts the user to set or update monthly budget and fixed expenses
     */
    private static void setupInitialBudget() {
        System.out.println("\n--- Set Monthly Budget ---");
        while (true) {
            try {
                System.out.print("Enter total monthly budget ($): ");
                BigDecimal budget = new BigDecimal(scanner.nextLine().trim());

                System.out.print("Enter total fixed expenses ($): ");
                BigDecimal fixed = new BigDecimal(scanner.nextLine().trim());

                userLedger.setBudget(budget, fixed);
                System.out.println("Budget updated successfully!");
                break;
            }
            catch (NumberFormatException e) {
                System.out.println(
                    "[!] Invalid numeric amount. Please enter a valid decimal number.");
            }
            catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
            }
        }
    }

    /**
     * Handles user interaction for adding a new expense transaction
     */
    private static void handleAddExpense() {
        System.out.println("\n--- Add Expense ---");

        // Show available categories first
        System.out.print("Available Categories: [");
        Category[] cats = userLedger.getCategories();
        for (int i = 0; i < cats.length; i++) {
            System.out.print(cats[i].getName());
            if (i < cats.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        try {
            System.out.print("Enter date (YYYY-MM-DD, must be in " + userLedger
                .getMonth() + "): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter amount ($): ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());

            Transaction t = userLedger.addExpense(date, category, description,
                amount);
            System.out.println("[+] Expense added successfully: " + t);
        }
        catch (DateTimeParseException e) {
            System.out.println(
                "[!] Invalid date format. Please use YYYY-MM-DD.");
        }
        catch (NumberFormatException e) {
            System.out.println("[!] Invalid expense amount.");
        }
        catch (IllegalArgumentException e) {
            System.out.println("[!] Failed to add expense: " + e.getMessage());
        }
    }

    /**
     * Handles user interaction for deleting a new expense transaction
     */
    private static void handleDeleteExpense() {
        System.out.println("\n--- Delete Expense ---");
        System.out.print("Enter Transaction ID to delete (e.g., T1): ");
        String id = scanner.nextLine().trim();

        boolean removed = userLedger.deleteExpense(id);
        if (removed) {
            System.out.println("[+] Transaction " + id + " has been deleted.");
        }
        else {
            System.out.println("[-] Transaction with ID '" + id
                + "' not found.");
        }
    }

    /**
     * Displays all expenses recorded 
     */
    private static void handleViewExpenses() {
        System.out.println("\n--- Recorded Expenses (Sorted by Date) ---");
        Transaction[] expenses = userLedger.getExpenses();

        if (expenses.length == 0) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        for (Transaction t : expenses) {
            System.out.println(t);
        }
    }

    /**
     * Displays monthly budget summary and balance status
     */
    private static void handleViewSummary() {
        System.out.println("\n=========================================");
        System.out.println("       Monthly Financial Summary         ");
        System.out.println("=========================================");
        System.out.println("Target Month:       " + userLedger.getMonth());
        System.out.println("Monthly Budget:     $" + userLedger
            .getMonthlyBudget());
        System.out.println("Fixed Expenses:     $" + userLedger
            .getFixedExpenses());
        System.out.println("Variable Spent:     $" + userLedger
            .getTotalSpent());
        System.out.println("-----------------------------------------");

        BigDecimal remaining = userLedger.getRemainingBudget();
        System.out.println("Remaining Budget:   $" + remaining);

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("[WARNING] You are currently over budget by $"
                + remaining.abs() + "!");
        }
        else {
            System.out.println("[OK] You are within your budget limits.");
        }
    }

    /**
     * Displays the total amount spent within a specific category
     */
    private static void handleCategorySpending() {
        System.out.println("\n--- Check Category Spending ---");
        System.out.print("Enter category name: ");
        String catName = scanner.nextLine().trim();

        try {
            BigDecimal total = userLedger.getCategoryTotal(catName);
            System.out.println("Total spent in [" + catName + "]: $" + total);
        }
        catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }
}
