import student.TestCase;
import java.time.LocalDate;
import java.math.BigDecimal;


public class TransactionTest extends TestCase
{
    
    private Category category;
    private Transaction transaction;
    
    public void setUp()
    {
        category = new Category("Food");
        
        transaction = new Transaction("001", LocalDate.of(2026, 10, 8), category,
            "Groceries", new BigDecimal("35.50"));
    }
    public void testConstructor()
    {
        assertEquals("001", transaction.getId());
        assertEquals(LocalDate.of(2026, 10, 8), 
            transaction.getDate());
        assertEquals(category, transaction.getCategory());
        assertEquals("Groceries", transaction.getDescription());
        assertEquals(new BigDecimal("35.50"), transaction.getAmount());
            
    }
    
    public void testNullId()
    {
        try
        {
            new Transaction(null, LocalDate.of(2026, 10, 8), category, "Groceries", 
                new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
        
    }
    
    public void testBlankId()
    {
        try
        {
            new Transaction(" ", LocalDate.of(2026, 10, 8), category, "Groceries", 
                new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testNullDate()
    {
        try
        {
            new Transaction("002", null, category, "Groceries", new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testNullCategory()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), null, 
                "Groceries", new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testNullDescription()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, null,
                new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testBlankDescription()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, "  ", 
                new BigDecimal("35.50"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testNullAmount()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, "Groceries", null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testZeroAmount()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, 
                "Groceries", BigDecimal.ZERO);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testNegativeAmount()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, "Groceries", 
                new BigDecimal("-10.00"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testAmountOverLimit()
    {
        try
        {
            new Transaction("002", LocalDate.of(2026, 10, 8), category, "Groceries",
                new BigDecimal("100000.01"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
    
    public void testAmountAtLimit()
    {
        Transaction maxTransaction = new Transaction("002", LocalDate.of(2026, 10, 8), 
            category, "Groceries", new BigDecimal("100000.00"));
        
        assertEquals(new BigDecimal("100000.00"), maxTransaction.getAmount());
    }
 
    public void testEqualsSameId()
    {
        Transaction other = new Transaction("001", LocalDate.of(2026, 10, 9), category, 
            "Lunch", new BigDecimal("15.00"));
        
        assertTrue(transaction.equals(other));
    }
    
    public void testEqualsDifferentId()
    {
        Transaction other = new Transaction("002", LocalDate.of(2026, 10, 8), category, 
            "Groceries", new BigDecimal("35.50"));
        
        assertFalse(transaction.equals(other));
    }
    
    public void testEqualsNull()
    {
        assertFalse(transaction.equals(null));
    }
    
    public void testEqualsDifferentObject()
    {
        assertFalse(transaction.equals("001"));
    }
    
    public void testEqualsSameObject()
    {
        assertTrue(transaction.equals(transaction));
    }
    
    public void testHashCode()
    {
        Transaction other = new Transaction("001", LocalDate.of(2026, 10, 9), 
            category, "Lunch", new BigDecimal("15.00"));
        
        assertEquals(transaction.hashCode(), other.hashCode());
    }
    
    public void testToString()
    {
        String expected = "ID: 001, Date: 2026-10-08, "
            + "Category: Food, Description: Groceries, "
            + "Amount: $35.50";
        
        assertEquals(expected, transaction.toString());
    }

}
