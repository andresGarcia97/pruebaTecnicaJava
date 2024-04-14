package co.com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	@MockBean
	private AccountBankService accountBankService;

	@Autowired
	private MockMvc restAccountBankMockMvc;

	private AccountBankDTO accountBank;

	@BeforeEach
	public void initTest() {
		this.accountBank = new AccountBankDTO();
	}

	@Test
	void createAccountBank() throws Exception {

		restAccountBankMockMvc
		.perform(
				post(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(accountBank)))
		.andExpect(status().isOk());

		final ArgumentCaptor<AccountBankDTO> captor = ArgumentCaptor.forClass(AccountBankDTO.class);
		verify(accountBankService, times(1)).save(captor.capture());
		assertNotNull(captor.getValue());
	}

	@Test
	void getAllAccountBanks() throws Exception {
		restAccountBankMockMvc
		.perform(
				get(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		verify(accountBankService, times(1)).findAll();
	}

	@Test
	void putAccountBank() throws Exception {

		final Long id = 124l;
		this.accountBank = new AccountBankDTO();
		this.accountBank.setId(id);

		restAccountBankMockMvc
		.perform(
				put(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(accountBank)))
		.andExpect(status().isOk());

		final ArgumentCaptor<AccountBankDTO> captor = ArgumentCaptor.forClass(AccountBankDTO.class);
		verify(accountBankService, times(1)).update(captor.capture());
		assertNotNull(captor.getValue());
		assertEquals(id, captor.getValue().getId());
	}
	
}
