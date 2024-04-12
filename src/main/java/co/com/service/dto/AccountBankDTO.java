package co.com.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AccountBankDTO implements Serializable {

    private static final long serialVersionUID = 6441152201568629889L;

	private Long id;

    @NotNull
    private AccountType accountType;

    @NotNull
    private Long number;

    @NotNull
    private AccountState state;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal balance;

    @NotNull
    private Boolean exentGMF;

    @NotNull
    private ZonedDateTime creationDate;

    private ZonedDateTime lastModificationDate;

    private ClientDTO account;

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

    public ClientDTO getAccount() {
        return account;
    }

    public void setAccount(ClientDTO account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBankDTO)) {
            return false;
        }

        AccountBankDTO accountBankDTO = (AccountBankDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountBankDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBankDTO{" +
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
