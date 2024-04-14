package co.com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import co.com.dto.ClientDTO;
import co.com.service.ClientService;

@DirtiesContext
@WebMvcTest(ClientResource.class)
class ClientResourceTest {

	private static final String ENTITY_API_URL = "/api/clients";

	@MockBean
	private ClientService clientService;

	@Autowired
	private MockMvc restClientMockMvc;

	private ClientDTO client;

	@BeforeEach
	public void initTest() {
		client = new ClientDTO();
	}

	@Test
	void createClient() throws Exception {

		restClientMockMvc
		.perform(
				post(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(client)))
		.andExpect(status().isOk());

		final ArgumentCaptor<ClientDTO> captor = ArgumentCaptor.forClass(ClientDTO.class);
		verify(clientService, times(1)).save(captor.capture());
		assertNotNull(captor.getValue());
	}


	@Test
	void getAllClients() throws Exception {

		// Get all the clientList
		restClientMockMvc
		.perform(
				get(ENTITY_API_URL))
		.andExpect(status().isOk());

		verify(clientService, times(1)).findAll();
	}

	@Test
	void putClient() throws Exception {

		final Long id = 1255l;
		this.client = new ClientDTO();
		this.client.setId(id);

		restClientMockMvc
		.perform(
				put(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(client)))
		.andExpect(status().isOk());

		final ArgumentCaptor<ClientDTO> captor = ArgumentCaptor.forClass(ClientDTO.class);
		verify(clientService, times(1)).update(captor.capture());
		assertNotNull(captor.getValue());
		assertEquals(id, captor.getValue().getId());
	}

	@Test
	void deleteClient() throws Exception {

		final Long id = 1255l;

		final String urlDelete = ENTITY_API_URL + "/" + id;

		restClientMockMvc
		.perform(
				delete(urlDelete)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

		final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(clientService, times(1)).delete(captor.capture());
		assertNotNull(captor.getValue());
		assertEquals(id, captor.getValue());
	}
}
