package co.com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.entities.AccountBankEntity;
import co.com.repository.AccountBankRepository;
import co.com.service.AccountBankService;
import co.com.service.dto.AccountBankDTO;
import co.com.service.mapper.AccountBankMapper;

@Service
@Transactional
public class AccountBankServiceImpl implements AccountBankService {

    private final Logger log = LoggerFactory.getLogger(AccountBankServiceImpl.class);

    private final AccountBankRepository accountBankRepository;

    private final AccountBankMapper accountBankMapper;

    public AccountBankServiceImpl(AccountBankRepository accountBankRepository, AccountBankMapper accountBankMapper) {
        this.accountBankRepository = accountBankRepository;
        this.accountBankMapper = accountBankMapper;
    }

    @Override
    public AccountBankDTO save(AccountBankDTO accountBankDTO) {
        log.debug("Request to save AccountBankEntity : {}", accountBankDTO);
        AccountBankEntity accountBank = accountBankMapper.toEntity(accountBankDTO);
        accountBank = accountBankRepository.save(accountBank);
        return accountBankMapper.toDomain(accountBank);
    }

    @Override
    public AccountBankDTO update(AccountBankDTO accountBankDTO) {
        log.debug("Request to update AccountBankEntity : {}", accountBankDTO);
        AccountBankEntity accountBank = accountBankMapper.toEntity(accountBankDTO);
        accountBank = accountBankRepository.save(accountBank);
        return accountBankMapper.toDomain(accountBank);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountBankDTO> findAll() {
        log.debug("Request to get all AccountBanks");
        return accountBankRepository.findAll().stream().map(accountBankMapper::toDomain).toList();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountBankEntity : {}", id);
        accountBankRepository.deleteById(id);
    }
}
