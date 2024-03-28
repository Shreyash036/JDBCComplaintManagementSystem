import java.sql.*;
import java.util.*;

public class ComplaintManagementSystem 
{

    Scanner sc = new Scanner(System.in);

    public static void main(String[] arg) 
    {
        ComplaintManagementSystem obj = new ComplaintManagementSystem();
        obj.customerComplaintOperations();
    }

    public void customerComplaintOperations() 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shreyashdemo", "root", "root123");
            boolean exitRequested = false;

            while (!exitRequested) 
            {
                System.out.println("1: Create customers table");
                System.out.println("2: Create complaints table");
                System.out.println("3: Insert customer");
                System.out.println("4: Insert complaint");
                System.out.println("5: View customers");
                System.out.println("6: View complaints");
                System.out.println("7: Get customer by ID");
                System.out.println("8: Get customer by name");
                System.out.println("9: Exit");
                System.out.println("Enter your choice:");
                int ch = Integer.parseInt(sc.nextLine()); // Use nextLine() to consume newline character

                switch (ch) 
                {
                    case 1:
                        createCustomersTable(conn);
                        break;
                    case 2:
                        createComplaintsTable(conn);
                        break;
                    case 3:
                        insertCustomer(conn);
                        break;
                    case 4:
                        insertComplaint(conn);
                        break;
                    case 5:
                        viewCustomers(conn);
                        break;
                    case 6:
                        viewComplaints(conn);
                        break;
                    case 7:
                        getCustomerById(conn);
                        break;
                    case 8:
                        getCustomerByName(conn);
                        break;
                    case 9:
                        conn.close(); // Close connection before exiting
                        exitRequested = true;
                        break;
                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
            }
        } 
        catch (Exception e) 
        {
            System.err.println(e.getMessage());
        }
    }

    public void createCustomersTable(Connection conn) throws SQLException 
    {
        String query = "create table customers (id int auto_increment primary key, name varchar(100) not null, email varchar(100) not null)";
        Statement st = conn.createStatement();
        st.execute(query);
        System.out.println("Customers table created Successfully");
    }

    public void createComplaintsTable(Connection conn) throws SQLException 
    {
        String query = "CREATE TABLE complaints (id INT AUTO_INCREMENT PRIMARY KEY, customer_id INT, description TEXT, status VARCHAR(50), FOREIGN KEY (customer_id) REFERENCES customers(id))";
        Statement st = conn.createStatement();
        st.execute(query);
        System.out.println("Complaints table created Successfully");
    }

    public void insertCustomer(Connection conn) throws SQLException 
    {
        String query = "INSERT INTO customers (name, email) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        String name, email;
        System.out.println("Enter customer name:");
        name = sc.nextLine();
        System.out.println("Enter customer email:");
        email = sc.nextLine();
        ps.setString(1, name);
        ps.setString(2, email);
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Customer added successfully");
        }
    }

    public void insertComplaint(Connection conn) throws SQLException 
    {
        String query = "INSERT INTO complaints (customer_id, description, status) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        int customerId;
        String description, status;
        System.out.println("Enter customer id:");
        customerId = Integer.parseInt(sc.nextLine());
        System.out.println("Enter complaint description:");
        description = sc.nextLine();
        System.out.println("Enter complaint status:");
        status = sc.nextLine();
        ps.setInt(1, customerId);
        ps.setString(2, description);
        ps.setString(3, status);
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Complaint added successfully");
        }
    }

    public void viewCustomers(Connection conn) throws SQLException 
    {
        String query = "SELECT * FROM customers";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        System.out.println("Customer ID\tCustomer Name\tCustomer Email");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "\t\t" + rs.getString("name") + "\t\t" + rs.getString("email"));
        }
    }

    public void viewComplaints(Connection conn) throws SQLException {
        String query = "SELECT c.id, c.description, c.status, cu.name AS customer_name FROM complaints c JOIN customers cu ON c.customer_id = cu.id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        System.out.println("Complaint ID\tDescription\tStatus\tCustomer Name");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "\t\t" + rs.getString("description") + "\t" + rs.getString("status") + "\t" + rs.getString("customer_name"));
        }
    }

    public void getCustomerById(Connection conn) throws SQLException 
    {
        System.out.println("Enter customer ID:");
        int customerId = Integer.parseInt(sc.nextLine());
        String query = "SELECT * FROM customers WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) 
        {
            System.out.println("Customer ID: " + rs.getInt("id"));
            System.out.println("Customer Name: " + rs.getString("name"));
            System.out.println("Customer Email: " + rs.getString("email"));
        } else 
        {
            System.out.println("Customer not found with ID: " + customerId);
        }
    }

    public void getCustomerByName(Connection conn) throws SQLException 
    {
        System.out.println("Enter customer name:");
        String customerName = sc.nextLine();
        String query = "SELECT * FROM customers WHERE name=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) 
        {
            System.out.println("Customer ID: " + rs.getInt("id"));
            System.out.println("Customer Name: " + rs.getString("name"));
            System.out.println("Customer Email: " + rs.getString("email"));
        } else 
        {
            System.out.println("Customer not found with name: " + customerName);
        }
    }
}
