import java.sql.*;
import java.util.*;

class Student {
    private int id;
    private String name;
    private int age;

    public Student(int id, String name, int age) { this.id = id; this.name = name; this.age = age; }
    public Student(String name, int age) { this.name = name; this.age = age; }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
}

class StudentDAO {
    private Connection con;

    public StudentDAO() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db", "root", "root");
    }

    public void addStudent(Student s) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO students(name, age) VALUES(?, ?)");
        ps.setString(1, s.getName());
        ps.setInt(2, s.getAge());
        ps.executeUpdate();
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM students");
        while (rs.next()) {
            list.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("age")));
        }
        return list;
    }

    public void updateStudent(int id, String newName, int newAge) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE students SET name=?, age=? WHERE id=?");
        ps.setString(1, newName);
        ps.setInt(2, newAge);
        ps.setInt(3, id);
        ps.executeUpdate();
    }

    public void deleteStudent(int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public void close() throws SQLException { con.close(); }
}

public class PartC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            StudentDAO dao = new StudentDAO();

            while (true) {
                System.out.println("\n1. Add Student\n2. View Students\n3. Update Student\n4. Delete Student\n5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: "); String name = sc.next();
                        System.out.print("Enter age: "); int age = sc.nextInt();
                        dao.addStudent(new Student(name, age));
                        System.out.println("Student Added!"); break;
                    case 2:
                        List<Student> list = dao.getAllStudents();
                        for (Student s : list)
                            System.out.println("ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge());
                        break;
                    case 3:
                        System.out.print("Enter Student ID: "); int idU = sc.nextInt();
                        System.out.print("Enter new name: "); String newName = sc.next();
                        System.out.print("Enter new age: "); int newAge = sc.nextInt();
                        dao.updateStudent(idU, newName, newAge);
                        System.out.println("Student Updated!"); break;
                    case 4:
                        System.out.print("Enter Student ID to delete: "); int idD = sc.nextInt();
                        dao.deleteStudent(idD);
                        System.out.println("Student Deleted!"); break;
                    case 5:
                        dao.close(); sc.close(); return;
                    default: System.out.println("Invalid Choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
