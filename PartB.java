import java.sql.*;
import java.util.Scanner;

public class PartB {
    static final String URL = "jdbc:mysql://localhost:3306/college_db";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            while (true) {
                System.out.println("\n1. Insert Product\n2. Display Products\n3. Update Product Price\n4. Delete Product\n5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: insertProduct(con, sc); break;
                    case 2: displayProducts(con); break;
                    case 3: updateProduct(con, sc); break;
                    case 4: deleteProduct(con, sc); break;
                    case 5: con.close(); sc.close(); return;
                    default: System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertProduct(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter product name: ");
        String name = sc.next();
        System.out.print("Enter product price: ");
        double price = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement("INSERT INTO product(name, price) VALUES (?, ?)");
        ps.setString(1, name);
        ps.setDouble(2, price);
        ps.executeUpdate();
        System.out.println("Product Added Successfully!");
    }

    static void displayProducts(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product");

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                               ", Name: " + rs.getString("name") +
                               ", Price: " + rs.getDouble("price"));
        }
    }

    static void updateProduct(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Product ID to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new price: ");
        double price = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement("UPDATE product SET price=? WHERE id=?");
        ps.setDouble(1, price);
        ps.setInt(2, id);
        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("Product Updated Successfully!");
        else
            System.out.println("Product Not Found!");
    }

    static void deleteProduct(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement("DELETE FROM product WHERE id=?");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("Product Deleted Successfully!");
        else
            System.out.println("Product Not Found!");
    }
}
