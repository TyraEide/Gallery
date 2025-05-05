package com.Gallery.integration.filter;

import com.Gallery.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    // An protected URL, which we need to be authenticated to access
    private static final String PROTECTED_URL = "/api/courses";

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get(PROTECTED_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectRequestWithInvalidToken() throws Exception {
        String invalidToken = "invalid.jwt.token";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        mockMvc.perform(get(PROTECTED_URL)
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // This test does not work a the moment since the test does not take into account the users autherization
   // @Test
   // void shouldAllowRequestWithValidToken() throws Exception {
   //     String validToken = "valid.jwt.token";
   //     String email = "user@example.com";
   //     when(jwtUtil.validateToken(validToken)).thenReturn(true);
   //     when(jwtUtil.extractEmail(validToken)).thenReturn(email);
//
   //     mockMvc.perform(get(PROTECTED_URL)
   //             .header("Authorization", "Bearer " + validToken)
   //             .contentType(MediaType.APPLICATION_JSON))
   //             .andExpect(status().isOk());
   // }
}//
