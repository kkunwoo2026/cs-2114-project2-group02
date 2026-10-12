# PocketLedger

A personal monthly expense tracking program written in Java. It manages monthly budgets, tracks expenses across categories, and stores records using a custom generic LinkedChain data structure.

---

## How to Compile and Run the Program and Tests

1. Compile all source files:
javac *.java

2. Run the application:
java Main

3. Run the JUnit tests:
java -jar junit-platform-console-standalone.jar -cp . --scan-class-path

---

## Reflection

### 1. What changed from your specification?
During implementation, we switched our numeric fields (amounts and budgets) from primitive double to BigDecimal and changed dates from String to LocalDate. This was decided in group discussion to avoid floating-point rounding issues and ensure clean date validation using YearMonth. Also, our specification originally stated that only Transaction would use the chain, but we updated User so that Category also uses LinkedChain<Category> to properly reuse our generic class.

### 2. What changed in the LinkedChain?
We kept the core singly linked chain design and node structure as provided. The only change we made was adding an addToEnd(T newEntry) method, because the default add inserts at the head and ended up printing our predefined categories in reverse order. The rest of the app interacts strictly through the chain's public methods (add, remove, toArray, size, isEmpty) with no app logic leaked inside.

### 3. What happened to your unknowns?
In our specification, we wanted to move from an enum to a LinkedChain for categories to make future additions easier. We figured out how to store them and handle case-insensitive matching in findCategory(). We also solved how to safely convert our generic chain into typed arrays using toArray(T[] values) without casting errors. Dynamically adding new categories directly from user input in the console is still open.

### 4. What doesn't work yet?
The ledger currently runs entirely in memory, so all data resets when exiting the program. While Category supports new instances, Main doesn't yet have a menu option for users to type in and create new categories at runtime. Also, User.addExpense() doesn't block transactions that exceed the monthly budget; it only warns the user in the summary menu.

---

## Next Steps

### 1. What is left to finish the MVP?
First, implement dynamic category creation by adding a method in User and hooking it up to the console menu in Main. Second, complete all remaining normal and bad-input test cases across User, Transaction, and Category based on our test plan. Lastly, add a quick console warning right when an added expense puts the balance below zero.

### 2. Which stretch goals will you attempt?
- Committed: Spending breakdown by category percentages (from our spec) and simple CSV file saving/loading so records persist after closing the app.
- Dropped: Visual graphs and multi-month comparison, to keep our focus on code quality and passing all JUnit tests.

### 3. What new tests will you need?
We need tests for adding an expense with a date outside the current month, attempting to delete a transaction ID that doesn't exist, calling getExpenses() when the chain is empty, and rejecting invalid amounts (like zero, negative, or over $100,000). We also need a test verifying case-insensitive equality in Category.

### 4. Who does what?
- Kunwoo: Implemented User class and Usertest class.
- Andrew: Implemented Main class.
- Lena: Implemented Transaction class.
- Will: Unit tests for CategoryTest and helper logic for CSV storage.

---

## Attribution & GenAI Statement

### Attribution Table
| Class / Test File | Owner / Contributor | Role & Contribution |
| :--- | :--- | :--- |
| LinkedChain.java | Andrew Park | Implemented node operations, added addToEnd and toArray |
| Transaction.java | Lena | Domain validations (BigDecimal, date checks, amount limits) |
| Category.java | Will | Class design, case-insensitive equals and normalization |
| User.java | Kunwoo | Ledger logic, budget checks, array conversion, and sorting |
| Main.java | Andrew | Console loop, menu interaction, input handling |
| Test Files | Lena, Andrew, Kunwoo, Will | Writing test cases for normal and edge cases |

### Generative AI Note
GenAI was used as a brainstorming partner to verify edge cases for our test plan (such as empty chains and bad inputs for BigDecimal), double-check Big-O estimates from our specification, and help format our documentation. All design choices, class logic, and code implementations were written, tested, and reviewed by the team members.