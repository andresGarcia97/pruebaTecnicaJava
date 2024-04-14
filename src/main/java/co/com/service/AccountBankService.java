package co.com.service;

import java.util.List;
import java.util.Optional;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.domain.client.ClientException;
import co.com.dto.AccountBankDTO;

public interface AccountBankService {

	AccountBankDTO save(AccountBankDTO accountBank) throws AccountBankException, ClientException;

	AccountBankDTO update(AccountBankDTO accountBank) throws AccountBankException;

	List<AccountBankDTO> findAll();

	Optional<AccountBankDTO> findAccountBank(AccountBankDTO account);

	void updateBalance(AccountBank account);

}
