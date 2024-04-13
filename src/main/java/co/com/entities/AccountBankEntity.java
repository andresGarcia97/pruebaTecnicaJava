package co.com.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "account_bank")
public class AccountBankEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AccountState state;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @NotNull
    @Column(name = "exent_gmf", nullable = false)
    private Boolean exentGMF;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "last_modification_date")
    private ZonedDateTime lastModificationDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;
    
    public AccountBankEntity() {
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

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBankEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountBankEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
    	final Long clientId = getClient() == null ? null : getClient().getId();
        return "AccountBankEntity{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", number=" + getNumber() +
            ", state='" + getState() + "'" +
            ", balance=" + getBalance() +
            ", exentGMF='" + getExentGMF() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastModificationDate='" + getLastModificationDate() + "'" +
            ", clientId='" + clientId  + "'" +
            "}";
    }
}
