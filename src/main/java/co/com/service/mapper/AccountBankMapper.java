package co.com.service.mapper;

import org.mapstruct.Mapper;

import co.com.entities.AccountBankEntity;
import co.com.service.dto.AccountBankDTO;

@Mapper(componentModel = "spring")
public interface AccountBankMapper extends EntityMapper<AccountBankDTO, AccountBankEntity> {
	
}
