package co.com.web.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import co.com.controller.AccountBankResource;
import co.com.dto.AccountBankDTO;
import co.com.service.AccountBankService;

@DirtiesContext
@WebMvcTest(AccountBankResource.class)
class AccountBankResourceItTest {

    private static final String ENTITY_API_URL = "/api/account-banks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    
    @MockBean
    private AccountBankService accountBankService;
    
    @Autowired
    private MockMvc restAccountBankMockMvc;
    
    @BeforeEach
    public void initTest() {
    	
    }

    @Test
    void createAccountBank() throws Exception {
    	
    	final AccountBankDTO accountBankDTO = new AccountBankDTO();
        restAccountBankMockMvc
            .perform(
            		post(ENTITY_API_URL)
            		.contentType(MediaType.APPLICATION_JSON)
            		.content(TestUtil.convertObjectToJsonBytes(accountBankDTO))
            )
            .andExpect(status().isOk());
        
        final ArgumentCaptor<AccountBankDTO> captor = ArgumentCaptor.forClass(AccountBankDTO.class);
        verify(accountBankService, times(1)).save(captor.capture());
        assertNotNull(captor.getValue());
        
    }

   
}
