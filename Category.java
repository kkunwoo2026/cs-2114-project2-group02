import java.util.Locale;

/**
 *  Represents a spending category in the personal ledger.
 *  Category names cannot be changed after construction.
 * 
 *  @author willfleming
 *  @version Oct 9, 2026
 */

public class Category
{
    private final String name;
    private final String normalizedName;

    /**
     * Creates a category with the given name.
     * Leading and trailing whitespace is removed.
     *
     * @param name
     *            the category name
     * @throws IllegalArgumentException
     *             if the name is null or blank
     */
    public Category(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException(
                "Category name cannot be null.");
        }

        String trimmedName = name.trim();

        if (trimmedName.isEmpty())
        {
            throw new IllegalArgumentException(
                "Category name cannot be blank.");
        }

        this.name = trimmedName;
        this.normalizedName = trimmedName.toLowerCase(Locale.ROOT);
    }


    /**
     * Returns the category name with its original capitalization.
     *
     * @return the category name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Compares categories by their normalized names.
     * Capitalization does not affect equality.
     *
     * @param other
     *            the object to compare with this category
     * @return true if both categories have the same normalized name
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (other == null)
        {
            return false;
        }

        if (getClass() != other.getClass())
        {
            return false;
        }

        Category otherCategory = (Category)other;

        return normalizedName.equals(otherCategory.normalizedName);
    }


    /**
     * Returns a hash code consistent with equals.
     * Equal categories have equal hash codes.
     *
     * @return the hash code of the normalized name
     */
    @Override
    public int hashCode()
    {
        return normalizedName.hashCode();
    }


    /**
     * Returns the category name as readable text.
     *
     * @return the category name
     */
    @Override
    public String toString()
    {
        return name;
    }

}
