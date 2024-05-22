package ma.il.ebanking.repositories;

import ma.il.ebanking.entites.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount,String> {
    
}
