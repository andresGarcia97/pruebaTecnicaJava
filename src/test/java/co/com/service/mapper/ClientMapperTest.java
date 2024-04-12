package co.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;

import co.com.service.mapper.client.ClientQueriesMapper;

class ClientMapperTest {

    private ClientQueriesMapper clientMapper;

    @BeforeEach
    public void setUp() {
        clientMapper = new ClientMapperImpl();
    }
}
