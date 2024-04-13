package co.com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.transaction.Transaction;
import co.com.entities.TransactionEntity;
import co.com.repository.TransactionRepository;
import co.com.service.TransactionService;
import co.com.service.dto.TransactionDTO;
import co.com.service.mapper.transaction.TransactionDomainMapper;
import co.com.service.mapper.transaction.TransactionEntityMapper;
import co.com.service.mapper.transaction.TransactionQueriesMapper;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final TransactionQueriesMapper queriesMapper;
    private final TransactionDomainMapper domainMapper;
    private final TransactionEntityMapper entityMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
    		TransactionQueriesMapper transactionMapper,
    		TransactionDomainMapper domainMapper,
    		TransactionEntityMapper entityMapper) {
        this.transactionRepository = transactionRepository;
        this.queriesMapper = transactionMapper;
		this.domainMapper = domainMapper;
		this.entityMapper = entityMapper;
    }

    @Override
    public TransactionDTO saveAndFlush(final TransactionDTO transaction) {
        
    	final Transaction toValidate = domainMapper.toDomain(transaction).validateCreation();
        log.debug("saveAndFlush :: toValidate: {}", toValidate);
        
        final TransactionEntity saved = transactionRepository.saveAndFlush(entityMapper.toEntity(toValidate));
        log.debug("saveAndFlush :: saved: {}", saved);
        
        return queriesMapper.toDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll().stream().map(queriesMapper::toDto).toList();
    }
    
}
