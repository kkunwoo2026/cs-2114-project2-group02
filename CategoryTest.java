import student.TestCase;
/**
 *  Tests the category class. 
 * 
 *  @author willfleming
 *  @version Oct 9, 2026
 */

public class CategoryTest extends TestCase
{
    private Category food;

    /**
     * Creates a fresh category before each test.
     */
    @Override
    public void setUp()
    {
        food = new Category("Food");
    }


    /**
     * Tests that the constructor stores a valid name.
     */
    public void testConstructor()
    {
        Category transportation = new Category("Transportation");

        assertEquals("Transportation", transportation.getName());
    }


    /**
     * Tests that surrounding whitespace is removed while
     * internal spaces and capitalization are preserved.
     */
    public void testConstructorTrimsWhitespace()
    {
        Category category = new Category(" \tDining Out\n ");

        assertEquals("Dining Out", category.getName());
    }


    /**
     * Tests that a null name is rejected.
     */
    public void testConstructorNullName()
    {
        try
        {
            new Category(null);
            fail("Expected IllegalArgumentException for a null name.");
        }
        catch (IllegalArgumentException exception)
        {
            assertEquals(
                "Category name cannot be null.",
                exception.getMessage());
        }
    }


    /**
     * Tests that an empty name is rejected.
     */
    public void testConstructorEmptyName()
    {
        try
        {
            new Category("");
            fail("Expected IllegalArgumentException for an empty name.");
        }
        catch (IllegalArgumentException exception)
        {
            assertEquals(
                "Category name cannot be blank.",
                exception.getMessage());
        }
    }


    /**
     * Tests that a name containing only whitespace is rejected.
     */
    public void testConstructorWhitespaceName()
    {
        try
        {
            new Category(" \t\n ");
            fail("Expected IllegalArgumentException for a blank name.");
        }
        catch (IllegalArgumentException exception)
        {
            assertEquals(
                "Category name cannot be blank.",
                exception.getMessage());
        }
    }


    /**
     * Tests the name getter.
     */
    public void testGetName()
    {
        assertEquals("Food", food.getName());
    }


    /**
     * Tests that a category equals itself.
     */
    public void testEqualsSameObject()
    {
        assertTrue(food.equals(food));
    }


    /**
     * Tests equality between separate categories with the same name.
     */
    public void testEqualsSameName()
    {
        Category otherFood = new Category("Food");

        assertTrue(food.equals(otherFood));
        assertTrue(otherFood.equals(food));
    }


    /**
     * Tests that capitalization does not affect equality.
     */
    public void testEqualsDifferentCapitalization()
    {
        Category otherFood = new Category("fOoD");

        assertTrue(food.equals(otherFood));
        assertTrue(otherFood.equals(food));
    }


    /**
     * Tests that constructor trimming is reflected in equality.
     */
    public void testEqualsTrimmedName()
    {
        Category otherFood = new Category("  FOOD  ");

        assertTrue(food.equals(otherFood));
    }


    /**
     * Tests that categories with different names are not equal.
     */
    public void testEqualsDifferentName()
    {
        Category rent = new Category("Rent");

        assertFalse(food.equals(rent));
    }


    /**
     * Tests comparison with null.
     */
    public void testEqualsNull()
    {
        assertFalse(food.equals(null));
    }


    /**
     * Tests comparison with an object of a different class.
     */
    public void testEqualsDifferentClass()
    {
        assertFalse(food.equals("Food"));
    }


    /**
     * Tests that equality is transitive.
     */
    public void testEqualsTransitive()
    {
        Category second = new Category("FOOD");
        Category third = new Category("food");

        assertTrue(food.equals(second));
        assertTrue(second.equals(third));
        assertTrue(food.equals(third));
    }


    /**
     * Tests that equal categories have equal hash codes.
     */
    public void testHashCodeEqualCategories()
    {
        Category otherFood = new Category("  FOOD  ");

        assertTrue(food.equals(otherFood));
        assertEquals(food.hashCode(), otherFood.hashCode());
    }


    /**
     * Tests that repeated hash code calls return the same value.
     */
    public void testHashCodeConsistent()
    {
        int originalHash = food.hashCode();

        assertEquals(originalHash, food.hashCode());
    }


    /**
     * Tests the readable string representation.
     */
    public void testToString()
    {
        assertEquals("Food", food.toString());

        Category category = new Category("  Dining Out  ");
        assertEquals("Dining Out", category.toString());
    }

}
