package co.com.service.mapper.accountbank;

import org.mapstruct.Mapper;

import co.com.entities.AccountBankEntity;
import co.com.service.dto.AccountBankDTO;
import co.com.service.mapper.QueriesMapper;

@Mapper(componentModel = "spring")
public interface AccountBankQueriesMapper extends QueriesMapper<AccountBankDTO, AccountBankEntity> {}
