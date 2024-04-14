package co.com.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

import co.com.entities.enumeration.IdentificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "client")
public class ClientEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "identification_type", nullable = false)
	private IdentificationType identificationType;

	@NotNull
	@Column(name = "identification", nullable = false)
	private String identification;

	@Column(name = "name")
	private String name;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@NotNull
	@Column(name = "born_date", nullable = false)
	private LocalDate bornDate;

	@NotNull
	@Column(name = "creation_date", nullable = false)
	private ZonedDateTime creationDate;

	@Column(name = "last_modification_date")
	private ZonedDateTime lastModificationDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
	private Set<AccountBankEntity> accounts;

	public ClientEntity() {
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

	public Set<AccountBankEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountBankEntity> accounts) {
		this.accounts = accounts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClientEntity)) {
			return false;
		}
		return getId() != null && getId().equals(((ClientEntity) o).getId());
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "ClientEntity{" +
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
