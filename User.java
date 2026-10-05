import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

/**
 * A class for user.
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
     */
    public YearMonth getMonth() {
        return month;
    }


    /**
     * Updates the monthly budget and fixed expenses.
     */
    public void setBudget(BigDecimal monthlyBudget,
        BigDecimal fixedExpenses) {

        if (monthlyBudget == null || fixedExpenses == null) {
            throw new IllegalArgumentException(
                "Budget amounts cannot be null.");
        }

        if (monthlyBudget.compareTo(BigDecimal.ZERO) < 0
            || fixedExpenses.compareTo(BigDecimal.ZERO) < 0) {
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
     */
    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }


    /**
     * Returns the fixed expenses.
     */
    public BigDecimal getFixedExpenses() {
        return fixedExpenses;
    }


    /**
     * Adds a new expense to the ledger.
     */
    public Transaction addExpense(
        LocalDate date,
        String categoryName,
        String description,
        BigDecimal amount) {

        if (date == null) {
            throw new IllegalArgumentException(
                "Please input a valid date.");
        }

        if (!YearMonth.from(date).equals(month)) {
            throw new IllegalArgumentException(
                "Please input a valid date.");
        }

        if (categoryName == null
            || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }

        Category category = findCategory(categoryName);

        if (category == null) {
            throw new IllegalArgumentException(
                "Please input a valid category.");
        }

        String id = "T" + nextTransactionId;

        Transaction transaction = new Transaction(
            id,
            date,
            category,
            description,
            amount);

        transactions.add(transaction);

        nextTransactionId++;

        return transaction;
    }


    /**
     * Deletes the transaction with the specified ID.
     */
    public boolean deleteExpense(String transactionId) {

        if (transactionId == null
            || transactionId.trim().isEmpty()) {
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
     */
    public Transaction[] getExpenses() {

        Transaction[] expenses = transactions.toArray();

        Arrays.sort(expenses,
            (first, second) ->
                first.getDate().compareTo(second.getDate()));

        return expenses;
    }


    /**
     * Returns all available categories.
     */
    public Category[] getCategories() {
        return categories.toArray();
    }


    /**
     * Returns the total amount spent on recorded transactions.
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
     */
    public BigDecimal getCategoryTotal(String categoryName) {

        if (categoryName == null
            || categoryName.trim().isEmpty()) {
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
     */
    public BigDecimal getRemainingBudget() {

        return monthlyBudget
            .subtract(fixedExpenses)
            .subtract(getTotalSpent());
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
            if (category.getName().equalsIgnoreCase(
                categoryName.trim())) {
                return category;
            }
        }

        return null;
    }


    /**
     * Finds a transaction by ID.
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

        categories.add(new Category("Food"));
        categories.add(new Category("Transportation"));
        categories.add(new Category("Entertainment"));
        categories.add(new Category("Utilities"));
        categories.add(new Category("Other"));
    }
}