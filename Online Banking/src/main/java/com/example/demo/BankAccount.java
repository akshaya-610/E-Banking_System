package com.example.demo;

import java.sql.*;
import java.util.*;
public class BankAccount {
    private int id;
    private String name;
    private String contact;
    private String email;
    private String aadharNumber;
    private String accountNumber;
    

    // Constructor for creating a BankAccount instance from an existing record in the database
    BankAccount(String name, String contact, String email, String aadharNumber) {
        
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.aadharNumber = aadharNumber;
        
    }
   
 
	public BankAccount(String accountNumber, String name, String contact, String email, String aadharNumber) {
		super();
	
		this.name = name;
		this.contact = contact;
		this.email = email;
		this.aadharNumber = aadharNumber;
		this.accountNumber = accountNumber;
	}
	// Getters and setters for all fields
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }
    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    

    // Save the BankAccount record to the database
    public void save() throws SQLException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection conn = dbConnection.getConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	String sql = "INSERT INTO accounts (accountNum ,name, contact, email, aadhar_number) VALUES (?, ?, ?, ?,?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, accountNumber);
            stmt.setString(2, name);
            stmt.setString(3, contact);
            stmt.setString(4, email);
            stmt.setString(5, aadharNumber);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception
        	e.printStackTrace();
        	
        } finally {
            // Close the statement and result set
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // Handle the exception
                	e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Handle the exception
                	e.printStackTrace();
                }
            }
        }
    }

//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_management_system", "root", "");
//            String createTableQuery = "CREATE TABLE IF NOT EXISTS accounts (accountNum VARCHAR(36) PRIMARY KEY, name VARCHAR(255), contact VARCHAR(255), email VARCHAR(255), aadhar_number VARCHAR(255))";
//            stmt = conn.prepareStatement(createTableQuery);
//            stmt.executeUpdate();
//            // Insert the new record into the bank_accounts table
//            String sql = "INSERT INTO accounts (accountNum ,name, contact, email, aadhar_number) VALUES (?, ?, ?, ?,?)";
//            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            stmt.setString(1, accountNumber);
//            stmt.setString(2, name);
//            stmt.setString(3, contact);
//            stmt.setString(4, email);
//            stmt.setString(5, aadharNumber);
//            
//            stmt.executeUpdate();
//            
//            // Retrieve the ID of the new record and set it on this BankAccount instance
//            rs = stmt.getGeneratedKeys();
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close the database resources
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stmt != null) {
//                    stmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    
    
    public static List<BankAccount> getAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BankAccount> bankAccounts = new ArrayList<>();

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_management_system", "root", "");
            String sql = "SELECT * FROM bank_accounts";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                BankAccount bankAccount = new BankAccount(
                		rs.getString("accountNum"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("aadhar_number")
                       
                );
                bankAccounts.add(bankAccount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bankAccounts;
    }
  

}
