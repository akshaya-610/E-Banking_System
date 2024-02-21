package com.example.demo;

import java.sql.SQLException;

public interface TransactionDAO {
	public  void save(Transaction trans) throws SQLException;
	public String generateTransactionId();
}
