import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

public class DisplayList extends JFrame {
	JLabel lastName = new JLabel("Last Name");
	JTextField lastNameField = new JTextField(10);
	JLabel firstName = new JLabel("First Name");
	JTextField firstNameField = new JTextField(10);
	JLabel company = new JLabel("Company");
	JTextField companyField = new JTextField(20);
	JLabel phone1 = new JLabel("Phone 1");
	JTextField phone1Field = new JTextField(20);
	JLabel phone2 = new JLabel("Phone 2");
	JTextField phone2Field = new JTextField(20);
	JLabel city = new JLabel("City");
	JTextField cityField = new JTextField(10);
	JLabel country = new JLabel("Country");
	JTextField countryField = new JTextField(10);
	JLabel email = new JLabel("Email");
	JTextField emailField = new JTextField(20);
	JLabel subDate = new JLabel("Subscription Date");
	JTextField subDateField = new JTextField(10);
	JLabel website = new JLabel("Website");
	JTextField websiteField = new JTextField(20);

	private void updateCustomer(Customer customer) {
		// Get updated values from input fields
		customer.setFirstName(firstNameField.getText());
		customer.setLastName(lastNameField.getText());
		customer.setCompany(companyField.getText());
		customer.setCity(cityField.getText());
		customer.setCountry(countryField.getText());
		customer.setPhone1(phone1Field.getText());
		customer.setPhone2(phone2Field.getText());
		customer.setEmail(emailField.getText());
		customer.setSubscriptionDate(subDateField.getText());
		customer.setWebsite(websiteField.getText());

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/path/database.db")) {
			String updateSQL = "UPDATE testdb SET first_name = ?, last_name = ?, company = ?, city = ?, country = ?, phone1 = ?, phone2 = ?, email = ?, subscription_date = ?, website = ? WHERE customer_unique_id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
				pstmt.setString(1, customer.getFirstName());
				pstmt.setString(2, customer.getLastName());
				pstmt.setString(3, customer.getCompany());
				pstmt.setString(4, customer.getCity());
				pstmt.setString(5, customer.getCountry());
				pstmt.setString(6, customer.getPhone1());
				pstmt.setString(7, customer.getPhone2());
				pstmt.setString(8, customer.getEmail());
				pstmt.setString(9, customer.getSubscriptionDate());
				pstmt.setString(10, customer.getWebsite());
				pstmt.setString(11, customer.getCustomerId());
				pstmt.executeUpdate();
				System.out.println("Customer updated successfully.");
			}
		} catch (SQLException e) {
			System.err.println("SQL error: " + e.getMessage());
			e.printStackTrace();
		}


//		// Update the customer in the list
//		for (int i = 0; i < customers.size(); i++) {
//			if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
//				customers.set(i, customer);
//				break;
//			}
//		}

	}
	private void deleteCustomer(Customer customer) {
		System.out.println("Deleting customer: " + customer.getCustomerId());
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/path/database.db")) {
			String deleteSQL = "DELETE FROM testdb WHERE customer_unique_id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
				pstmt.setString(1, customer.getCustomerId());
				pstmt.executeUpdate();
				System.out.println("Customer deleted successfully.");
			}
		} catch (SQLException e) {
			System.err.println("SQL error: " + e.getMessage());
			e.printStackTrace();
		}

		dispose();
	}


	public DisplayList(Customer customer) {

		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(4, 3));
		JLabel customerId = new JLabel("Customer ID");
		JTextField customerIdField = new JTextField(15);
		customerIdField.setText(customer.getCustomerId());
		customerIdField.setEditable(true);

		lastNameField.setText(customer.getLastName());
		lastNameField.setEditable(true);

		firstNameField.setText(customer.getFirstName());
		firstNameField.setEditable(true);

		companyField.setText(customer.getCompany());
		companyField.setEditable(true);

		phone1Field.setText(customer.getPhone1());
		phone1Field.setEditable(true);

		phone2Field.setText(customer.getPhone2());
		phone2Field.setEditable(true);

		cityField.setText(customer.getCity());
		cityField.setEditable(true);

		countryField.setText(customer.getCountry());
		countryField.setEditable(true);

		emailField.setText(customer.getEmail());
		emailField.setEditable(true);

		subDateField.setText(customer.getSubscriptionDate());
		subDateField.setEditable(true);

		websiteField.setText(customer.getWebsite());
		websiteField.setEditable(true);

		JPanel dp1 = new JPanel();
		dp1.add(customerId);
		dp1.add(customerIdField);
		dataPanel.add(dp1);

		JPanel dp2 = new JPanel();
		dp2.add(lastName);
		dp2.add(lastNameField);
		dataPanel.add(dp2);

		JPanel dp3 = new JPanel();
		dp3.add(firstName);
		dp3.add(firstNameField);
		dataPanel.add(dp3);

		JPanel dp4 = new JPanel();
		dp4.add(company);
		dp4.add(companyField);
		dataPanel.add(dp4);
		JPanel dp5 = new JPanel();
		dp5.add(city);
		dp5.add(cityField);
		dataPanel.add(dp5);

		JPanel dp6 = new JPanel();
		dp6.add(country);
		dp6.add(countryField);
		dataPanel.add(dp6);

		JPanel dp7 = new JPanel();
		dp7.add(phone1);
		dp7.add(phone1Field);
		dataPanel.add(dp7);

		JPanel dp8 = new JPanel();
		dp8.add(phone2);
		dp8.add(phone2Field);
		dataPanel.add(dp8);

		JPanel dp9 = new JPanel();
		dp9.add(email);
		dp9.add(emailField);
		dataPanel.add(dp9);

		JPanel dp10 = new JPanel();
		dp10.add(subDate);
		dp10.add(subDateField);
		dataPanel.add(dp10);
		JPanel dp11 = new JPanel();
		dp11.add(website);
		dp11.add(websiteField);
		dataPanel.add(dp11);

		this.add(dataPanel);
		JPanel buttonPanel = new JPanel();
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateCustomer(customer));
		buttonPanel.add(updateButton);
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> deleteCustomer(customer));
		buttonPanel.add(deleteButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> dispose());
		buttonPanel.add(cancelButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(800, 400);

		// Set the layout for JFrame to ensure all components are correctly placed
		this.setLayout(new BorderLayout());
		this.add(dataPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		// Make sure the frame is set up correctly
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	
	public static void main(String[] args) {

	}
}
