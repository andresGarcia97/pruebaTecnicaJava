package co.com.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import co.com.entities.enumeration.TransactionType;

public class TransactionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;

	private TransactionType transactionType;

	private ZonedDateTime transactionDate;

	private BigDecimal amount;

	@JsonInclude(Include.NON_NULL)
	private AccountBankDTO origin;

	@JsonInclude(Include.NON_NULL)
	private AccountBankDTO destiny;

	public TransactionDTO() {
		super();
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

	public AccountBankDTO getOrigin() {
		return origin;
	}

	public void setOrigin(AccountBankDTO origin) {
		this.origin = origin;
	}

	public AccountBankDTO getDestiny() {
		return destiny;
	}

	public void setDestiny(AccountBankDTO destiny) {
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
		TransactionDTO other = (TransactionDTO) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(destiny, other.destiny)
				&& Objects.equals(id, other.id) && Objects.equals(origin, other.origin)
				&& Objects.equals(transactionDate, other.transactionDate) && transactionType == other.transactionType;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "TransactionDTO{" +
				"id='" + getId() + "'" +
						", transactionType='" + getTransactionType() + "'" +
						", transactionDate='" + getTransactionDate() + "'" +
						", amount=" + getAmount() +
						", origin=" + getOrigin() +
						", destiny=" + getDestiny() +
						"}";
	}
}
