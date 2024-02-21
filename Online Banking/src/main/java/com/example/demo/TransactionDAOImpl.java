package com.example.demo;

import java.sql.Connection;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.List;
public  class TransactionDAOImpl implements TransactionDAO {
	
	public TransactionDAOImpl() {
		System.out.println("hi");
	}
	public String generateTransactionId() {
        // generate a unique transaction id using UUID
        return UUID.randomUUID().toString();
    }
	
	 public  void  save(Transaction trans) throws SQLException {
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
	            String insertSql = "INSERT INTO transaction "
	                    + "(transaction_id, account_number, transaction_type, amount, transaction_date) "
	                    + "VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
	            insertStmt.setString(1, trans.getTransactionId());
	            insertStmt.setString(2, trans.getAccountNumber());
	            insertStmt.setString(3, trans.getTransactionType());
	            insertStmt.setDouble(4, trans.getAmount());
	            insertStmt.setTimestamp(5, new java.sql.Timestamp(trans.getTransactionDate().getTime()));
	            insertStmt.executeUpdate();// update the balance table based on the transaction type

	            String updateBalanceSql = "INSERT INTO balance "
	                    + "(account_number, current_balance) "
	                    + "VALUES (?, ?) "
	                    + "ON DUPLICATE KEY UPDATE "
	                    + "current_balance = "
	                    + "IF((SELECT transaction_type FROM transaction WHERE account_number = ? ORDER BY transaction_date DESC LIMIT 1) = 'Deposit', "
	                    + "current_balance + ?, "
	                    + "current_balance - ?)";


	            PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);

	          updateBalanceStmt.setString(1, trans.getAccountNumber());
	          updateBalanceStmt.setDouble(2, trans.getAmount());
	          updateBalanceStmt.setString(3, trans.getAccountNumber());
	          updateBalanceStmt.setDouble(4, trans.getAmount());
	          updateBalanceStmt.setDouble(5, trans.getAmount());

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

