package co.com.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.domain.accountbank.AccountBankException;
import co.com.domain.transaction.TransactionException;
import co.com.service.TransactionService;
import co.com.service.dto.TransactionDTO;

@RestController
@RequestMapping("/api/transactions")
public class TransactionResource {

    private static final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private final TransactionService transactionService;
    
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @PostMapping("")
    public ResponseEntity<?> createTransaction(@RequestBody(required = true) final TransactionDTO transaction) {
        log.debug("REST request to save transaction: {}", transaction);
        if (transaction.getId() != null) {
            throw new IllegalArgumentException("A new transaction cannot already have an ID");
        }
        try {
			return ResponseEntity.ok(transactionService.saveAndFlush(transaction));
		} catch (TransactionException e) {
			log.error("Error to create transaction: {} ERROR: ", transaction, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (AccountBankException e) {
			log.error("Error to set accounts: {} ERROR: ", transaction, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @GetMapping("")
    public List<TransactionDTO> getAllTransactions() {
        log.debug("REST request to get all transactions");
        return transactionService.findAll();
    }
    
}
