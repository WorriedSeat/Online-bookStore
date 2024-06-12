// Import necessary classes for handling arrays and input scanning
import java.util.ArrayList;
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookStore.getBookStore();
        while (true) {
            // Read a line of input from the user, split it into an array of strings
            String[] command = scanner.nextLine().split(" ");
 
            // Switch statement to handle different commands
            switch (command[0]) {
                case "createBook" -> BookStore.getBookStore().createBook(command[1], command[2], Float.parseFloat(command[3]));
                case "createUser" -> BookStore.getBookStore().createUser(command[1], command[2]);
                case "subscribe" -> BookStore.getBookStore().subscribe(command[1]);
                case "unsubscribe" -> BookStore.getBookStore().unsubscribe(command[1]);
                case "updatePrice" -> BookStore.getBookStore().changePrice(command[1], command[2]);
                case "readBook" -> BookStore.getBookStore().readBook(command[1], command[2]);
                case "listenBook" -> BookStore.getBookStore().listenBook(command[1], command[2]);
                case "end" -> System.exit(0);
            }
        }
    }
}
 
// Enum to represent the types of user accounts (premium or standard)
enum types {
    premium,
    standard
}
 
// Abstract class representing a user
abstract class User implements basicAccFunc {
    String name;
    types type;
 
    protected User(String name) {
        this.name = name;
    }
}
 
// Interface defining default behaviors for reading and listening to books
interface basicAccFunc {
    default void readBook(Book book, User user) {
        System.out.println(user.name + " reading " + book.title + " by " + book.author);
    }
 
    default void listenBook(Book book, User user) {
        System.out.println(user.name + " listening " + book.title + " by " + book.author);
    }
}
 
// Class representing a standard user
class StandardUser extends User {
    public StandardUser(String name) {
        super(name);
        this.type = types.standard;
    }
}
 
// Class representing a premium user
class PremiumUser extends User {
    public PremiumUser(String name) {
        super(name);
        this.type = types.premium;
    }
}
 
// Class representing a book with a title, author, and price
class Book {
    String title, author;
    float price;
 
    public Book(String title, String author, float price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }
 
    public void updatePrice(float price) {
        this.price = price;
    }
}
 
// Interface defining a method to check if a user has premium access
interface proxyForBookAccessibility {
    default boolean checkForPremium(User user) {
        if (user.type == types.premium)
            return true;
        else {
            System.out.println("No access");
            return false;
        }
    }
}
 
// Class representing the book store
class BookStore implements proxyForBookAccessibility {
    private static BookStore bookStore;
 
    // Lists to hold books, users, and subscribed users
    public ArrayList<Book> books;
    public ArrayList<User> users;
    public ArrayList<User> subscribed;
 
    // Private constructor to ensure only one instance of BookStore is created
    private BookStore() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        subscribed = new ArrayList<>();
    }
 
    // Static method to get the singleton instance of BookStore
    public static BookStore getBookStore() {
        if (bookStore == null) {
            bookStore = new BookStore();
        }
        return bookStore;
    }
 
    // Method to create a new book in the book store
    public void createBook(String title, String author, float price) {
        for (Book book : books) {
            if (book.title.equals(title)) {
                System.out.println("Book already exists");
                return;
            }
        }
        books.add(new Book(title, author, price));
    }
 
    // Method to create a new user in the book store
    public void createUser(String type, String name) {
        for (User user : users) {
            if (user.name.equals(name)) {
                System.out.println("User already exists");
                return;
            }
        }
        if (type.equals("standard"))
            users.add(new StandardUser(name));
        if (type.equals("premium"))
            users.add(new PremiumUser(name));
    }
 
    // Method to subscribe a user to the book store
    public void subscribe(String username) {
        for (User user : users) {
            if (user.name.equals(username)) {
                if(!subscribed.contains(user))
                    subscribed.add(user);
                else
                    System.out.println("User already subscribed");
                return;
            }
 
        }
    }
 
    // Method to unsubscribe a user from the book store
    public void unsubscribe(String username) {
        for (User user : users) {
            if (user.name.equals(username)) {
                if(subscribed.contains(user))
                    subscribed.remove(user);
                else
                    System.out.println("User is not subscribed");
                return;
            }
        }
    }
 
    // Method to change the price of a book and notify subscribed users
    public void changePrice(String title, String price) {
        for (User user : subscribed) {
            System.out.println(user.name + " notified about price update for " + title + " to " + price);
        }
        for (Book book : books) {
            if (book.title.equals(title)) {
                book.updatePrice(Float.parseFloat(price));
                return;
            }
        }
    }
 
    // Method to allow a user to read a book
    public void readBook(String username, String title) {
        for (User user : users) {
            if (user.name.equals(username)) {
                for (Book book : books) {
                    if (book.title.equals(title)) {
                        user.readBook(book, user);
                        return;
                    }
                }
            }
        }
    }
    
// Method to allow a user to listen to a book, if they have premium access
    public void listenBook(String username, String title) {
        for (User user : users) {
            if (user.name.equals(username)) {
                if (checkForPremium(user)) {
                    for (Book book : books) {
                        if (book.title.equals(title)) {
                            user.listenBook(book, user);
                            return;
                        }
                    }
                } else return;
            }
        }
    }
}
