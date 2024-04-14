package co.com.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.domain.transaction.Transaction;
import co.com.domain.transaction.TransactionException;
import co.com.dto.AccountBankDTO;
import co.com.dto.TransactionDTO;
import co.com.entities.TransactionEntity;
import co.com.entities.enumeration.AccountType;
import co.com.entities.enumeration.TransactionType;
import co.com.repository.TransactionRepository;
import co.com.service.impl.TransactionServiceImpl;
import co.com.service.mapper.transaction.TransactionDomainMapper;
import co.com.service.mapper.transaction.TransactionEntityMapper;
import co.com.service.mapper.transaction.TransactionQueriesMapper;

@DirtiesContext
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private AccountBankService accountBankService;

	@MockBean
	private TransactionQueriesMapper queriesMapper;

	@MockBean
	private TransactionDomainMapper domainMapper;

	@MockBean
	private TransactionEntityMapper entityMapper;

	private TransactionService transactionService;

	private TransactionDTO transactionDto;

	@BeforeEach
	public void initTest() {

		this.transactionDto = new TransactionDTO();

		this.transactionService = new TransactionServiceImpl(
				transactionRepository,
				queriesMapper,
				domainMapper,
				entityMapper,
				accountBankService);
	}

	@Nested
	@DisplayName("Fallos en las validaciones para guardar una transaccion")
	class SaveAndFlushFail {

		@Test
		@DisplayName("No existe ni la cuenta de origen ni la de destino")
		void wrongTransactionAccountsNotFound() {

			transactionDto.setDestiny(new AccountBankDTO());
			transactionDto.setOrigin(new AccountBankDTO());

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains("Se debe referenciar al menos una cuenta"));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
		}

		@Test
		@DisplayName("No se puede realizar la transaccion, sin definir el tipo de la operacion")
		void wrongType() {

			final AccountBankDTO accountDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(new AccountBankDTO());

			final Transaction transaction = new Transaction();

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains("El tipo de transacción es obligatoria"));
			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
		}

		@Test
		@DisplayName("No se puede realizar la transaccion, sin definir el monto de la operacion")
		void wrongAmount() {

			final AccountBankDTO accountDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(new AccountBankDTO());

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.CONSIGNACION);

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains("El monto de la transacción es obligatorio"));
			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
		}

		@Test
		@DisplayName("No se puede realizar la transaccion, si el monto es Zero")
		void wrongAmountZero() {

			final AccountBankDTO accountDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(new AccountBankDTO());

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.CONSIGNACION);
			transaction.setAmount(BigDecimal.ZERO);

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains("obligatorio y debe ser mayor a Zero"));
			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
		}

		@Test
		@DisplayName("No se puede realizar la transaccion de CONSIGNACION, sin el destino")
		void wrongConsignacion() {

			final AccountBankDTO accountOrigin = new AccountBankDTO();
			transactionDto.setDestiny(new AccountBankDTO());
			transactionDto.setOrigin(accountOrigin);

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.CONSIGNACION);
			transaction.setAmount(new BigDecimal(120));

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountOrigin)).thenReturn(Optional.of(accountOrigin));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains(TransactionType.CONSIGNACION.toString()));
			assertTrue(exception.getMessage().contains("destino"));
			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
		}

		@Test
		@DisplayName("No se puede realizar la transaccion de RETIRO, sin el origen")
		void wrongRetiro() {

			final AccountBankDTO accountDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(new AccountBankDTO());

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.RETIRO);
			transaction.setAmount(new BigDecimal(120));

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains(TransactionType.RETIRO.toString()));
			assertTrue(exception.getMessage().contains("origen"));

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
		}

		@Test
		@DisplayName("RETIRO, fallido, saldo insuficiente")
		void wrongRetiroWrongBalance() throws AccountBankException {

			final AccountBankDTO accountOrigin = new AccountBankDTO();
			transactionDto.setDestiny(new AccountBankDTO());
			transactionDto.setOrigin(accountOrigin);

			final BigDecimal balanceOrigin = BigDecimal.TEN;

			final AccountBank origin = new AccountBank();
			origin.setAccountType(AccountType.CUENTA_AHORROS);
			origin.setBalance(balanceOrigin);
			origin.setId(15l);
			origin.setNumber(origin.generateRandonNumberAccount());

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.RETIRO);
			transaction.setAmount(new BigDecimal(120));
			transaction.setOrigin(origin);

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountOrigin)).thenReturn(Optional.of(accountOrigin));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			when(entityMapper.toEntity(transaction)).thenReturn(new TransactionEntity());
			when(transactionRepository.saveAndFlush(any(TransactionEntity.class))).thenReturn(new TransactionEntity());
			when(queriesMapper.toDto(any(TransactionEntity.class))).thenReturn(new TransactionDTO());

			final Exception exception = assertThrows(AccountBankException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains(String.valueOf(transaction.getAmount())));
			assertTrue(exception.getMessage().contains(String.valueOf(balanceOrigin)));
			assertTrue(exception.getMessage().contains(String.valueOf(origin.getNumber())));

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
			verify(accountBankService, never()).updateBalance(any(AccountBank.class));
		}

		@Test
		@DisplayName("TRANSFERENCIA, fallida, es la misma cuenta")
		void wrongTransferencia() throws AccountBankException {

			final AccountBankDTO accountOriginAndDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountOriginAndDestiny);
			transactionDto.setOrigin(accountOriginAndDestiny);

			final BigDecimal balanceOrigin = BigDecimal.TEN;

			final AccountBank originAndDestiny = new AccountBank();
			originAndDestiny.setAccountType(AccountType.CUENTA_AHORROS);
			originAndDestiny.setBalance(balanceOrigin);
			originAndDestiny.setId(15l);
			originAndDestiny.setNumber(originAndDestiny.generateRandonNumberAccount());

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.TRANSFERENCIA);
			transaction.setAmount(new BigDecimal(120));
			transaction.setOrigin(originAndDestiny);
			transaction.setDestiny(originAndDestiny);

			when(accountBankService.findAccountBank(accountOriginAndDestiny)).thenReturn(Optional.of(accountOriginAndDestiny));
			when(accountBankService.findAccountBank(accountOriginAndDestiny)).thenReturn(Optional.of(accountOriginAndDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			when(entityMapper.toEntity(transaction)).thenReturn(new TransactionEntity());
			when(transactionRepository.saveAndFlush(any(TransactionEntity.class))).thenReturn(new TransactionEntity());
			when(queriesMapper.toDto(any(TransactionEntity.class))).thenReturn(new TransactionDTO());

			final Exception exception = assertThrows(TransactionException.class, () -> {
				transactionService.saveAndFlush(transactionDto);
			});

			assertTrue(exception.getMessage().contains(TransactionType.TRANSFERENCIA.toString()));
			assertTrue(exception.getMessage().contains(String.valueOf(originAndDestiny.getNumber())));

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, never()).saveAndFlush(any(TransactionEntity.class));
			verify(accountBankService, never()).updateBalance(any(AccountBank.class));
		}

	}

	@Nested
	@DisplayName("Exito en las validaciones para guardar una transaccion")
	class SaveAndFlushSuccess {

		@Test
		@DisplayName("CONSIGNACION, exitosa")
		void successConsignacion() throws TransactionException, AccountBankException {

			final AccountBankDTO accountDestiny = new AccountBankDTO();
			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(new AccountBankDTO());

			final BigDecimal balanceDestiny = BigDecimal.TEN;

			final AccountBank destiny = new AccountBank();
			destiny.setBalance(balanceDestiny);

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.CONSIGNACION);
			transaction.setAmount(new BigDecimal(120));
			transaction.setDestiny(destiny);

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			when(entityMapper.toEntity(transaction)).thenReturn(new TransactionEntity());
			when(transactionRepository.saveAndFlush(any(TransactionEntity.class))).thenReturn(new TransactionEntity());
			when(queriesMapper.toDto(any(TransactionEntity.class))).thenReturn(new TransactionDTO());

			final TransactionDTO result = transactionService.saveAndFlush(transactionDto);

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, times(1)).saveAndFlush(any(TransactionEntity.class));

			assertNotNull(transaction.getTransactionDate());
			assertEquals(TransactionType.CONSIGNACION ,transaction.getTransactionType());
			assertNotNull(result);

			final ArgumentCaptor<AccountBank> captor = ArgumentCaptor.forClass(AccountBank.class);
			verify(accountBankService, times(1)).updateBalance(captor.capture());
			assertEquals(balanceDestiny.add(transaction.getAmount()), captor.getValue().getBalance());
		}

		@Test
		@DisplayName("RETIRO, exitoso")
		void successRetiro() throws TransactionException, AccountBankException {

			final AccountBankDTO accountOrigin = new AccountBankDTO();
			transactionDto.setDestiny(new AccountBankDTO());
			transactionDto.setOrigin(accountOrigin);

			final BigDecimal balanceOrigin = BigDecimal.TEN;

			final AccountBank origin = new AccountBank();
			origin.setBalance(balanceOrigin);

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.RETIRO);
			transaction.setAmount(new BigDecimal(8));
			transaction.setOrigin(origin);

			when(accountBankService.findAccountBank(any(AccountBankDTO.class))).thenReturn(Optional.empty());
			when(accountBankService.findAccountBank(accountOrigin)).thenReturn(Optional.of(accountOrigin));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			when(entityMapper.toEntity(transaction)).thenReturn(new TransactionEntity());
			when(transactionRepository.saveAndFlush(any(TransactionEntity.class))).thenReturn(new TransactionEntity());
			when(queriesMapper.toDto(any(TransactionEntity.class))).thenReturn(new TransactionDTO());

			final TransactionDTO result = transactionService.saveAndFlush(transactionDto);

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, times(1)).saveAndFlush(any(TransactionEntity.class));

			assertNotNull(transaction.getTransactionDate());
			assertEquals(TransactionType.RETIRO ,transaction.getTransactionType());
			assertNotNull(result);

			final ArgumentCaptor<AccountBank> captor = ArgumentCaptor.forClass(AccountBank.class);
			verify(accountBankService, times(1)).updateBalance(captor.capture());
			assertEquals(balanceOrigin.subtract(transaction.getAmount()), captor.getValue().getBalance());
		}

		@Test
		@DisplayName("TRANSFERENCIA, exitosa")
		void successTransferencia() throws TransactionException, AccountBankException {

			final AccountBankDTO accountOrigin = new AccountBankDTO();
			final AccountBankDTO accountDestiny = new AccountBankDTO();

			transactionDto.setDestiny(accountDestiny);
			transactionDto.setOrigin(accountOrigin);

			final BigDecimal balanceOrigin = BigDecimal.TEN;
			final AccountBank origin = new AccountBank();
			origin.setId(556l);
			origin.setBalance(balanceOrigin);

			final BigDecimal balanceDestiny = BigDecimal.TEN;
			final AccountBank destiny = new AccountBank();
			destiny.setId(557l);
			destiny.setBalance(balanceDestiny);

			final Transaction transaction = new Transaction();
			transaction.setTransactionType(TransactionType.TRANSFERENCIA);
			transaction.setAmount(new BigDecimal(5));
			transaction.setOrigin(origin);
			transaction.setDestiny(destiny);

			final TransactionEntity transactionsCopy = new TransactionEntity();
			transactionsCopy.setId(UUID.randomUUID());

			when(accountBankService.findAccountBank(accountDestiny)).thenReturn(Optional.of(accountDestiny));
			when(accountBankService.findAccountBank(accountOrigin)).thenReturn(Optional.of(accountOrigin));
			when(domainMapper.toDomain(any(TransactionDTO.class))).thenReturn(transaction);

			when(entityMapper.toEntity(transaction)).thenReturn(new TransactionEntity());
			when(entityMapper.toEntity(any(Transaction.class))).thenReturn(transactionsCopy);

			when(transactionRepository.saveAndFlush(any(TransactionEntity.class))).thenReturn(transactionsCopy);
			when(queriesMapper.toDto(any(TransactionEntity.class))).thenReturn(new TransactionDTO());

			final TransactionDTO result = transactionService.saveAndFlush(transactionDto);

			verify(accountBankService, times(2)).findAccountBank(any(AccountBankDTO.class));
			verify(transactionRepository, times(3)).saveAndFlush(any(TransactionEntity.class));

			assertNotNull(transaction.getTransactionDate());
			assertEquals(TransactionType.TRANSFERENCIA ,transaction.getTransactionType());
			assertNotNull(result);

			final ArgumentCaptor<AccountBank> captor = ArgumentCaptor.forClass(AccountBank.class);
			verify(accountBankService, times(2)).updateBalance(captor.capture());

			final AccountBank accountOriginResult = captor.getAllValues().stream()
					.filter(account -> origin.getId().equals(account.getId()))
					.findFirst()
					.get();

			assertEquals(balanceOrigin.subtract(transaction.getAmount()), accountOriginResult.getBalance());

			final AccountBank accountDestinyResult = captor.getAllValues().stream()
					.filter(account -> destiny.getId().equals(account.getId()))
					.findFirst()
					.get();

			assertEquals(balanceDestiny.add(transaction.getAmount()), accountDestinyResult.getBalance());
		}

	}

}
