package com.example.demo;
import java.util.*;

public class AccountFactory {
    public static BankAccount createAccount(String name, String phoneNum, String emailID,String aadar) {
        String accountNum = generateAccountNumber();
        return new BankAccount(accountNum,name,phoneNum, emailID, aadar);
    }

    private static String generateAccountNumber() {
    	 Random random = new Random();
         long randomNum = 100000000000L + (long)(random.nextDouble() * 900000000000L);
        return Long.toString(randomNum);
    }
}
