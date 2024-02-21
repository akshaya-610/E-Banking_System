//
//package com.example.demo;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class BankController {
//
//    @GetMapping("/bank-account-form")
//    public String showBankAccountForm() {
//        return "bank-account-form";
//    }
//
//    @PostMapping("/bank-account-form")
//    public String saveBankAccount(
//            @RequestParam("name") String name,
//            @RequestParam("contact") String contact,
//            @RequestParam("email") String email,
//            @RequestParam("aadharNumber") String aadharNumber,
//            Model model) {
//
//        // Create a new BankAccount instance with the input values
//        BankAccount bankAccount = new BankAccount(name, contact, email, aadharNumber);
//
//        // Save the BankAccount instance to the database
//        bankAccount.save();
//
//        // Add a success message to the model
//        model.addAttribute("successMessage", "Bank account created successfully!");
//
//        return "bank-account-form";
//    }
//}
package com.example.demo;
import java.util.*;
import java.sql.*;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BankController {

    private final List<BankAccount> bankAccounts = new ArrayList<>();
 
//    @GetMapping("/bank_management_system")
//    public String showBankAccounts(Model model) {
//        model.addAttribute("bankAccounts", bankAccounts);
//        return "bank-accounts";
//    }
    

   

    @GetMapping("/new")
    public String showNewBankAccountForm() {
        return "new-bank-account";
    }

    @PostMapping("/new")
    public String addBankAccount(
            @RequestParam("name") String name,
            @RequestParam("aadharNumber") String aadharNumber,
            @RequestParam("contact") String contact,
            @RequestParam("email") String email) {
        BankAccount bankAccount = AccountFactory.createAccount(name, contact, email, aadharNumber);
        bankAccounts.add(bankAccount);
        return "redirect:/";
    }
    @GetMapping("/show-all-accounts")
    public ModelAndView showAllAccounts() {
        List<BankAccount> bankAccounts = BankAccount.getAll();
        ModelAndView modelAndView = new ModelAndView("all-accounts");
        modelAndView.addObject("bankAccounts", bankAccounts);
        return modelAndView;
    }
    
    @PostMapping("/transactions")
    public String makeTransaction(@RequestParam("accountNumber") String accountNumber,
                                   @RequestParam("transactionType") String transactionType,
                                   @RequestParam("amount") double amount,
                                   Model model) {
        try {
            
            Transaction transaction = new Transaction(accountNumber, transactionType, amount);
            TransactionDAO trans=new TransactionDAOImpl();
            trans.save(transaction);
            model.addAttribute("message", "Transaction successful");
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("message", "Transaction failed");
        }
        return "transaction-form";
    }


}

