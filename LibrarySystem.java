import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;


public class LibrarySystem {
    private List<Book> books;
    private List<Member> members;
    private List<LendingTransaction> transactions;

    
    public LibrarySystem() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void addBook(String title, String author, int bookID) {
        Book newBook = new Book(title, author, bookID);
        books.add(newBook);
    }

    public void registerMember(String name, int memberID) {
        Member newMember = new Member(name, memberID);
        members.add(newMember);
    }

    public void removeBook(String title) {
        // Iterate through the list of books and remove the first book with a matching title (case-insensitive)
        boolean bookFound = false;
        for (Iterator<Book> iterator = books.iterator(); iterator.hasNext(); ) {
            Book book = iterator.next();
            if (book.getTitle().equalsIgnoreCase(title)) {
                iterator.remove();
                System.out.println("Book removed successfully");
                bookFound = true;
                break;
            }
        }
        if (!bookFound) {
            System.out.println("Book not found");
        }
    }

    public void removeMember(String name) {
        // Iterate through the list of names and remove the first member who matches (case-insensitive)
        boolean memberFound = false;
        for (Iterator<Member> iterator = members.iterator(); iterator.hasNext(); ) {
            Member member = iterator.next();
            if (member.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                System.out.println("Member removed successfully");
                memberFound = true;
                break; // Exit the loop after removal
            }
        }
        if (!memberFound) {
            System.out.println("Member not found");
        }
    }

    public Book bookInformation(String title) {
        // Iterate through the list of books and print the first book with a matching title (case-insensitive)
        for (Book book : books) { 
            if (book.getTitle().equalsIgnoreCase(title)) {
                    return book;
            }
        }
        return null; // Return null if no matching book is found
    }

