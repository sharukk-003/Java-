import java.sql.*;
import java.util.Scanner;
public class LibraryManagement {
    static final String URL = "jdbc:mysql://localhost:3306/company";
    static final String USER = "root";
    static final String PASSWORD = "test@123";   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            try ( // Establish Connection
                    Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                int choice;
                do {
                    
                    System.out.println("\n========== Employee Management ==========");
                    System.out.println("1. Add Book");
                    System.out.println("2. View Book");
                    System.out.println("3. Update Book price");
                    System.out.println("4. Delete Book");
                    System.out.println("5.Search Book");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");
                    choice = sc.nextInt();
                    switch(choice) {
                        case 1:
                            System.out.print("Enter Book ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter Book Name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter Author Name: ");
                            String author = sc.nextLine();
                            System.out.print("Enter Price: ");
                            double price = sc.nextDouble();
                            String insert = "INSERT INTO library VALUES(?,?,?,?)";
                            PreparedStatement ps = con.prepareStatement(insert);
                            ps.setInt(1, id);
                            ps.setString(2, name);
                            ps.setString(3, author);
                            ps.setDouble(4, price);
                            
                            int row = ps.executeUpdate();
                            if(row > 0)
                                System.out.println("Book Added Successfully.");
                            break;
                        case 2:
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("SELECT * FROM library");
                            System.out.println("\n--------------------------------------------");
                            System.out.println("ID\tName\tAuthor\tPrice");
                            System.out.println("--------------------------------------------");
                            while(rs.next()) {
                                System.out.println(
                                        rs.getInt("book_id") + "\t" +
                                                rs.getString("book_name") + "\t" +
                                                rs.getString("author") + "\t\t" +
                                                rs.getDouble("price"));
                            }
                            break;
                        case 3:
                            System.out.print("Enter Book ID: ");
                            int eid = sc.nextInt();
                            System.out.print("Enter New Price: ");
                            double Price = sc.nextDouble();
                            
                            String update = "UPDATE library SET price=? WHERE book_id=?";
                            PreparedStatement ps2 = con.prepareStatement(update);
                            ps2.setDouble(1, Price);
                            ps2.setInt(2, eid);
                            int updateRow = ps2.executeUpdate();
                            if(updateRow > 0)
                                System.out.println("Book Updated Successfully.");
                            else
                                System.out.println("Book Not Found.");
                            break;
                        case 4:
                            System.out.print("Enter Book ID: ");
                            int did = sc.nextInt();
                            String delete = "DELETE FROM library WHERE book_id=?";
                            PreparedStatement ps3 = con.prepareStatement(delete);
                            ps3.setInt(1, did);
                            int deleteRow = ps3.executeUpdate();
                            if(deleteRow > 0)
                                System.out.println("Book Deleted Successfully.");
                            else
                                System.out.println("Book Not Found.");
                            break;
                        case 5:
                            /*System.out.print("Enter Book ID: ");
                            int bid = sc.nextInt();
                            String search = "DELETE FROM library WHERE book_id=?";
                            PreparedStatement ps4 = con.prepareStatement(search);
                            ps4.setInt(1, bid);
                            int searchrow = ps4.executeUpdate();
                            if(searchrow > 0)
                                System.out.println("Book found Successfully.");
                            else
                                System.out.println("Book Not Found.");
                            break;
*/
                            System.out.print("Enter Book ID: ");
                            int bid = sc.nextInt();

                            // CORRECTED: Changed DELETE to SELECT query
                            String search = "SELECT * FROM library WHERE book_id=?";
                            try (PreparedStatement ps4 = con.prepareStatement(search)) {
                                ps4.setInt(1, bid);
                                try (ResultSet rs2 = ps4.executeQuery()) {
                                    if (rs2.next()) {
                                        System.out.println("\n--- Book Found ---");
                                        System.out.println("ID: " + rs2.getInt("book_id"));
                                        System.out.println("Name: " + rs2.getString("book_name"));
                                        System.out.println("Author: " + rs2.getString("author"));
                                        System.out.println("Price: " + rs2.getDouble("price"));
                                    } else {
                                        System.out.println("Book Not Found.");
                                    }
                                }
                            }
                            break;
                        case 6:
                            System.out.println("Thank You...");
                            break;
                        default:
                            System.out.println("Invalid Choice.");
                    }
                } while(choice != 6);
            }
            sc.close();
        }
        catch(ClassNotFoundException e) {
            System.out.println("MySQL Driver Not Found.");
        }
        catch(SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
