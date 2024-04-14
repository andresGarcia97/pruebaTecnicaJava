package co.com.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.domain.client.ClientException;
import co.com.dto.AccountBankDTO;
import co.com.dto.ClientDTO;
import co.com.entities.AccountBankEntity;
import co.com.entities.enumeration.AccountState;
import co.com.entities.enumeration.AccountType;
import co.com.repository.AccountBankRepository;
import co.com.service.impl.AccountBankServiceImpl;
import co.com.service.mapper.accountbank.AccountBankDomainMapper;
import co.com.service.mapper.accountbank.AccountBankEntityMapper;
import co.com.service.mapper.accountbank.AccountBankQueriesMapper;

@DirtiesContext
@ExtendWith(SpringExtension.class)
class AccountBankServiceTest {

	@MockBean
	private AccountBankRepository accountBankRepository;

	@MockBean
	private ClientService clientService;

	@MockBean
	private AccountBankQueriesMapper queriesMapper;

	@MockBean
	private AccountBankDomainMapper domainMapper;

	@MockBean
	private AccountBankEntityMapper entityMapper;

	private AccountBankService accountBankService;

	private AccountBankDTO accountBankDto;

	@BeforeEach
	public void initTest() {

		this.accountBankDto = new AccountBankDTO();

		this.accountBankService = new AccountBankServiceImpl(
				accountBankRepository,
				queriesMapper,
				domainMapper,
				entityMapper,
				clientService);
	}

	@Nested
	@DisplayName("Validaciones para guardar una nueva cuenta bancaria")
	class Save {

		@Test
		@DisplayName("Cliente invalido, no se puede crear la cuenta")
		void clientInvalid() throws ClientException {

			final ClientDTO client = new ClientDTO();
			accountBankDto.setClient(client);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.save(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("La cuenta debe estar asociada a un cliente"));
			verify(clientService, never()).findById(anyLong());
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Cliente No encontrado, no se puede crear la cuenta")
		void clientNotFound() throws ClientException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			when(clientService.findById(anyLong())).thenThrow(new ClientException(ClientException.CLIENT_NOT_EXIST));

			final Exception exception = assertThrows(ClientException.class, () -> {
				accountBankService.save(accountBankDto);
			});

			assertTrue(exception.getMessage().contains(ClientException.CLIENT_NOT_EXIST));
			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("No se puede crear la cuenta, sin especificar el tipo")
		void wrongAccountType() throws ClientException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			final AccountBank accountBank = new AccountBank();

			when(clientService.findById(anyLong())).thenReturn(client);
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.save(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("El tipo de cuenta es obligatorio"));
			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Al ser una CUENTA_CORRIENTE se debe especificar el estado")
		void wrongState() throws ClientException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			final AccountBank accountBank = new AccountBank();
			accountBank.setAccountType(AccountType.CUENTA_CORRIENTE);

			when(clientService.findById(anyLong())).thenReturn(client);
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.save(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("El estado de la cuenta es obligatorio"));
			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("El campo 'exentGMF' es obligatorio")
		void wrongExentGmf() throws ClientException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			final AccountBank accountBank = new AccountBank();
			accountBank.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBank.setState(AccountState.ACTIVA);

			when(clientService.findById(anyLong())).thenReturn(client);
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.save(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("Se debe definir si la cuenta es exenta de GMF"));
			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Campos validos y/o alterados, se crea la cuenta bancaria")
		void accountBankSuccess() throws ClientException, AccountBankException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			final AccountBank accountBank = new AccountBank();
			accountBank.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBank.setState(AccountState.ACTIVA);
			accountBank.setExentGMF(true);
			accountBank.setBalance(new BigDecimal(-1));
			accountBank.setLastModificationDate(ZonedDateTime.now());

			when(clientService.findById(anyLong())).thenReturn(client);
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);
			when(accountBankRepository.findByNumber(accountBank.getNumber())).thenReturn(Optional.empty());

			when(entityMapper.toEntity(accountBank)).thenReturn(new AccountBankEntity());
			when(accountBankRepository.save(any(AccountBankEntity.class))).thenReturn(new AccountBankEntity());
			when(queriesMapper.toDto(any(AccountBankEntity.class))).thenReturn(new AccountBankDTO());

			final AccountBankDTO result = accountBankService.save(accountBankDto);

			assertNotNull(accountBank.getNumber());
			assertNotNull(accountBank.getCreationDate());
			assertNull(accountBank.getLastModificationDate());
			assertEquals(BigDecimal.ZERO, accountBank.getBalance());

			assertNotNull(result);

			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, times(1)).findByNumber(anyLong());
			verify(accountBankRepository, times(1)).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Campos validos, se crea la cuenta bancaria")
		void alteringRandomNumberAccount() throws ClientException, AccountBankException {

			final ClientDTO client = new ClientDTO();
			client.setId(12l);
			accountBankDto.setClient(client);

			final AccountBank accountBank = new AccountBank();
			accountBank.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBank.setState(AccountState.ACTIVA);
			accountBank.setExentGMF(true);
			accountBank.setBalance(new BigDecimal(120));

			final AccountBankEntity accountBankWithSameNumber = new AccountBankEntity();
			accountBankWithSameNumber.setNumber(accountBank.generateRandonNumberAccount());
			accountBankWithSameNumber.setId(16l);

			when(clientService.findById(anyLong())).thenReturn(client);
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);
			when(accountBankRepository.findByNumber(anyLong())).thenReturn(Optional.of(accountBankWithSameNumber));