    public Member memberInformation(String name) {
        // Iterate through the list of members and return the first member with a matching name (case-insensitive)
        for (Member member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        // Return null if no matching member is found
        return null;
    }

    public void searchBookInformation(String title){

        Book book = bookInformation(title);
        if (book != null) {
            System.out.println("Book Information:");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Book ID: " + book.getBookID());
        }
        else {
            System.out.println("Book not found");
        }
    }

    public void searchMemberInformation(String name){
            
            Member member = memberInformation(name);
            if (member != null) {
                System.out.println("Member Information:");
                System.out.println("Name: " + member.getName());
                System.out.println("Member ID: " + member.getMemberID());
            }
            else {
                System.out.println("Member not found");
            }
    }

    public void displayBookNames() {
        // Iterate through the list of books and print the title of each book
        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }

    public void displayMemberNames() {
        // Iterate through the list of members and print the name of each member
        for (Member member : members) {
            System.out.println(member.getName());
        }
    }

    // ------------- Lending Transactions -----------------

    public void lendBook(String bookToLend, String memberName, Date dueDate) {
        Book book = bookInformation(bookToLend);
        Member member = memberInformation(memberName);

        if(book == null) {
            System.out.println("Error lending, Book not found");
            return;
        }
        if(member == null) {
            System.out.println("Error lending, Member not found");
            return;
        }
        LendingTransaction transaction = new LendingTransaction(book, member, dueDate);
        transactions.add(transaction);
        System.out.println("Book lent successfully");
    }

    public void returnBook(String bookReturned, String memberName, Date returnDate) {
        Book book = bookInformation(bookReturned);
        Member member = memberInformation(memberName);

        // Iterate through the list of transactions and find the first transaction with a matching book and member
        boolean transactionFound = false;
        for (Iterator<LendingTransaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            LendingTransaction transaction = iterator.next();
            if (transaction.getBook().equals(book) && transaction.getMember().equals(member)) {
                transaction.setReturnDate(returnDate);
                System.out.println("Book returned successfully");
                transactionFound = true;
                break;
            }
        }
        if (!transactionFound) {
            System.out.println("Error returning book, no matching transaction found");
        }
    }

    public void lendingInformation() {
        // Iterate through the list of transactions and print the details of each transaction
        for (LendingTransaction transaction : transactions) {
            System.out.println("Book: " + transaction.getBook().getTitle() + " (" + transaction.getBook().getBookID() + ")");
            System.out.println("Member: " + transaction.getMember().getName() + " (" + transaction.getMember().getMemberID() + ")");
            System.out.println("Due Date: " + transaction.getDueDate());

            if(transaction.getReturnDate() == null) {
                System.out.println("Book not returned yet");
            }
            else {
                System.out.println("Book returned");
                System.out.println("Return Date: " + transaction.getReturnDate());
            }
            // if return date is later than due date, print the number of days overdue and the fine amount
            if (transaction.getReturnDate() != null && transaction.getReturnDate().after(transaction.getDueDate())) {
                long diff = transaction.getReturnDate().getTime() - transaction.getDueDate().getTime();
                long daysOverdue = diff / (24 * 60 * 60 * 1000);
                System.out.println("Days overdue: " + daysOverdue); //Print number of days Overdue
                System.out.println("Fine Amount: Rs." + calculateFine(transaction, transaction.getReturnDate())); //Print fine amount
            }
            System.out.println();
        }
    }

    public void overdueBooks(Date checkingDate){ // iterate through the list and print transactions with due date more than checking date and return date null
        for (LendingTransaction transaction : transactions) {
            if (transaction.getDueDate().before(checkingDate) && transaction.getReturnDate() == null) {
                System.out.println("Book: " + transaction.getBook().getTitle() + " (" + transaction.getBook().getBookID() + ")");
                System.out.println("Member: " + transaction.getMember().getName() + " (" + transaction.getMember().getMemberID() + ")");
                System.out.println("Due Date: " + transaction.getDueDate());

                long diff = checkingDate.getTime() - transaction.getDueDate().getTime();
                long daysOverdue = diff / (24 * 60 * 60 * 1000);
                System.out.println("Days overdue: " + daysOverdue); //Print number of days Overdue
                System.out.println("Fines Due: Rs." + calculateFine(transaction, checkingDate)); //Print fine amount
                System.out.println();
            }
        }
    }

    public double calculateFine(LendingTransaction transaction, Date checkingDate) {
        long diff =  checkingDate.getTime() - transaction.getDueDate().getTime();
        long daysOverdue = diff / (24 * 60 * 60 * 1000);
        double fine = 0.0;

        if (daysOverdue > 0 && daysOverdue <= 7) {
            fine = daysOverdue * 50.0;
        }
        else if (daysOverdue > 7) {
            fine = 7 * 50.0 + (daysOverdue - 7) * 100.0;
        }

        return fine;
    }

    public void commandLineInterface(){
        System.out.println();
        System.out.println("*---------Welcome to the IS2104:RAD Library Management System---------*");
        System.out.println();
        System.out.println("Please select an option (Enter the option number):");
        System.out.println(" 1. Add a book");
        System.out.println(" 2. Register a member");
        System.out.println(" 3. Remove a book");
        System.out.println(" 4. Remove a member");
        System.out.println(" 5. Search for a book");
        System.out.println(" 6. Search for a member");
        System.out.println(" 7. Display all books");
        System.out.println(" 8. Display all members");
        System.out.println(" 9. Lend a book");
        System.out.println("10. Return a book");
        System.out.println("11. Display all lending transactions");
        System.out.println("12. Display all overdue books");
        System.out.println("13. Calculate Fine for a lending transaction");
        System.out.println(" 0. Exit");
        System.out.println();
        System.out.println("Enter your option: ");
    }

    // ------------- Main method -----------------

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Define date format

        LibrarySystem library = new LibrarySystem();
        int bookID_counter = 0;
        int memberID_counter = 0;
        
        boolean running = true;

        while(running){ 

            library.commandLineInterface(); // Implementation of the command line user interface
            int option;
            while (true) {
                try {
                    option = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Error: Please enter a valid integer value.");
                    scanner.nextLine(); // consume the invalid input
                }
            }
            scanner.nextLine(); // Consume the newline character

            switch(option){
                case 1:
                    System.out.println("---Adding New Book---");
                    System.out.println("Enter the title of the book:");
                    String title = scanner.nextLine();
                    System.out.println("Enter the author of the book:");
                    String author = scanner.nextLine();
                    if (title.isEmpty() || author.isEmpty()) {
                        System.out.println("Error: Title and author cannot be empty");
                        break;
                    }
                    bookID_counter++;
                    library.addBook(title, author, bookID_counter);
                    System.out.println("Book added successfully");
                    break;

                case 2:
                    System.out.println("---Adding New Member---");
                    System.out.println("Enter the name of the member:");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) {
                        System.out.println("Error: Name cannot be empty");
                        break;
                    }
                    memberID_counter++;
                    library.registerMember(name, memberID_counter);
                    System.out.println("Member registered successfully");
                    break;

                case 3:
                    System.out.println("---Removing a Book---");
                    System.out.println("Enter the title of the book to remove:");
                    String bookToRemove = scanner.nextLine();
                    library.removeBook(bookToRemove);
                    break;

                case 4:
                    System.out.println("---Removing a Member---");
                    System.out.println("Enter the name of the member to remove:");
                    String memberToRemove = scanner.nextLine();
                    library.removeMember(memberToRemove);
                    break;

                case 5:
                    System.out.println("---Search for a Book---");
                    System.out.println("Enter the title of the book to search:");
                    String bookToSearch = scanner.nextLine();
                    library.searchBookInformation(bookToSearch);
                    break;

                case 6:
                    System.out.println("---Search for a Member---");
                    System.out.println("Enter the name of the member to search:");
                    String memberToSearch = scanner.nextLine();
                    library.searchMemberInformation(memberToSearch);
                    break;

                case 7:
                    System.out.println("---Displaying Book Catalogue of the Library---");
                    library.displayBookNames();
                    break;

                case 8:
                    System.out.println("---Displaying list of Library Members---");
                    library.displayMemberNames();
                    break;

                case 9:
                    System.out.println("---Lending a Book---");
                    System.out.println("Enter the title of the book to lend:");
                    String bookToLend = scanner.nextLine();
                    System.out.println("Enter the name of the member:");
                    String memberName = scanner.nextLine();
                    System.out.println("Enter the due date (yyyy-MM-dd):");
                    String dueDateString = scanner.nextLine();
                    Date dueDate = null;
                    try {
                        // Set lenient to false to make the date parsing strict
                        dateFormat.setLenient(false);
                        dueDate = dateFormat.parse(dueDateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Cannot lend the book.");
                    }
            
                    if (dueDate != null) {
                        // Call the library.lendBook function only if the date is valid
                        library.lendBook(bookToLend, memberName, dueDate);
                    }
                    break;

                case 10:
                    System.out.println("---Returning a Book---");
                    System.out.println("Enter the title of the book to return:");
                    String bookToReturn = scanner.nextLine();
                    System.out.println("Enter the name of the member:");
                    String memberName2 = scanner.nextLine();
                    System.out.println("Enter the return date (yyyy-MM-dd):");
                    String returnDateString = scanner.nextLine();
                    Date returnDate = null;
                    try {
                        dateFormat.setLenient(false);
                        returnDate = dateFormat.parse(returnDateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Cannot return the book.");
                    }

                    if (returnDate != null) {
                        // Call the library.returnBook function only if the date is valid
                        library.returnBook(bookToReturn, memberName2, returnDate);
                    }
                    break;

                case 11:
                    System.out.println("---Displaying all Book Lending Inforamtion---");
                    library.lendingInformation();
                    break;

                case 12:
                    System.out.println("---Displaying all Overdue Books---");
                    System.out.println("Enter the checking date (yyyy-MM-dd):");
                    String checkingDateString = scanner.nextLine();
                    Date checkingDate = null;
                    try {
                        dateFormat.setLenient(false);
                        checkingDate = dateFormat.parse(checkingDateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Unable to show overdue books.");
                    }
                    if (checkingDate != null) {
                        library.overdueBooks(checkingDate);
                    }
                    break;

                case 13:
                    System.out.println("---Calculating Fine for a Lending Transaction---");
                    System.out.println("Enter the title of the book:");
                    String bookTitle = scanner.nextLine();
                    System.out.println("Enter the name of the member:");
                    String memberName3 = scanner.nextLine();
                    System.out.println("Enter the checking date (yyyy-MM-dd):");
                    String checkingDateString2 = scanner.nextLine();
                    Date checkingDate2 = null;

                    try {
                        dateFormat.setLenient(false);
                        checkingDate2 = dateFormat.parse(checkingDateString2);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Unable to calculate fine.");
                    }
                    Book book = library.bookInformation(bookTitle);
                    Member member = library.memberInformation(memberName3);
                    
                    Date dueDate2 = null;
                    for (LendingTransaction transaction : library.transactions) {
                        if (transaction.getBook().equals(book) && transaction.getMember().equals(member)) {
                            dueDate2 = transaction.getDueDate();
                            break;
                        }
                    }
                    if (checkingDate2 != null && dueDate2 != null) {
                        System.out.println("Fine amount: Rs." + library.calculateFine(new LendingTransaction(book, member, dueDate2), checkingDate2));
                    }
                    else {
                        System.out.println("No matching transaction found");
                    }
                    break;

                case 0:
                    System.out.println("---Thank you for using IS2104:RAD Library Management System---");
                    System.out.println("All user data will be lost.");
                    System.out.println("Exiting...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
                }
        
        }
        scanner.close();

    }
}
