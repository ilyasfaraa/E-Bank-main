package ma.il.ebanking;

import ma.il.ebanking.dtos.BankAccountDTO;
import ma.il.ebanking.dtos.CurrentBankAccountDTO;
import ma.il.ebanking.dtos.CustomerDTO;
import ma.il.ebanking.dtos.SavingBankAccountDTO;
import ma.il.ebanking.entites.AccountOperation;
import ma.il.ebanking.entites.CurrentAccount;
import ma.il.ebanking.entites.Customer;
import ma.il.ebanking.entites.SavingAccount;
import ma.il.ebanking.enums.AccountStatus;
import ma.il.ebanking.enums.OperationType;
import ma.il.ebanking.exceptions.CustomerNotFoundException;
import ma.il.ebanking.repositories.AccountOperationRepo;
import ma.il.ebanking.repositories.BankAccountRepo;
import ma.il.ebanking.repositories.CustomerRepo;
import ma.il.ebanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Mohamed", "Hamza", "Ayoub").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(cust -> {
                try {
                    bankAccountService.saveCurrentBankAccount(cust.getId(), 9000, Math.random() * 99000);
                    bankAccountService.saveSavingBankAccount(cust.getId(), 5.5, Math.random() * 128000);
                    /*List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
                    for (BankAccountDTO bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            String accountId;
                            if (bankAccount instanceof SavingBankAccountDTO) {
                                accountId = ((SavingBankAccountDTO) bankAccount).getId();
                            } else {
                                accountId = ((CurrentBankAccountDTO) bankAccount).getId();

                            }
                            bankAccountService.credit(accountId, 1000 + Math.random() * 120000, "Credit");
                            bankAccountService.debit(accountId, 2000 + Math.random() * 780, "Debit");
                        }
                    }*/
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } /*catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }*/
            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount : bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO) {
                        accountId = ((SavingBankAccountDTO) bankAccount).getId();
                    } else {
                        accountId = ((CurrentBankAccountDTO) bankAccount).getId();

                    }
                    bankAccountService.credit(accountId, 1000 + Math.random() * 120000, "Credit");
                    bankAccountService.debit(accountId, 2000 + Math.random() * 780, "Debit");
                }
            }
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepo customerRepo, BankAccountRepo bankAccountRepo, AccountOperationRepo accountOperationRepo) {
        return args -> {
            Stream.of("Mohamed", "Hamza", "Ayoub").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepo.save(customer);
            });
            customerRepo.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 7500);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepo.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 8700);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(6.5);
                bankAccountRepo.save(savingAccount);
            });
            bankAccountRepo.findAll().forEach(acc -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 56);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepo.save(accountOperation);
                }

            });
        };
    }
}
