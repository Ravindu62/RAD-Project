class Book{
    private String title;
    private String author;
    private int bookID;

    public Book(String title, String author, int bookID){
        this.title = title;
        this.author = author;
        this.bookID = bookID;
    }

    // Getters and Setters
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookID() {
        return bookID;
    }
    
}