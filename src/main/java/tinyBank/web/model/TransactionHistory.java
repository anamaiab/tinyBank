package tinyBank.web.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionHistory {

    private Double transactionAmount;
    private String timestamp;
    private TypeTransaction typeTransaction;


    public TransactionHistory (double amount, TypeTransaction type){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.timestamp = sdf.format(new Date());
        typeTransaction = type;
        transactionAmount = amount;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

}
