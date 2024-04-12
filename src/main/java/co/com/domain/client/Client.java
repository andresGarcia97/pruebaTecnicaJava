package co.com.domain.client;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.domain.accountbank.AccountBank;
import co.com.entities.enumeration.IdentificationType;

public class Client {
	
	private static final Logger log = LoggerFactory.getLogger(Client.class);

	private static final int MIN_YEARS = 18;
	private static final int MIN_LENGTH_NAME_AND_LAST_NAME = 2;
	private static final String REGEX_EMAIL = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";

	private Long id;

	private IdentificationType identificationType;

	private String identification;

	private String name;

	private String lastName;

	private String email;

	private LocalDate bornDate;

	private ZonedDateTime creationDate;

	private ZonedDateTime lastModificationDate;
	
	private Set<AccountBank> accounts;

	public Client() {
		super();
	}

	public Client validateCreation() throws ClientException {

		this.validateBornDate();
		this.validateNameAndLastName();
		this.validateEmail();

		this.setCreationDate(ZonedDateTime.now().withNano(0));
		return this;
	}

	public Client validateUpdate(final Client client) throws ClientException {

		this.validateBornDate();
		this.validateNameAndLastName();
		this.validateEmail();
		
		if(this.getCreationDate().toInstant().compareTo(client.getCreationDate().toInstant()) != 0) {
			log.error("validateUpdate :: dateCreationPrevious: {}, dateCreationUpdate: {}", client.getCreationDate(), this.getCreationDate());
			throw new ClientException("No se puede cambiar la fecha de creacion");
		}
		
		this.setLastModificationDate(ZonedDateTime.now().withNano(0));
		return this;
	}

	private void validateBornDate() throws ClientException {

		if(this.bornDate == null) {
			throw new ClientException("La fecha de nacimiento es obligatoria");
		}

		final LocalDate currentTime = LocalDate.now();

		if(this.bornDate.isAfter(currentTime)) {
			throw new ClientException("La fecha de nacimiento ingresada, es en el futuro");
		}

		final long diffYears = Math.abs(ChronoUnit.YEARS.between(currentTime, this.bornDate));
		if(diffYears < MIN_YEARS) {
			throw new ClientException("El usuario es menor de edad, tiene: " + diffYears + " años cuando lo minimo son: " + MIN_YEARS);
		}
	}

	private void validateNameAndLastName() throws ClientException {

		if(this.name == null || this.name.isBlank()) {
			throw new ClientException("El nombre es obligatorio");
		}

		if(this.lastName == null || this.lastName.isBlank()) {
			throw new ClientException("El apellido es obligatorio");
		}

		if(MIN_LENGTH_NAME_AND_LAST_NAME >= this.name.length() || MIN_LENGTH_NAME_AND_LAST_NAME >= this.lastName.length()) {
			throw new ClientException("El nombre y el apellido, deben de tener minimo " + MIN_LENGTH_NAME_AND_LAST_NAME + " caracteres");
		}
	}

	private void validateEmail() throws ClientException {

		if(this.email == null || this.email.isBlank()) {
			throw new ClientException("El correo es obligatorio");
		}

		if(!Pattern.matches(REGEX_EMAIL, this.email)) {
			throw new ClientException("El correo ingresado no es valido");
		}
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

	public Set<AccountBank> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountBank> accounts) {
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
		Client other = (Client) obj;
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
		return "Client{" +
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
