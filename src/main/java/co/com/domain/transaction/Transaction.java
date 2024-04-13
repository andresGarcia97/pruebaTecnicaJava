package co.com.domain.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import co.com.domain.accountbank.AccountBank;
import co.com.entities.enumeration.TransactionType;

public class Transaction {

	private UUID id;
	
    private TransactionType transactionType;
    
    private ZonedDateTime transactionDate;
    
    private BigDecimal amount;

    private AccountBank origin;
    
    private AccountBank destiny;
    
    public Transaction() {
		super();
	}
    
	public Transaction validateCreation() {

		return this;
	}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountBank getOrigin() {
        return origin;
    }

    public void setOrigin(AccountBank origin) {
        this.origin = origin;
    }
    
    public AccountBank getDestiny() {
		return destiny;
	}

	public void setDestiny(AccountBank destiny) {
		this.destiny = destiny;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(amount, destiny, id, origin, transactionDate, transactionType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(destiny, other.destiny)
				&& Objects.equals(id, other.id) && Objects.equals(origin, other.origin)
				&& Objects.equals(transactionDate, other.transactionDate) && transactionType == other.transactionType;
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id='" + getId() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", amount=" + getAmount() +
            ", origin=" + getOrigin() +
            ", destiny=" + getDestiny() +
            "}";
    }
    
}
