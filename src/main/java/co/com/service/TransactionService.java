package co.com.service;

import java.util.List;

import co.com.domain.accountbank.AccountBankException;
import co.com.domain.transaction.TransactionException;
import co.com.dto.TransactionDTO;

public interface TransactionService {

	TransactionDTO saveAndFlush(TransactionDTO transactionDTO) throws TransactionException, AccountBankException;

	List<TransactionDTO> findAll();

}
