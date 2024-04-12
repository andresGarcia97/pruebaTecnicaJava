package co.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AccountBankMapperTest {

    private AccountBankMapper accountBankMapper;

    @BeforeEach
    public void setUp() {
        accountBankMapper = new AccountBankMapperImpl();
    }
}
