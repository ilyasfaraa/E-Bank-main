package ma.il.ebanking.dtos;

import lombok.Data;
import ma.il.ebanking.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    //private BankAccount bankAccount;
    private String description;
}
