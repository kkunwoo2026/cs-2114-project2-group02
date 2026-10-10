// Virginia Tech Honor Code Pledge:
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of those who
// do.
// -- Kunwoo Kim (kkunwoo)

// LLM Statement:
// I have not used any assistance for the assignment beyond course resources and
// staff.

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

/**
 * Creating class for user.
 * @author Kunwoo Kim
 * @version 2026.10.10
 */
public class User {
    private YearMonth month;
    private BigDecimal monthlyBudget;
    private BigDecimal fixedExpenses;
    private LinkedChain<Transaction> transactions;
    private LinkedChain<Category> categories;
    private int nextTransactionId;

    /**
     * Creates a ledger for the specified month.
     * Initializes transaction and category chains,
     * adds predefined categories, and sets budgets to zero.
     * 
     * @param month
     *            - gives which month the transaction will be about.
     */
    public User(YearMonth month) {
        if (month == null) {
            throw new IllegalArgumentException("Month cannot be null.");
        }
        this.month = month;
        this.monthlyBudget = BigDecimal.ZERO;
        this.fixedExpenses = BigDecimal.ZERO;
        this.transactions = new LinkedChain<Transaction>();
        this.categories = new LinkedChain<Category>();
        this.nextTransactionId = 1;
        initializeCategories();
    }


    /**
     * Returns the month associated with this ledger.
     * 
     * @return month - gets which month the transaction is.
     */
    public YearMonth getMonth() {
        return month;
    }


    /**
     * Updates the monthly budget and fixed expenses.
     * 
     * @param monthlyBudget
     *            - set the monthly budget.
     * @param fixedExpenses
     *            - sets up the fixedExpenses.
     */
    public void setBudget(BigDecimal monthlyBudget, BigDecimal fixedExpenses) {
        if (monthlyBudget == null || fixedExpenses == null) {
            throw new IllegalArgumentException(
                "Budget amounts cannot be null.");
        }

        if (monthlyBudget.compareTo(BigDecimal.ZERO) < 0 || fixedExpenses
            .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Budget amounts cannot be negative.");
        }

        if (fixedExpenses.compareTo(monthlyBudget) > 0) {
            throw new IllegalArgumentException(
                "Fixed expenses must not exceed the budget.");
        }

        this.monthlyBudget = monthlyBudget;
        this.fixedExpenses = fixedExpenses;
    }
    
    /**
     * Returns the monthly budget.
     * 
     * @return getters for getMontlyBudget().
     */
    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    /**
     * Returns the fixed expenses.
     * 
     * @return getters for fixedExpenses().
     */
    public BigDecimal getFixedExpenses() {
        return fixedExpenses;
    }


    /**
     * Adds a new expense to the ledger.
     * 
     * @param date
     *            - The date of transaction
     * @param categoryName
     *            - The name of category the transaction belongs
     * @param description
     *            - The description of the transaction
     * @param amount
     *            - The amount of transaction.
     */
    public Transaction addExpense(
        LocalDate date,
        String categoryName,
        String description,
        BigDecimal amount) {

        if (date == null) {
            throw new IllegalArgumentException("Please input a valid date.");
        }

        if (!YearMonth.from(date).equals(month)) {
            throw new IllegalArgumentException("Please input a valid date.");
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }

        Category category = findCategory(categoryName);

        if (category == null) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }

        String id = "T" + nextTransactionId;

        Transaction transaction = new Transaction(id, date, category,
            description, amount);

        transactions.add(transaction);
        nextTransactionId++;
        return transaction;
    }

    /**
     * Deletes the transaction with the specified ID.
     * 
     * @param transactionId
     *            which transaction delete
     * @return boolean whether the delete was successful or not.
     */
    public boolean deleteExpense(String transactionId) {

        if (transactionId == null || transactionId.trim().isEmpty()) {
            return false;
        }

        Transaction transaction = findTransaction(transactionId);

        if (transaction == null) {
            return false;
        }

        return transactions.remove(transaction);
    }


    /**
     * Returns all expenses sorted by date.
     * 
     * @return A transaction list of expenses.
     */
    public Transaction[] getExpenses() {
        Transaction[] expenses = transactions.toArray();
        Arrays.sort(expenses, (first, second) -> first.getDate().compareTo(
            second.getDate()));
        return expenses;
    }


    /**
     * Returns all available categories.
     * 
     * @return getters for categories().
     */
    public Category[] getCategories() {
        return categories.toArray();
    }


    /**
     * Returns the total amount spent on recorded transactions.
     * 
     * @return getters for totalSpent.
     */
    public BigDecimal getTotalSpent() {
        BigDecimal total = BigDecimal.ZERO;
        Transaction[] expenses = transactions.toArray();
        
        for (Transaction transaction : expenses) {
            total = total.add(transaction.getAmount());
        }
        return total;
    }


    /**
     * Returns the total amount spent in a specific category.
     * 
     * @param categoryName
     *            - the name of category
     * @return getters for categoryTotal().
     */
    public BigDecimal getCategoryTotal(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }
        Category category = findCategory(categoryName);
        if (category == null) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }
        BigDecimal total = BigDecimal.ZERO;
        Transaction[] expenses = transactions.toArray();
        for (Transaction transaction : expenses) {
            if (transaction.getCategory().equals(category)) {
                total = total.add(transaction.getAmount());
            }
        }
        return total;
    }


    /**
     * Returns the amount of the budget remaining.
     * 
     * @return getters for remainingBudget().
     */
    public BigDecimal getRemainingBudget() {
        return monthlyBudget.subtract(fixedExpenses).subtract(getTotalSpent());
    }

    /**
     * Finds a category by name, ignoring capitalization.
     */
    private Category findCategory(String categoryName) {

        if (categoryName == null) {
            return null;
        }

        Category[] categoryArray = categories.toArray();

        for (Category category : categoryArray) {
            if (category.getName().equalsIgnoreCase(categoryName.trim())) {
                return category;
            }
        }

        return null;
    }


    /**
     * Finds a transaction by ID.
     * 
     * @param transactionId
     *            The transactions to find
     * @return Transaction
     */
    private Transaction findTransaction(String transactionId) {

        if (transactionId == null) {
            return null;
        }
        Transaction[] expenseArray = transactions.toArray();

        for (Transaction transaction : expenseArray) {
            if (transaction.getId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }


    /**
     * Adds the predefined categories to the category chain.
     */
    private void initializeCategories() {
        categories.addToEnd(new Category("Food"));
        categories.addToEnd(new Category("Transportation"));
        categories.addToEnd(new Category("Entertainment"));
        categories.addToEnd(new Category("Utilities"));
        categories.addToEnd(new Category("Other"));
    }
}
