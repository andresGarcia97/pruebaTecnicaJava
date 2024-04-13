package co.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.entities.AccountBankEntity;
import co.com.repository.AccountBankRepository;
import co.com.service.AccountBankService;
import co.com.service.ClientService;
import co.com.service.dto.AccountBankDTO;
import co.com.service.dto.ClientDTO;
import co.com.service.mapper.accountbank.AccountBankDomainMapper;
import co.com.service.mapper.accountbank.AccountBankEntityMapper;
import co.com.service.mapper.accountbank.AccountBankQueriesMapper;

@Service
@Transactional
public class AccountBankServiceImpl implements AccountBankService {

    private static final Logger log = LoggerFactory.getLogger(AccountBankServiceImpl.class);

    private final AccountBankRepository accountBankRepository;
    
    private final ClientService clientService;

    private final AccountBankQueriesMapper queriesMapper;
    private final AccountBankDomainMapper domainMapper;
    private final AccountBankEntityMapper entityMapper;

    public AccountBankServiceImpl(AccountBankRepository accountBankRepository,
    		AccountBankQueriesMapper accountBankMapper,
    		AccountBankDomainMapper domainMapper,
    		AccountBankEntityMapper entityMapper,
    		ClientService clientService) {
        this.accountBankRepository = accountBankRepository;
		this.clientService = clientService;
        this.queriesMapper = accountBankMapper;
		this.domainMapper = domainMapper;
		this.entityMapper = entityMapper;
    }

    @Override
    public AccountBankDTO save(final AccountBankDTO accountBank) throws AccountBankException {
    	
		if(accountBank.getClient() == null || accountBank.getClient().getId() == null) {
			throw new AccountBankException("La cuenta debe estar asociada a un cliente obligatoriamente");
		}
    	
    	final ClientDTO clientFound = clientService.findById(accountBank.getClient().getId())
    			.orElseThrow(() -> new AccountBankException("No existe un cliente con el ID proporcionado"));
    	
    	accountBank.setClient(clientFound);
    	
        final AccountBank toValidate = domainMapper.toDomain(accountBank).validateCreation();
        log.debug("save :: toValidate: {}", toValidate);
        
        accountBankRepository.findByNumber(toValidate.getNumber())
        .ifPresent(accountNumberRepeated -> {
        	toValidate.setNumber(toValidate.generateRandonNumberAccount());
        	log.warn("save :: Numero de cuenta repetido, generando de nuevo, newGeneration: {}, previousExist: {}, accountId: {}",
        			toValidate.getNumber(), accountNumberRepeated.getNumber(), accountNumberRepeated.getId());
        });
        
        final AccountBankEntity saved = accountBankRepository.save(entityMapper.toEntity(toValidate));
        log.debug("save :: saved: {}", saved);
        
		return queriesMapper.toDto(saved);
    }

    @Override
    public AccountBankDTO update(final AccountBankDTO accountBank) throws AccountBankException {
    	
    	final AccountBankEntity accountBankFound = accountBankRepository.findById(accountBank.getId())
    			.orElseThrow(() -> new AccountBankException("No existe una cuenta bancaria con el ID proporcionado"));
    	
        final AccountBank toUpdate = domainMapper.toDomain(accountBank).validateUpdate(entityMapper.toDomain(accountBankFound));
        log.debug("update :: toUpdate: {}", toUpdate);
        
        final AccountBankEntity updated = accountBankRepository.save(entityMapper.toEntity(toUpdate));
        log.debug("update :: updated: {}", updated);
        
        return queriesMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountBankDTO> findAll() {
        return accountBankRepository.findAll().stream().map(queriesMapper::toDto).toList();
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<AccountBankDTO> findAccountBank(final AccountBankDTO account) {
		return (account == null || account.getId() == null)
				? Optional.empty()
				: accountBankRepository.findById(account.getId()).map(queriesMapper::toDto);
	}

	@Override
	public void updateBalance(final AccountBank account) {
		accountBankRepository.updateBalanceById(account.getBalance(), account.getId());
		accountBankRepository.flush();
		log.debug("updateBalance :: balance: {}, accountId: {}", account.getBalance(), account.getId());
	}
    
}
