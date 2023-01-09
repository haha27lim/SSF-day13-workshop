package sg.edu.nus.iss.app.day13workshop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import sg.edu.nus.iss.app.day13workshop.controller.AddressbookController;
import sg.edu.nus.iss.app.day13workshop.model.Contact;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Day13workshopApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private AddressbookController addressbkController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate restTemplate;

	private static final String TEST_URL = "http://localhost:";

	@Test
	void contextLoads() {
		assertThat(addressbkController).isNotNull();
	}

	@Test
	public void testAddressbookController() {
		assertThat(this.restTemplate.getForObject(TEST_URL + port + "/addressbook",
				String.class)).contains("Your contact information");
	}

	@Test
	public void testContact() throws Exception {
		Contact c = new Contact();
		c.setEmail("a@a.com");
		c.setName("Kenneth");
		c.setPhoneNumber("422343");
		assertEquals(c.getEmail(), "a@a.com");
	}

	@Test
	public void testSaveAddressUsingUrlEncoded() {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
					.post(TEST_URL + port + "/addressbook")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content("name=kenneth&email=k@k.com&phoneNumber=34324"));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveAddressUsingRestTemplate() {
		final String baseUrl = TEST_URL + port + "/addressbook";
		try {
			URI uri = new URI(baseUrl);
			Contact ctc = new Contact();
			ctc.setEmail("kk@k.com");
			ctc.setName("Test Engineer");
			ctc.setPhoneNumber("3243432");
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Contact> request = new HttpEntity<>(ctc, headers);
			ResponseEntity<String> result = this.restTemplate.postForEntity(TEST_URL + port
					+ "/addressbook", request, String.class);
			assertEquals(HttpStatus.OK, result.getStatusCode());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
