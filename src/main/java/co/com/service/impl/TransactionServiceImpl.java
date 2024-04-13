package co.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.domain.transaction.Transaction;
import co.com.domain.transaction.TransactionException;
import co.com.entities.TransactionEntity;
import co.com.entities.enumeration.TransactionType;
import co.com.repository.TransactionRepository;
import co.com.service.AccountBankService;
import co.com.service.TransactionService;
import co.com.service.dto.AccountBankDTO;
import co.com.service.dto.TransactionDTO;
import co.com.service.mapper.transaction.TransactionDomainMapper;
import co.com.service.mapper.transaction.TransactionEntityMapper;
import co.com.service.mapper.transaction.TransactionQueriesMapper;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    
    private final AccountBankService accountBankService;

    private final TransactionQueriesMapper queriesMapper;
    private final TransactionDomainMapper domainMapper;
    private final TransactionEntityMapper entityMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
    		TransactionQueriesMapper transactionMapper,
    		TransactionDomainMapper domainMapper,
    		TransactionEntityMapper entityMapper,
    		AccountBankService accountBankService) {
        this.transactionRepository = transactionRepository;
		this.accountBankService = accountBankService;
        this.queriesMapper = transactionMapper;
		this.domainMapper = domainMapper;
		this.entityMapper = entityMapper;
    }

    @Override
    public TransactionDTO saveAndFlush(final TransactionDTO transaction) throws TransactionException, AccountBankException {
    	
    	final Optional<AccountBankDTO> existAccountOrigin = accountBankService.findAccountBank(transaction.getOrigin());
    	final Optional<AccountBankDTO> existAccountDestiny = accountBankService.findAccountBank(transaction.getDestiny());
    	
		if(existAccountOrigin.isEmpty() && existAccountDestiny.isEmpty()) {
			throw new TransactionException("Se debe referenciar al menos una cuenta, ya sea destino o origen");
		}
        
		existAccountOrigin.ifPresent(transaction::setOrigin);
		existAccountDestiny.ifPresent(transaction::setDestiny);
		
    	final Transaction toValidate = domainMapper.toDomain(transaction).validateCreation();
        log.debug("saveAndFlush :: toValidate: {}", toValidate);
        
		final AccountBank destiny = toValidate.getDestiny();
		final AccountBank origin = toValidate.getOrigin();
		
		if(destiny != null && TransactionType.CONSIGNACION.equals(toValidate.getTransactionType())) {
			accountBankService.updateBalance(destiny);
		}
				
		if(origin != null && TransactionType.RETIRO.equals(toValidate.getTransactionType())) {
			accountBankService.updateBalance(origin);
		}
		
		if(destiny != null && origin != null && TransactionType.TRANSFERENCIA.equals(toValidate.getTransactionType())) {
			accountBankService.updateBalance(destiny);
			accountBankService.updateBalance(origin);
			
        	final Transaction copyConsignacion = new Transaction(toValidate, TransactionType.CONSIGNACION);
        	final TransactionEntity savedConsignacion = transactionRepository.saveAndFlush(entityMapper.toEntity(copyConsignacion));
        	log.debug("saveAndFlush :: savedConsignacion: {}", savedConsignacion);
        	
        	final Transaction copyRetiro = new Transaction(toValidate, TransactionType.RETIRO);  
            final TransactionEntity savedRetiro = transactionRepository.saveAndFlush(entityMapper.toEntity(copyRetiro));
            log.debug("saveAndFlush :: savedRetiro: {}", savedRetiro);
		}
        
        final TransactionEntity saved = transactionRepository.saveAndFlush(entityMapper.toEntity(toValidate));
        log.debug("saveAndFlush :: saved: {}", saved);
        
        return queriesMapper.toDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        return transactionRepository.findAll().stream().map(queriesMapper::toDto).toList();
    }
    
}
