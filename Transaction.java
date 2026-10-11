import java.time.LocalDate;
import java.math.BigDecimal;


public class Transaction
{
    //~ Fields ................................................................

    private String id;
    private LocalDate date;
    private Category category;
    private String description;
    private BigDecimal amount;
    
    //~ Constructors ..........................................................

    public Transaction(String id, LocalDate date, Category category,
        String description, BigDecimal amount)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException("Invalid transaction ID");
            
        }
        if (date == null)
        {
            throw new IllegalArgumentException("Please input a valid date");
        }
        if (category == null)
        {
            throw new IllegalArgumentException("Please input a valid category");
        }
        if (description == null || description.trim().isEmpty())
        {
            throw new IllegalArgumentException("Please input a valid description");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) 
        {
            throw new IllegalArgumentException("Please enter a valid form of expense");
            
        }
        if (amount.compareTo(new BigDecimal("100000")) > 0)
        {
            throw new IllegalArgumentException("Suspicious expense");
        }
               
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }
    //~Public  Methods ........................................................

    public String getId()
    {
        return id;
    }
    public LocalDate getDate()
    {
        return date;
    }
    public Category getCategory()
    {
        return category;
    }
    public String getDescription()
    {
        return description;
    }
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        
        if (!(other instanceof Transaction))
        {
            return false;
        }
        
        Transaction otherTransaction = (Transaction)other;
        return id.equals(otherTransaction.id);
    }
    
    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
    
    @Override
    public String toString()
    {
        return "ID: " + id + ", Date: " + date + ", Category: " + category
            + ", Description: " + description + ", Amount: $" + amount;
    }
}
