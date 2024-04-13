package co.com.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.domain.accountbank.AccountBankException;
import co.com.service.AccountBankService;
import co.com.service.dto.AccountBankDTO;

@RestController
@RequestMapping("/api/account-banks")
public class AccountBankResource {

    private static final Logger log = LoggerFactory.getLogger(AccountBankResource.class);

    private final AccountBankService accountBankService;

    public AccountBankResource(AccountBankService accountBankService) {
        this.accountBankService = accountBankService;
    }
    
    @PostMapping("")
    public ResponseEntity<?> createAccountBank(@RequestBody(required = true) final AccountBankDTO accountBank) {
        log.debug("REST request to save accountBank: {}", accountBank);
        try {
			return ResponseEntity.ok(accountBankService.save(accountBank));
		} catch (AccountBankException e) {
			log.error("Error to create accountBank: {} ERROR: ", accountBank, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @PutMapping("")
    public ResponseEntity<?> updateAccountBank(@RequestBody(required = true) final AccountBankDTO accountBank) {
        log.debug("REST request to update accountBank: {}", accountBank);
        if (accountBank == null || accountBank.getId() == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
        try {
			return ResponseEntity.ok().body(accountBankService.update(accountBank));
		} catch (AccountBankException e) {
			log.error("Error to update accountBank: {} ERROR: ", accountBank, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @GetMapping("")
    public List<AccountBankDTO> getAll() {
        log.debug("REST request to get all accountBanks");
        return accountBankService.findAll();
    }
    
    @DeleteMapping("/{accountBankId}")
    public ResponseEntity<Void> deleteAccountBank(@PathVariable(required = true) Long accountBankId) {
        log.debug("REST request to delete accountBankId: {}", accountBankId);
        accountBankService.delete(accountBankId);
        return ResponseEntity.noContent().build();
    }
}
