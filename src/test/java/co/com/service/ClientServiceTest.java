package co.com.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.com.domain.client.Client;
import co.com.domain.client.ClientException;
import co.com.dto.ClientDTO;
import co.com.entities.AccountBankEntity;
import co.com.entities.ClientEntity;
import co.com.entities.enumeration.IdentificationType;
import co.com.repository.ClientRepository;
import co.com.service.impl.ClientServiceImpl;
import co.com.service.mapper.client.ClientDomainMapper;
import co.com.service.mapper.client.ClientEntityMapper;
import co.com.service.mapper.client.ClientQueriesMapper;

@DirtiesContext
@ExtendWith(SpringExtension.class)
class ClientServiceTest {

	@MockBean
	private ClientRepository clientRepository;

	@MockBean
	private ClientQueriesMapper queriesMapper;

	@MockBean
	private ClientDomainMapper domainMapper;

	@MockBean
	private ClientEntityMapper entityMapper;

	private ClientService clientService;

	private ClientDTO clientDto;

	@BeforeEach
	public void initTest() {

		this.clientDto = new ClientDTO();

		this.clientService = new ClientServiceImpl(
				clientRepository,
				queriesMapper,
				domainMapper,
				entityMapper);
	}

	@Nested
	@DisplayName("Validaciones para guardar a un nuevo cliente")
	class Save {

		@Test
		@DisplayName("Cliente menor de edad")
		void clientWithWrongAge() {

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(10));

