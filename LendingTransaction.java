import java.util.Date;

public class LendingTransaction {
    private Book book;
    private Member member;
    private Date dueDate;
    private Date returnDate;

    public LendingTransaction(Book book, Member member, Date dueDate){
        this.book = book;
        this.member = member;
        this.dueDate = dueDate;
        this.returnDate = null; // Initialize return date as null
    }

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
