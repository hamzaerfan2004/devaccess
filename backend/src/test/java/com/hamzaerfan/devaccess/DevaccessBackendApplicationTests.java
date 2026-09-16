package com.hamzaerfan.devaccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class DevaccessBackendApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}
	
	@Test
	void ScanTest() throws Exception {
		String requestBody = """
				{
					"url": "https://example.com"
				}
				""";
		mockMvc.perform(
				post("/api/scans")
					.contentType("application/json")
					.content(requestBody)
		)
					
			.andExpect(status().isOk())
			.andExpect(content().string("Scan created for: https://example.com"));
	}
	
	@Test
	void helloEndpointReturnsExpectedResponse() throws Exception {
		mockMvc.perform(get("/api/hello"))
			.andExpect(status().isOk())
			.andExpect(content().string("Devaccess backend is running"));
	}

}
