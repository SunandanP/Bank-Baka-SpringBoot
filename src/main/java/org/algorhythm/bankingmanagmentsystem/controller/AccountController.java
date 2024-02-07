package org.algorhythm.bankingmanagmentsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.algorhythm.bankingmanagmentsystem.dataaccess.entity.Account;
import org.algorhythm.bankingmanagmentsystem.dataaccess.entity.Transaction;
import org.algorhythm.bankingmanagmentsystem.dataaccess.repository.AccountRepository;
import org.algorhythm.bankingmanagmentsystem.dataaccess.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("")
    @ResponseBody
    public String getHome(){
        return "Hehe";
    }
    @GetMapping("accounts/{id}/get-balance")
    public double getBalance(@PathVariable long id) throws Exception {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null){
            throw new Exception("User not found");
        }
        return account.getBalance();
    }

    @GetMapping("accounts/{id}/account-info")
    public Account getAccountInfo(@PathVariable long id){
        return accountRepository.getAccountById(id);
    }

    @PostMapping("accounts/{id}/withdraw")
    public ResponseEntity<Transaction> withdrawFromAccount(@RequestBody Transaction txn, @PathVariable long id){
        Account account = accountRepository.getAccountById(id);

        double expendable = transactionRepository.findTransactionByAccount_Id(id).stream()
                .filter(t -> t.getType().equals("Withdraw") && t.getTimestamp().toLocalDate().equals(LocalDate.now())) // Filter for withdrawals
                .map(Transaction::getAmount) // Extract the amounts
                .reduce(0.0, Double::sum);

        expendable = account.getDailyLimit() - expendable;
        String message = "";
        Transaction transaction = new Transaction();
        if (account.getMinBalance() > account.getBalance() - txn.getAmount()){
            message += "Account should have minimum balance of "+account.getBalance();
        }
        else if (account.getBalance() - txn.getAmount() < 0){
            message += "Insufficient funds.";
        } else if (expendable == 0) {
            message += "Daily limit reached.";
        } else if (expendable < txn.getAmount()) {
            message += "Beyond daily limit";
        } else {
            account.setBalance(account.getBalance() - txn.getAmount());

            account = accountRepository.save(account);
            transaction = transactionRepository.save(new Transaction(txn.getAmount(), LocalDateTime.now(), "Withdraw", account));
        }
        transaction.setType(message);
        if (transaction.getId() == 0){
            return new ResponseEntity<>(transaction, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("accounts/{id}/deposit")
    public ResponseEntity<Transaction> depositToAccount(@PathVariable long id, @RequestBody Transaction txn){
        Account account = accountRepository.getAccountById(id);
        Transaction transaction = new Transaction(txn.getAmount(), LocalDateTime.now(), "Deposit", account);

        account.setBalance(account.getBalance() + txn.getAmount());
        account = accountRepository.save(account);

        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.OK);
    }

    @GetMapping("accounts/{id}/get-all-transactions")
    public List<Transaction> getAllTransactions(@PathVariable long id){
        return transactionRepository.findTransactionByAccount_Id(id);
    }

    class IntraBank {

        long receiverId;
        double amount;

        public IntraBank( long receiverId, double amount) {
            this.receiverId = receiverId;
            this.amount = amount;
        }
    }

    @PostMapping("accounts/{id}/intra-bank")
    public ResponseEntity<Transaction> transferWithinBank(@PathVariable long id, @RequestBody IntraBank transfer){
        var sender = accountRepository.getAccountById(id);
        var temp = new Transaction();
        temp.setAmount(transfer.amount);
        var txn = withdrawFromAccount(temp, id);
        temp.setAmount(transfer.amount);
        depositToAccount(transfer.receiverId, temp);

        transactionRepository.save(new Transaction(transfer.amount, LocalDateTime.now(), "Withdraw", sender));
        transactionRepository.save(new Transaction(transfer.amount, LocalDateTime.now(), "Deposit", accountRepository.getAccountById(transfer.receiverId)));

        return txn;
    }

    @PostMapping("accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
    }

    @DeleteMapping("accounts/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable long id){
        accountRepository.deleteById(id);
        return new ResponseEntity<>("Account Deleted.", HttpStatus.OK);
    }

    @PutMapping("accounts/update/{id}")
    public ResponseEntity<Account> deleteAccount(@RequestBody Account account){
        account = accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }




    @PostMapping("accounts/login")
    public ResponseEntity<Account> login(@RequestBody Details details){
        Account account = accountRepository.findAccountByName(details.getUsername());
        if (account != null){
            if (account.getPassword() == details.getPassword()){
                return new ResponseEntity<>(account, HttpStatus.OK);
            }
            else{

                return new ResponseEntity<>(account, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else {
            return new ResponseEntity<>(account, HttpStatus.BAD_REQUEST);
        }
    }

}
class Details{
    private String username;
    private String password;

    public Details(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
