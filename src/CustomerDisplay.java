import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class CustomerDisplay extends JFrame {

    String[] columnNames = {"Customer Id", "Last Name", "First Name", "City", "Country", "Email", "Subscription Date"};
    JTable table;
    JButton filterButton;
    DisplayList dl = null;
    List<Customer> customers;
    String dbUrl = null;

    public CustomerDisplay(String dbUrl) {
        this.dbUrl = dbUrl;
        this.setSize(1024, 800);
        this.customers = fetchCustomersFromDatabase(dbUrl);
        this.add(createControlPanel(), BorderLayout.NORTH);
        this.add(createDisplayPanel(customers), BorderLayout.CENTER);
    }

    private List<Customer> fetchCustomersFromDatabase(String dbUrl) {
        List<Customer> customers = new ArrayList<>();
        String selectSQL = "SELECT customer_unique_id, first_name, last_name, company, city, country, phone1, phone2, email, subscription_date, website FROM testdb";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("customer_unique_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("company"),
                        rs.getString("city"),
                        rs.getString("country"),
                        rs.getString("phone1"),
                        rs.getString("phone2"),
                        rs.getString("email"),
                        rs.getString("subscription_date"),
                        rs.getString("website")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public JPanel createControlPanel() {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BorderLayout());
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(2, 3));
        JLabel lastName = new JLabel("Last Name");
        JLabel firstName = new JLabel("First Name");
        JLabel city = new JLabel("City");
        JLabel country = new JLabel("Country");
        JLabel dateFrom = new JLabel("Date From");
        JLabel dateTo = new JLabel("Date To");
        JTextField lastNameField = new JTextField(10);
        JTextField firstNameField = new JTextField(10);
        JTextField cityField = new JTextField(10);
        JTextField countryField = new JTextField(10);
        JTextField dateFromField = new JTextField(10);
        JTextField dateToField = new JTextField(10);
        JPanel p1 = new JPanel();
        p1.add(lastName);
        p1.add(lastNameField);
        JPanel p2 = new JPanel();
        p2.add(firstName);
        p2.add(firstNameField);
        JPanel p3 = new JPanel();
        p3.add(city);
        p3.add(cityField);
        JPanel p4 = new JPanel();
        p4.add(country);
        p4.add(countryField);
        JPanel p5 = new JPanel();
        p5.add(dateFrom);
        p5.add(dateFromField);
        JPanel p6 = new JPanel();
        p6.add(dateTo);
        p6.add(dateToField);
        fieldPanel.add(p1);
        fieldPanel.add(p2);
        fieldPanel.add(p3);
        fieldPanel.add(p4);
        fieldPanel.add(p5);
        fieldPanel.add(p6);
        newPanel.add(fieldPanel, BorderLayout.CENTER);

        filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            String lastNameFilter = lastNameField.getText().trim();
            String firstNameFilter = firstNameField.getText().trim();
            String cityFilter = cityField.getText().trim();
            String countryFilter = countryField.getText().trim();
            String dateFromFilter = dateFromField.getText().trim();
            String dateToFilter = dateToField.getText().trim();

            List<Customer> filteredCustomers = fetchCustomersFromDatabaseWithFilter(dbUrl, lastNameFilter, firstNameFilter, cityFilter, countryFilter, dateFromFilter, dateToFilter);
            refreshTable(filteredCustomers);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(filterButton);
        newPanel.add(buttonPanel, BorderLayout.SOUTH);
        return newPanel;
    }

    public JPanel createDisplayPanel(List<Customer> customers) {
        JPanel displayPanel = new JPanel();

        String[][] data = new String[customers.size()][7];
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getCustomerId();
            data[i][1] = customer.getLastName();
            data[i][2] = customer.getFirstName();
            data[i][3] = customer.getCity();
            data[i][4] = customer.getCountry();
            data[i][5] = customer.getEmail();
            data[i][6] = customer.getSubscriptionDate();
        }

        table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(900, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Customer selectedCustomer = customers.get(selectedRow);
                    if (dl != null) {
                        dl.setVisible(false);
                        dl = null;
                    }
                    dl = new DisplayList(selectedCustomer);
                    dl.setVisible(true);
                }
            }
        });

        displayPanel.add(scrollPane);
        return displayPanel;
    }

    private List<Customer> fetchCustomersFromDatabaseWithFilter(String dbUrl, String lastName, String firstName, String city, String country, String dateFrom, String dateTo) {
        List<Customer> customers = new ArrayList<>();
        StringBuilder selectSQL = new StringBuilder("SELECT customer_unique_id, first_name, last_name, company, city, country, phone1, phone2, email, subscription_date, website FROM testdb WHERE 1=1");
        if (!lastName.isEmpty()) {
            selectSQL.append(" AND last_name LIKE ?");
        }
        if (!firstName.isEmpty()) {
            selectSQL.append(" AND first_name LIKE ?");
        }
        if (!city.isEmpty()) {
            selectSQL.append(" AND city LIKE ?");
        }
        if (!country.isEmpty()) {
            selectSQL.append(" AND country LIKE ?");
        }
        if (!dateFrom.isEmpty()) {
            selectSQL.append(" AND subscription_date >= ?");
        }
        if (!dateTo.isEmpty()) {
            selectSQL.append(" AND subscription_date <= ?");
        }

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL.toString())) {

            int paramIndex = 1;
            if (!lastName.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + lastName + "%");
            }
            if (!firstName.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + firstName + "%");
            }
            if (!city.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + city + "%");
            }
            if (!country.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + country + "%");
            }
            if (!dateFrom.isEmpty()) {
                pstmt.setString(paramIndex++, dateFrom);
            }
            if (!dateTo.isEmpty()) {
                pstmt.setString(paramIndex++, dateTo);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getString("customer_unique_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("company"),
                            rs.getString("city"),
                            rs.getString("country"),
                            rs.getString("phone1"),
                            rs.getString("phone2"),
                            rs.getString("email"),
                            rs.getString("subscription_date"),
                            rs.getString("website")
                    );
                    customers.add(customer);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public void refreshTable(List<Customer> customers) {
        String[][] data = new String[customers.size()][7];
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getCustomerId();
            data[i][1] = customer.getLastName();
            data[i][2] = customer.getFirstName();
            data[i][3] = customer.getCity();
            data[i][4] = customer.getCountry();
            data[i][5] = customer.getEmail();
            data[i][6] = customer.getSubscriptionDate();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public static void main(String[] args) {
        JFrame customerDisplay = new CustomerDisplay("jdbc:sqlite:/Users/lancelot/Documents/NYU/study/ECE_Java/Assignment9/Assignment9/corporatedata.db");
        customerDisplay.setVisible(true);
        customerDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}