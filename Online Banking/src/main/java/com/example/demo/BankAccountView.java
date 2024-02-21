package com.example.demo;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//public class BankAccountView {
//    
//    private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
//    
//    @GetMapping("/hi")
//    public String showForm(Model model) {
//        model.addAttribute("bankAccounts", bankAccounts);
//        return "form";
//    }
//
//    @PostMapping("/save")
//    public String saveBankAccount(
//            @RequestParam("name") String name,
//            @RequestParam("aadharNumber") String aadharNumber,
//            @RequestParam("contact") String contact,
//            @RequestParam("email") String email) {
//        BankAccount bankAccount = new BankAccount(name, contact, email, aadharNumber);
//        bankAccount.save();
//        bankAccounts.add(bankAccount);
//        return "redirect:/hi";
//    }
//}

@Controller
public class BankAccountView {
    
    @GetMapping("/hi")
    public String showWelcomePage() {
        return "welcome";
    }

    @GetMapping("/create-account")
    public String showCreateAccountForm() {
        return "create-account-form";
    }

    @PostMapping("/save")
    public String saveBankAccount(
    		
            @RequestParam("name") String name,
            @RequestParam("aadharNumber") String aadharNumber,
            @RequestParam("contact") String contact,
            @RequestParam("email") String email) throws SQLException
    		
    {
        BankAccount bankAccount =AccountFactory.createAccount(name, contact, email, aadharNumber);
        bankAccount.save();
        return "success";
    }
    @GetMapping("/transaction-form")
    public String showTransactionForm() {
    return "transaction-form";
    }

    @PostMapping("/transaction")
    public String makeTransaction(@RequestParam("accountNumber") String accountNumber,
            @RequestParam("transactionType") String transactionType,
            @RequestParam("amount") double amount,
            Model model) {
	try {
	
	Transaction transaction = new Transaction(accountNumber, transactionType, amount);
	transaction.save();
	model.addAttribute("message", "Transaction successful");
	} catch (SQLException e) {
	e.printStackTrace();
	model.addAttribute("message", "Transaction failed");
	}
	return "success";
	}
    @GetMapping("/bank-accounts")
    public ModelAndView showAllBankAccounts() {
        List<BankAccount> bankAccounts = BankAccount.getAll();
        ModelAndView modelAndView = new ModelAndView("bank-accounts");
        modelAndView.addObject("bankAccounts", bankAccounts);
        return modelAndView;
    }	
    
    
   }



