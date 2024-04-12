package co.com.service.mapper;

import org.mapstruct.Mapper;

import co.com.entities.TransactionEntity;
import co.com.service.dto.TransactionDTO;


@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, TransactionEntity> {
	
}
