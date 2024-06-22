package tinyBank.web.model;

import java.util.ArrayList;
import java.util.List;

public class History {

    private Double totalAmount;
    private List<TransactionHistory> transactionHistory;

    public History() {
        this.totalAmount = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public Double getTotalAmount(){
        return totalAmount;
    }

    public void setTotalAmount(Double value){
        totalAmount = value;
    }

    public List<TransactionHistory> getTransactionHistory(){
        return transactionHistory;
    }

    public void addTransactionHistory(Double amount, TypeTransaction typeTransaction){
        TransactionHistory newHistory = new TransactionHistory(amount, typeTransaction);
        transactionHistory.add(newHistory);
    }

    public void addTransaction(Double amount, TypeTransaction typeTransaction){
        if(typeTransaction.equals(TypeTransaction.WITHDRAWAL) || typeTransaction.equals(TypeTransaction.TRANSFER_SENT)) {
            totalAmount -= amount;
        }
        else {
            totalAmount += amount;
        }
        addTransactionHistory(amount, typeTransaction);
    }

}