			when(domainMapper.toDomain(clientDto)).thenReturn(client);

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.save(clientDto);
			});

			assertTrue(exception.getMessage().contains("El usuario es menor de edad"));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Cliente con nombre y apellido erroneo")
		void clientWithWrongName() {

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("no");
			client.setLastName("la");

			when(domainMapper.toDomain(clientDto)).thenReturn(client);

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.save(clientDto);
			});

			assertTrue(exception.getMessage().contains("El nombre y el apellido"));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Cliente con correo erroneo")
		void clientWithWrongEmail() {

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("nombre");
			client.setLastName("appellido");
			client.setEmail("correoInvalido@gmail");

			when(domainMapper.toDomain(clientDto)).thenReturn(client);

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.save(clientDto);
			});

			assertTrue(exception.getMessage().contains("El correo ingresado no es valido"));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Cliente sin identificacion")
		void clientWithOutIdentification() throws ClientException {

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("nombre");
			client.setLastName("appellido");
			client.setEmail("correoValido@gmail.com");

			when(domainMapper.toDomain(clientDto)).thenReturn(client);
			when(entityMapper.toEntity(client)).thenReturn(new ClientEntity());
			when(clientRepository.save(any(ClientEntity.class))).thenReturn(new ClientEntity());
			when(queriesMapper.toDto(any(ClientEntity.class))).thenReturn(new ClientDTO());

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.save(clientDto);
			});

			assertTrue(exception.getMessage().contains("La identificación es obligatoria"));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Cliente con datos correctamente validados")
		void clientSaveSuccess() throws ClientException {

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("nombre");
			client.setLastName("appellido");
			client.setEmail("correoValido@gmail.com");
			client.setIdentification("Identificacion");
			client.setIdentificationType(IdentificationType.CEDULA);

			when(domainMapper.toDomain(clientDto)).thenReturn(client);
			when(entityMapper.toEntity(client)).thenReturn(new ClientEntity());
			when(clientRepository.save(any(ClientEntity.class))).thenReturn(new ClientEntity());
			when(queriesMapper.toDto(any(ClientEntity.class))).thenReturn(new ClientDTO());

			final ClientDTO result = clientService.save(clientDto);

			assertNotNull(client.getCreationDate());
			assertNotNull(result);

			verify(clientRepository, times(1)).save(any(ClientEntity.class));
		}

	}

	@Nested
	@DisplayName("Validaciones para actualizar un cliente")
	class Update {

		@Test
		@DisplayName("No existe el cliente con el ID entregado")
		void clientNotExist() {

			when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.update(clientDto);
			});

			assertTrue(exception.getMessage().contains(ClientException.CLIENT_NOT_EXIST));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Error al actualizar el cliente, se esta intentando cambiar la fecha de creacion")
		void clientTryToChangeDateCreation() {

			clientDto.setId(12l);

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("nombre");
			client.setLastName("appellido");
			client.setEmail("correoValido@gmail.com");
			client.setCreationDate(ZonedDateTime.now());
			client.setIdentification("Identificacion");
			client.setIdentificationType(IdentificationType.CEDULA);

			final Client clientCopy = new Client();
			clientCopy.setBornDate(LocalDate.now().minusYears(20));
			clientCopy.setName("nombre");
			clientCopy.setLastName("appellido");
			clientCopy.setEmail("correoValido@gmail.com");
			clientCopy.setCreationDate(ZonedDateTime.now().minusSeconds(1));

			when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new ClientEntity()));
			when(domainMapper.toDomain(any(ClientDTO.class))).thenReturn(client);
			when(entityMapper.toDomain(any(ClientEntity.class))).thenReturn(clientCopy);

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.update(clientDto);
			});

			assertTrue(exception.getMessage().contains("No se puede cambiar la fecha de creacion"));
			verify(clientRepository, never()).save(any(ClientEntity.class));
		}

		@Test
		@DisplayName("Actualizacion Exitosa del cliente")
		void clientUpdateSuccess() throws ClientException {

			clientDto.setId(12l);

			final ZonedDateTime currentTime = ZonedDateTime.now().minusMinutes(1);

			final Client client = new Client();
			client.setBornDate(LocalDate.now().minusYears(20));
			client.setName("nombre");
			client.setLastName("appellido");
			client.setEmail("correoValido@gmail.com");
			client.setCreationDate(currentTime);
			client.setIdentification("Identificacion");
			client.setIdentificationType(IdentificationType.CEDULA);

			final Client clientCopy = new Client();
			clientCopy.setBornDate(LocalDate.now().minusYears(20));
			clientCopy.setName("nombre");
			clientCopy.setLastName("appellido");
			clientCopy.setEmail("correovalido@gmail.com");
			clientCopy.setCreationDate(currentTime);
			clientCopy.setIdentification("Identificacion");
			clientCopy.setIdentificationType(IdentificationType.CEDULA);

			when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new ClientEntity()));
			when(domainMapper.toDomain(any(ClientDTO.class))).thenReturn(client);
			when(entityMapper.toDomain(any(ClientEntity.class))).thenReturn(clientCopy);
			when(entityMapper.toEntity(any(Client.class))).thenReturn(new ClientEntity());
			when(clientRepository.save(any(ClientEntity.class))).thenReturn(new ClientEntity());
			when(queriesMapper.toDto(any(ClientEntity.class))).thenReturn(new ClientDTO());

			final ClientDTO result = clientService.update(clientDto);

			assertNotNull(client.getLastModificationDate());
			assertNotNull(result);

			verify(clientRepository, times(1)).save(any(ClientEntity.class));
		}

	}

	@Nested
	@DisplayName("Elimina un cliente, siempre que exista y no tenga cuentas asociadas")
	class Delete {

		@Test
		@DisplayName("No existe el cliente con el ID entregado")
		void clientNotExist() throws ClientException {

			clientDto.setId(12l);

			when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

			clientService.delete(clientDto.getId());

			verify(clientRepository, never()).deleteById(anyLong());
		}

		@Test
		@DisplayName("Existe el cliente, pero tiene cuentas asociadas, no se elimina")
		void clientExist() {

			clientDto.setId(12l);

			final AccountBankEntity account = new AccountBankEntity();
			account.setNumber(123l);

			final ClientEntity clientToDelete = new ClientEntity();
			clientToDelete.setAccounts(Set.of(account));

			when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientToDelete));

			final Exception exception = assertThrows(ClientException.class, () -> {
				clientService.delete(clientDto.getId());
			});

			assertTrue(exception.getMessage().contains("No se puede eliminar este cliente"));
			verify(clientRepository, never()).deleteById(anyLong());
		}

		@Test
		@DisplayName("Existe el cliente, y no tiene cuentas asociadas, se elimina")
		void clientDeleteSuccess() throws ClientException {

			clientDto.setId(12l);

			final ClientEntity clientToDelete = new ClientEntity();
			clientToDelete.setAccounts(Set.of());

			when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientToDelete));

			clientService.delete(clientDto.getId());

			verify(clientRepository, times(1)).deleteById(clientDto.getId());
		}

	}

}
