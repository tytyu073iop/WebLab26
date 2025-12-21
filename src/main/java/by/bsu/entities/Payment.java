package by.bsu.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private Double amount;
    
    @Column(name = "payment_date")
    private Date paymentDate;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.SUCCESS;
    
    // Конструкторы
    public Payment() {
        this.paymentDate = new Date();
        this.status = PaymentStatus.SUCCESS;
    }
    
    public Payment(Account fromAccount, Account toAccount, Double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.paymentDate = new Date();
        this.status = PaymentStatus.SUCCESS;
    }
    
    // Геттеры и сеттеры
    public Long getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    public Account getFromAccount() {
        return fromAccount;
    }
    
    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    public Account getToAccount() {
        return toAccount;
    }
    
    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    // Enum для статусов платежа
    public enum PaymentStatus {
        SUCCESS,
        FAILED,
        CANCELLED
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", status=" + status +
                '}';
    }
}