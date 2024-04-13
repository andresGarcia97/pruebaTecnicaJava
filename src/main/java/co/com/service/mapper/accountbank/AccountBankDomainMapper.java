package co.com.service.mapper.accountbank;

import org.mapstruct.Mapper;

import co.com.domain.accountbank.AccountBank;
import co.com.dto.AccountBankDTO;
import co.com.service.mapper.DomainMapper;

@Mapper(componentModel = "spring")
public interface AccountBankDomainMapper extends DomainMapper<AccountBankDTO, AccountBank>{}
