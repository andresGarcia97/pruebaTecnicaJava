package co.com.service;

import java.util.List;
import java.util.Optional;

import co.com.domain.accountbank.AccountBankException;
import co.com.service.dto.AccountBankDTO;

public interface AccountBankService {
	
    AccountBankDTO save(AccountBankDTO accountBank) throws AccountBankException;
    
    AccountBankDTO update(AccountBankDTO accountBank) throws AccountBankException;
    
    List<AccountBankDTO> findAll();

	Optional<AccountBankDTO> findAccountBank(final AccountBankDTO origin);
    
}
