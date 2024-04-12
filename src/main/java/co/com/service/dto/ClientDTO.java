package co.com.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import co.com.entities.enumeration.IdentificationType;

public class ClientDTO implements Serializable {

    private static final long serialVersionUID = -2611438298976094339L;

	private Long id;

    private IdentificationType identificationType;

    private String identification;

    private String name;

    private String lastName;

    private String email;

    private LocalDate bornDate;
    
    private ZonedDateTime creationDate;

    private ZonedDateTime lastModificationDate;
    
    @JsonInclude(Include.NON_NULL)
    private Set<AccountBankDTO> accounts;
    
    public ClientDTO() {
    	super();
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
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

	public Set<AccountBankDTO> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountBankDTO> accounts) {
		this.accounts = accounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounts, bornDate, creationDate, email, id, identification, identificationType,
				lastModificationDate, lastName, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientDTO other = (ClientDTO) obj;
		return Objects.equals(accounts, other.accounts) && Objects.equals(bornDate, other.bornDate)
				&& Objects.equals(creationDate, other.creationDate) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(identification, other.identification)
				&& identificationType == other.identificationType
				&& Objects.equals(lastModificationDate, other.lastModificationDate)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(name, other.name);
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", identificationType='" + getIdentificationType() + "'" +
            ", identification='" + getIdentification() + "'" +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", bornDate='" + getBornDate() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastModificationDate='" + getLastModificationDate() + "'" +
            ", accounts='" + getAccounts() + "'" +
            "}";
    }
}
