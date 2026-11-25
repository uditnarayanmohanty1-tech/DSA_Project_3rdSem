
import java.util.*;
import java.io.*;

public class BookCollectionManager {

    static ArrayList<Book> books = new ArrayList<>();
    static int nextId = 101;
    static String file = "collection.txt";

    // Book class
    static class Book {
        String id, title, author;
        int year;

        Book(String id, String title, String author, int year) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.year = year;
        }
    }

    // Add book
    static void addBook(Scanner sc) {
        System.out.print("Title: ");
        String title = sc.nextLine().trim();

        System.out.print("Author: ");
        String author = sc.nextLine().trim();

        System.out.print("Year: ");
        String y = sc.nextLine().trim();

        if (!y.matches("\\d{4}")) {
            System.out.println("Invalid year. Enter a 4-digit number.");
            return;
        }

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }

        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title) &&
                b.author.equalsIgnoreCase(author)) {
                System.out.println("Book already exists.");
                return;
            }
        }

        String id = "BC" + nextId++;
        int year = Integer.parseInt(y);

        books.add(new Book(id, title, author, year));

        System.out.println("Book added. ID: " + id + " | "
                + title + " | " + author + " | " + year);
    }

    // Display books
    static void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            System.out.println((i + 1) + ". " + b.id + " | "
                    + b.title + " | " + b.author + " | " + b.year);
        }
    }

    // Sort books using Insertion Sort
    static void sortBooks() {
        for (int i = 1; i < books.size(); i++) {
            Book key = books.get(i);
            int j = i - 1;

            while (j >= 0 &&
                books.get(j).title.compareToIgnoreCase(key.title) > 0) {
                books.set(j + 1, books.get(j));
                j--;
            }

            books.set(j + 1, key);
        }

        displayBooks();
    }

    // Search book by author
    static void searchBook(Scanner sc) {
        System.out.print("Enter author: ");
        String name = sc.nextLine().trim();

        boolean found = false;

        for (Book b : books) {
            if (b.author.toLowerCase().contains(name.toLowerCase())) {
                System.out.println("Found: " + b.id + " | "
                        + b.title + " | " + b.author + " | " + b.year);
                found = true;
            }
        }

        if (!found)
            System.out.println("Book not found.");
    }

    // Remove book by ID
    static void removeBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        String id = sc.nextLine().trim();

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id.equalsIgnoreCase(id)) {
                books.remove(i);
                System.out.println("Book removed successfully.");
                return;
            }
        }

        System.out.println("Book not found.");
    }

    // Load books from file
    static void loadBooks() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|", -1);

                if (d.length == 4) {
                    books.add(new Book(d[0], d[1], d[2],
                            Integer.parseInt(d[3])));

                    int id = Integer.parseInt(d[0].substring(2));
                    nextId = Math.max(nextId, id + 1);
                }
            }

            br.close();
        } catch (IOException e) {
            // File will be created when saving.
        }
    }

    // Save books to file
    static void saveBooks() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));

            for (Book b : books) {
                pw.println(b.id + "|" + b.title + "|"
                        + b.author + "|" + b.year);
            }

            pw.close();
        } catch (IOException e) {
            System.out.println("File saving error.");
        }
    }

    // Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        loadBooks();

        while (true) {
            System.out.println("\n--- BOOK COLLECTION MANAGER ---");
            System.out.println("1. Add Book");
            System.out.println("2. Display All");
            System.out.println("3. Sort by Title");
            System.out.println("4. Search by Author");
            System.out.println("5. Remove Book");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": addBook(sc); break;
                case "2": displayBooks(); break;
                case "3": sortBooks(); break;
                case "4": searchBook(sc); break;
                case "5": removeBook(sc); break;
                case "6":
                    saveBooks();
                    System.out.println("Data saved. Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
