package co.com.domain.accountbank;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.domain.client.Client;
import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;

public class AccountBank {
	
	private static final Logger log = LoggerFactory.getLogger(AccountBank.class);

	private Long id;

    private AccountType accountType;

    private Long number;
    
    private AccountState state;

    private BigDecimal balance;

    private Boolean exentGMF;

    private ZonedDateTime creationDate;

    private ZonedDateTime lastModificationDate;

    private Client client;
    
    public AccountBank() {
		super();
	}
    
	public AccountBank validateCreation() throws AccountBankException {
		
		final AccountType type = this.accountType;
		if(type == null) {
			throw new AccountBankException("El tipo de cuenta es obligatorio");
		}
		
		if(AccountType.CUENTA_AHORROS.equals(type)) {
			this.setState(AccountState.ACTIVA);
		}
		else if(this.state == null) {
			throw new AccountBankException("EL estado de la cuenta es obligatorio, al ser una cuenta de tipo: " + type.toString());
		}
		
		log.debug("validateCreation:: {} {}", this, this.getClient());
		
		return this;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
	public int hashCode() {
		return Objects.hash(client, accountType, balance, creationDate, exentGMF, id, lastModificationDate, number,
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
		return Objects.equals(client, other.client) && accountType == other.accountType
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
            ", client=" + getClient() +
            "}";
    }
}