			when(entityMapper.toEntity(accountBank)).thenReturn(new AccountBankEntity());
			when(accountBankRepository.save(any(AccountBankEntity.class))).thenReturn(new AccountBankEntity());
			when(queriesMapper.toDto(any(AccountBankEntity.class))).thenReturn(new AccountBankDTO());

			final AccountBankDTO result = accountBankService.save(accountBankDto);

			assertNotNull(accountBank.getNumber());
			assertNotNull(accountBank.getCreationDate());
			assertNull(accountBank.getLastModificationDate());
			assertNotEquals(BigDecimal.ZERO, accountBank.getBalance());

			assertNotNull(result);

			verify(clientService, times(1)).findById(anyLong());
			verify(accountBankRepository, times(1)).findByNumber(anyLong());
			verify(accountBankRepository, times(1)).save(any(AccountBankEntity.class));
		}

	}

	@Nested
	@DisplayName("Validaciones para actualizar una cuenta bancaria")
	class Update {

		@Test
		@DisplayName("Cuenta invalida, no existe")
		void accountBankInvalid() throws ClientException {

			accountBankDto.setId(43l);

			when(accountBankRepository.findById(anyLong())).thenReturn(Optional.empty());

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.update(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("No existe una cuenta bancaria"));
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Cuenta valida, pero no tien el estado a actualizar de la cuenta")
		void stateInvalid() throws ClientException {

			accountBankDto.setId(43l);

			final AccountBank accountBank = new AccountBank();

			final AccountBank accountBankFound = new AccountBank();
			accountBankFound.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBankFound.setState(AccountState.ACTIVA);
			accountBankFound.setExentGMF(true);
			accountBankFound.setBalance(new BigDecimal(120));

			when(accountBankRepository.findById(anyLong())).thenReturn(Optional.of(new AccountBankEntity()));
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);
			when(entityMapper.toDomain(any(AccountBankEntity.class))).thenReturn(accountBankFound);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.update(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("El estado de la cuenta, es obligatorio"));
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Cuenta valida, pero no se puede CANCELAR, todavia tiene saldo")
		void stateCanceledInvalid() throws ClientException {

			accountBankDto.setId(43l);

			final AccountBank accountBank = new AccountBank();
			accountBank.setState(AccountState.CANCELADA);

			final AccountBank accountBankFound = new AccountBank();
			accountBankFound.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBankFound.setState(AccountState.ACTIVA);
			accountBankFound.setExentGMF(true);
			accountBankFound.setBalance(new BigDecimal(120));

			when(accountBankRepository.findById(anyLong())).thenReturn(Optional.of(new AccountBankEntity()));
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);
			when(entityMapper.toDomain(any(AccountBankEntity.class))).thenReturn(accountBankFound);

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				accountBankService.update(accountBankDto);
			});

			assertTrue(exception.getMessage().contains("Solo se pueden cancelar las cuentas"));
			verify(accountBankRepository, never()).save(any(AccountBankEntity.class));
		}

		@Test
		@DisplayName("Cuenta valida, actualizacion exitosa")
		void updateSuccess() throws ClientException, AccountBankException {

			accountBankDto.setId(43l);

			final AccountBank accountBank = new AccountBank();
			accountBank.setState(AccountState.INACTIVA);

			final AccountBank accountBankFound = new AccountBank();
			accountBankFound.setAccountType(AccountType.CUENTA_CORRIENTE);
			accountBankFound.setState(AccountState.ACTIVA);
			accountBankFound.setExentGMF(true);
			accountBankFound.setBalance(new BigDecimal(250));

			when(accountBankRepository.findById(anyLong())).thenReturn(Optional.of(new AccountBankEntity()));
			when(domainMapper.toDomain(accountBankDto)).thenReturn(accountBank);
			when(entityMapper.toDomain(any(AccountBankEntity.class))).thenReturn(accountBankFound);

			when(entityMapper.toEntity(any(AccountBank.class))).thenReturn(new AccountBankEntity());
			when(accountBankRepository.save(any(AccountBankEntity.class))).thenReturn(new AccountBankEntity());
			when(queriesMapper.toDto(any(AccountBankEntity.class))).thenReturn(new AccountBankDTO());

			final AccountBankDTO result = accountBankService.update(accountBankDto);

			assertEquals(accountBank.getState(), accountBankFound.getState());
			assertNotNull(accountBankFound.getLastModificationDate());

			assertNotNull(result);

			verify(clientService, never()).findById(anyLong());
			verify(accountBankRepository, times(1)).save(any(AccountBankEntity.class));
		}

	}

}
