package co.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;

import co.com.service.mapper.accountbank.AccountBankQueriesMapper;

class AccountBankQueriesMapperTest {

    private AccountBankQueriesMapper accountBankMapper;

    @BeforeEach
    public void setUp() {
        accountBankMapper = new AccountBankMapperImpl();
    }
}
