package ma.il.ebanking.services;

import ma.il.ebanking.repositories.BankAccountRepo;
import ma.il.ebanking.entites.BankAccount;
import ma.il.ebanking.entites.CurrentAccount;
import ma.il.ebanking.entites.SavingAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepo bankAccountRepo;

    public void consulter() {
        BankAccount bankAccount = bankAccountRepo.findById("22c4c84e-55cc-49e3-80a7-016a692e649a").orElse(null);
        if (bankAccount != null) {
            System.out.println("*******************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }
    }
}
