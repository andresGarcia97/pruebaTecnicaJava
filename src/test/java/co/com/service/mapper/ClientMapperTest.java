package co.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;

import co.com.service.mapper.client.ClientEntityMapper;

class ClientMapperTest {

    private ClientEntityMapper clientMapper;

    @BeforeEach
    public void setUp() {
        clientMapper = new ClientMapperImpl();
    }
}
