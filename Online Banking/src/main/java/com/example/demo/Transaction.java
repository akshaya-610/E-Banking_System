package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String transactionType;
    private double amount;
    private Date transactionDate;


    public Transaction(String accountNumber, String transactionType, double amount) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        
        this.transactionId = generateTransactionId();
        this.transactionDate = new Date();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    private String generateTransactionId() {
        // generate a unique transaction id using UUID
        return UUID.randomUUID().toString();
    }

    public void save() throws SQLException {
//    	  Connection connection = null;
//          PreparedStatement stmt = null;
//          ResultSet rs = null;
//          connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_management_system", "root", "");
       
    	DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    	Connection connection = dbConnection.getConnection();

    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		// create the transaction table if it doesn't exist
            String createTableSql = "CREATE TABLE IF NOT EXISTS transaction "
                    + "(transaction_id VARCHAR(36) PRIMARY KEY, "
                    + "account_number VARCHAR(36) NOT NULL, "
                    + "transaction_type VARCHAR(10) NOT NULL, "
                    + "amount DOUBLE NOT NULL, "
                    + "transaction_date TIMESTAMP NOT NULL)";
            PreparedStatement createTableStmt = connection.prepareStatement(createTableSql);
            createTableStmt.executeUpdate();
            // create the balance table if it doesn't exist
          String createBalanceTableSql = "CREATE TABLE IF NOT EXISTS balance "
                  + "(account_number VARCHAR(12) PRIMARY KEY, "
                  + "current_balance DOUBLE NOT NULL)";
          PreparedStatement createBalanceTableStmt = connection.prepareStatement(createBalanceTableSql);
          createBalanceTableStmt.executeUpdate();

            // insert the new transaction record into the transaction table
            String insertSql = "INSERT INTO transaction "
                    + "(transaction_id, account_number, transaction_type, amount, transaction_date) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, transactionId);
            insertStmt.setString(2, accountNumber);
            insertStmt.setString(3, transactionType);
            insertStmt.setDouble(4, amount);
            insertStmt.setTimestamp(5, new java.sql.Timestamp(transactionDate.getTime()));
            insertStmt.executeUpdate();
            
         // update the balance table based on the transaction type

            String updateBalanceSql = "INSERT INTO balance "
                    + "(account_number, current_balance) "
                    + "VALUES (?, ?) "
                    + "ON DUPLICATE KEY UPDATE "
                    + "current_balance = "
                    + "IF((SELECT transaction_type FROM transaction WHERE account_number = ? ORDER BY transaction_date DESC LIMIT 1) = 'Deposit', "
                    + "current_balance + ?, "
                    + "current_balance - ?)";


            PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);

          updateBalanceStmt.setString(1, accountNumber);
          updateBalanceStmt.setDouble(2, amount);
          updateBalanceStmt.setString(3, accountNumber);
          updateBalanceStmt.setDouble(4, amount);
          updateBalanceStmt.setDouble(5, amount);

          updateBalanceStmt.executeUpdate();


    	    
    	} catch (SQLException e) {
    	    // Handle the exception
    	} finally {
    	    // Close the statement and result set
    	    if (rs != null) {
    	        try {
    	            rs.close();
    	        } catch (SQLException e) {
    	            // Handle the exception
    	        }
    	    }
    	    if (stmt != null) {
    	        try {
    	            stmt.close();
    	        } catch (SQLException e) {
    	            // Handle the exception
    	        }
    	    }
    	}
    }
}

//    	// create the transaction table if it doesn't exist
//        String createTableSql = "CREATE TABLE IF NOT EXISTS transaction "
//                + "(transaction_id VARCHAR(36) PRIMARY KEY, "
//                + "account_number VARCHAR(36) NOT NULL, "
//                + "transaction_type VARCHAR(10) NOT NULL, "
//                + "amount DOUBLE NOT NULL, "
//                + "transaction_date TIMESTAMP NOT NULL)";
//        PreparedStatement createTableStmt = connection.prepareStatement(createTableSql);
//        createTableStmt.executeUpdate();
//        // create the balance table if it doesn't exist
//      String createBalanceTableSql = "CREATE TABLE IF NOT EXISTS balance "
//              + "(account_number VARCHAR(12) PRIMARY KEY, "
//              + "current_balance DOUBLE NOT NULL)";
//      PreparedStatement createBalanceTableStmt = connection.prepareStatement(createBalanceTableSql);
//      createBalanceTableStmt.executeUpdate();
//
//        // insert the new transaction record into the transaction table
//        String insertSql = "INSERT INTO transaction "
//                + "(transaction_id, account_number, transaction_type, amount, transaction_date) "
//                + "VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement insertStmt = connection.prepareStatement(insertSql);
//        insertStmt.setString(1, transactionId);
//        insertStmt.setString(2, accountNumber);
//        insertStmt.setString(3, transactionType);
//        insertStmt.setDouble(4, amount);
//        insertStmt.setTimestamp(5, new java.sql.Timestamp(transactionDate.getTime()));
//        insertStmt.executeUpdate();
//        
//     // update the balance table based on the transaction type
//
//        String updateBalanceSql = "INSERT INTO balance "
//                + "(account_number, current_balance) "
//                + "VALUES (?, ?) "
//                + "ON DUPLICATE KEY UPDATE "
//                + "current_balance = "
//                + "IF((SELECT transaction_type FROM transaction WHERE account_number = ? ORDER BY transaction_date DESC LIMIT 1) = 'Deposit', "
//                + "current_balance + ?, "
//                + "current_balance - ?)";
//
//
//        PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);
//
//      updateBalanceStmt.setString(1, accountNumber);
//      updateBalanceStmt.setDouble(2, amount);
//      updateBalanceStmt.setString(3, accountNumber);
//      updateBalanceStmt.setDouble(4, amount);
//      updateBalanceStmt.setDouble(5, amount);
//
//      updateBalanceStmt.executeUpdate();
//
    


