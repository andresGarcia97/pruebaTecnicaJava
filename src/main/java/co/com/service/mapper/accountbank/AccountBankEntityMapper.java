package co.com.service.mapper.accountbank;

import org.mapstruct.Mapper;

import co.com.domain.accountbank.AccountBank;
import co.com.entities.AccountBankEntity;
import co.com.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface AccountBankEntityMapper extends EntityMapper<AccountBank, AccountBankEntity> {}
