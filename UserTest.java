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
import junit.framework.TestCase;

/**
 * A testing class for user.
 * 
 * @author Kunwoo Kim
 * @version 2026.10.10
 */
public class UserTest extends TestCase {
    private User user;
    private YearMonth month;

    /**
     * Sets up a new User before each test.
     */
    protected void setUp() {
        month = YearMonth.of(2026, 10);
        user = new User(month);
    }


    /**
     * Checks constructor initialization.
     */
    public void testConstructor() {
        assertEquals(month, user.getMonth());
        assertEquals(0, user.getMonthlyBudget().compareTo(BigDecimal.ZERO));
        assertEquals(0, user.getFixedExpenses().compareTo(BigDecimal.ZERO));
        assertEquals(5, user.getCategories().length);
        assertEquals(0, user.getExpenses().length);
    }


    /**
     * Checks that a null month is rejected.
     */
    public void testConstructorNullMonth() {
        try {
            new User(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that a valid budget can be set.
     */
    public void testSetBudget() {
        user.setBudget(new BigDecimal("2000.00"), new BigDecimal("600.00"));

        assertMoney("2000.00", user.getMonthlyBudget());
        assertMoney("600.00", user.getFixedExpenses());
    }


    /**
     * Checks that null budget amounts are rejected.
     */
    public void testSetBudgetNull() {
        try {
            user.setBudget(null, BigDecimal.ZERO);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            user.setBudget(BigDecimal.ZERO, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that negative budget amounts are rejected.
     */
    public void testSetBudgetNegative() {
        try {
            user.setBudget(new BigDecimal("-1.00"), BigDecimal.ZERO);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            user.setBudget(new BigDecimal("100.00"), new BigDecimal("-1.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that fixed expenses cannot exceed the budget.
     */
    public void testSetBudgetFixedExpensesExceedBudget() {
        try {
            user.setBudget(new BigDecimal("100.00"), new BigDecimal("150.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that a valid expense is added.
     */
    public void testAddExpense() {
        Transaction transaction = user.addExpense(LocalDate.of(2026, 10, 10),
            "Food", "Lunch", new BigDecimal("15.00"));

        assertNotNull(transaction);
        assertEquals("T1", transaction.getId());
        assertEquals(1, user.getExpenses().length);
        assertMoney("15.00", user.getTotalSpent());
    }


    /**
     * Checks that transaction IDs increase.
     */
    public void testAddExpenseIds() {
        Transaction first = user.addExpense(LocalDate.of(2026, 10, 1), "Food",
            "Breakfast", new BigDecimal("5.00"));

        Transaction second = user.addExpense(LocalDate.of(2026, 10, 2), "Food",
            "Lunch", new BigDecimal("10.00"));

        assertEquals("T1", first.getId());
        assertEquals("T2", second.getId());
    }


    /**
     * Checks that a null date is rejected.
     */
    public void testAddExpenseNullDate() {
        try {
            user.addExpense(null, "Food", "Lunch", new BigDecimal("10.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that a date outside the ledger month is rejected.
     */
    public void testAddExpenseWrongMonth() {
        try {
            user.addExpense(LocalDate.of(2026, 11, 1), "Food", "Lunch",
                new BigDecimal("10.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that an invalid category is rejected.
     */
    public void testAddExpenseInvalidCategory() {
        try {
            user.addExpense(LocalDate.of(2026, 10, 10), "InvalidCategory",
                "Lunch", new BigDecimal("10.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks that category lookup ignores capitalization.
     */
    public void testAddExpenseCategoryCaseInsensitive() {
        Transaction transaction = user.addExpense(LocalDate.of(2026, 10, 10),
            "food", "Lunch", new BigDecimal("10.00"));

        assertNotNull(transaction);
        assertEquals(1, user.getExpenses().length);
    }


    /**
     * Checks that a blank category is rejected.
     */
    public void testAddExpenseBlankCategory() {
        try {
            user.addExpense(LocalDate.of(2026, 10, 10), "   ", "Lunch",
                new BigDecimal("10.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks successful deletion.
     */
    public void testDeleteExpense() {
        Transaction transaction = user.addExpense(LocalDate.of(2026, 10, 10),
            "Food", "Lunch", new BigDecimal("15.00"));

        assertTrue(user.deleteExpense(transaction.getId()));
        assertEquals(0, user.getExpenses().length);
        assertMoney("0", user.getTotalSpent());
    }


    /**
     * Checks deletion with an invalid ID.
     */
    public void testDeleteExpenseInvalidId() {
        assertFalse(user.deleteExpense("T999"));
        assertFalse(user.deleteExpense(null));
        assertFalse(user.deleteExpense("   "));
    }


    /**
     * Checks that deleted IDs are not reused.
     */
    public void testDeleteExpenseDoesNotReuseId() {
        Transaction first = user.addExpense(LocalDate.of(2026, 10, 1), "Food",
            "Breakfast", new BigDecimal("5.00"));

        user.deleteExpense(first.getId());

        Transaction second = user.addExpense(LocalDate.of(2026, 10, 2), "Food",
            "Lunch", new BigDecimal("10.00"));

        assertEquals("T2", second.getId());
    }


    /**
     * Checks that expenses are returned in date order.
     */
    public void testGetExpensesSortedByDate() {
        user.addExpense(LocalDate.of(2026, 10, 20), "Food", "Dinner",
            new BigDecimal("20.00"));

        user.addExpense(LocalDate.of(2026, 10, 5), "Food", "Breakfast",
            new BigDecimal("5.00"));

        user.addExpense(LocalDate.of(2026, 10, 12), "Food", "Lunch",
            new BigDecimal("12.00"));

        Transaction[] expenses = user.getExpenses();

        assertEquals(3, expenses.length);
        assertEquals(LocalDate.of(2026, 10, 5), expenses[0].getDate());
        assertEquals(LocalDate.of(2026, 10, 12), expenses[1].getDate());
        assertEquals(LocalDate.of(2026, 10, 20), expenses[2].getDate());
    }


    /**
     * Checks the total spent across all categories.
     */
    public void testGetTotalSpent() {
        user.addExpense(LocalDate.of(2026, 10, 1), "Food", "Breakfast",
            new BigDecimal("10.00"));

        user.addExpense(LocalDate.of(2026, 10, 2), "Transportation", "Bus",
            new BigDecimal("3.50"));

        assertMoney("13.50", user.getTotalSpent());
    }


    /**
     * Checks the total spent in a category.
     */
    public void testGetCategoryTotal() {
        user.addExpense(LocalDate.of(2026, 10, 1), "Food", "Breakfast",
            new BigDecimal("10.00"));

        user.addExpense(LocalDate.of(2026, 10, 2), "Food", "Lunch",
            new BigDecimal("15.00"));

        user.addExpense(LocalDate.of(2026, 10, 3), "Transportation", "Bus",
            new BigDecimal("3.50"));

        assertMoney("25.00", user.getCategoryTotal("Food"));

        assertMoney("3.50", user.getCategoryTotal("Transportation"));
    }


    /**
     * Checks that invalid category names are rejected.
     */
    public void testGetCategoryTotalInvalidCategory() {
        try {
            user.getCategoryTotal("InvalidCategory");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            user.getCategoryTotal(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected exception.
        }
    }


    /**
     * Checks the remaining budget calculation.
     */
    public void testGetRemainingBudget() {
        user.setBudget(new BigDecimal("2000.00"), new BigDecimal("600.00"));

        user.addExpense(LocalDate.of(2026, 10, 10), "Food", "Lunch",
            new BigDecimal("15.00"));

        user.addExpense(LocalDate.of(2026, 10, 11), "Transportation", "Bus",
            new BigDecimal("5.00"));

        assertMoney("1380.00", user.getRemainingBudget());
    }


    /**
     * Compares monetary values without considering scale.
     * 
     * @param expected
     *            expected monetary value
     * @param actual
     *            actual monetary value
     */
    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }
}
