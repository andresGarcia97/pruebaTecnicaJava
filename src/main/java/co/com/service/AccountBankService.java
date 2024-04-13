package co.com.service;

import java.util.List;

import co.com.domain.accountbank.AccountBankException;
import co.com.service.dto.AccountBankDTO;

public interface AccountBankService {
	
    AccountBankDTO save(AccountBankDTO accountBank) throws AccountBankException;
    
    AccountBankDTO update(AccountBankDTO accountBank) throws AccountBankException;
    
    List<AccountBankDTO> findAll();
    
    void delete(Long id);
}
