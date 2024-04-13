package co.com.service;

import java.util.List;

import co.com.service.dto.TransactionDTO;

public interface TransactionService {
	
    TransactionDTO saveAndFlush(TransactionDTO transactionDTO);
    
    List<TransactionDTO> findAll();
    
}
