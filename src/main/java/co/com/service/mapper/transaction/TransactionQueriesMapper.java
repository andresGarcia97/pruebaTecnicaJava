package co.com.service.mapper.transaction;

import org.mapstruct.Mapper;

import co.com.entities.TransactionEntity;
import co.com.service.dto.TransactionDTO;
import co.com.service.mapper.QueriesMapper;

@Mapper(componentModel = "spring")
public interface TransactionQueriesMapper extends QueriesMapper<TransactionDTO, TransactionEntity> {}
