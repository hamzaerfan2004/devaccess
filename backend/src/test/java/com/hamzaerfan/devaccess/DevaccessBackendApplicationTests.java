package com.hamzaerfan.devaccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.nullValue;

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
            .andExpect(jsonPath("$.status").value("completed"))
            .andExpect(jsonPath("$.error").value(nullValue()))
            .andExpect(jsonPath("$.results.passes").isArray())
            .andExpect(jsonPath("$.results.violations").isArray())
            .andExpect(jsonPath("$.results.incomplete").isArray());
    }

    @Test
    void helloEndpointReturnsExpectedResponse() throws Exception {
        mockMvc.perform(get("/api/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string("Devaccess backend is running"));
    }
}