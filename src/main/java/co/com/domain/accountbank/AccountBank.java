package co.com.domain.accountbank;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import co.com.domain.client.Client;
import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;

public class AccountBank {

	private Long id;

    private AccountType accountType;

    private Long number;
    
    private AccountState state;

    private BigDecimal balance;

    private Boolean exentGMF;

    private ZonedDateTime creationDate;

    private ZonedDateTime lastModificationDate;

    private Client account;
    
    public AccountBank() {
		super();
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getExentGMF() {
        return exentGMF;
    }

    public void setExentGMF(Boolean exentGMF) {
        this.exentGMF = exentGMF;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(ZonedDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Client getAccount() {
        return account;
    }

    public void setAccount(Client account) {
        this.account = account;
    }

    @Override
	public int hashCode() {
		return Objects.hash(account, accountType, balance, creationDate, exentGMF, id, lastModificationDate, number,
				state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountBank other = (AccountBank) obj;
		return Objects.equals(account, other.account) && accountType == other.accountType
				&& Objects.equals(balance, other.balance) && Objects.equals(creationDate, other.creationDate)
				&& Objects.equals(exentGMF, other.exentGMF) && Objects.equals(id, other.id)
				&& Objects.equals(lastModificationDate, other.lastModificationDate)
				&& Objects.equals(number, other.number) && state == other.state;
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "AccountBank{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", number=" + getNumber() +
            ", state='" + getState() + "'" +
            ", balance=" + getBalance() +
            ", exentGMF='" + getExentGMF() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastModificationDate='" + getLastModificationDate() + "'" +
            ", account=" + getAccount() +
            "}";
    }
}
