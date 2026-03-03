package io.github.ilyaslabs.menu.auth.controller;

import io.github.ilyaslabs.menu.BaseTest;
import io.github.ilyaslabs.menu.auth.db.document.User;
import io.github.ilyaslabs.menu.auth.exception.UserNotFoundException;
import io.github.ilyaslabs.menu.auth.service.UserService;
import io.github.ilyaslabs.microservice.exception.HttpResponseException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
class AuthControllerTest extends BaseTest {

    private final String URI_AUTHENTICATE = "/api/authenticate";

    @MockitoBean
    private UserService userService;

    /**
     * Tests successful authentication with valid username and password.
     */
    @Test
    void testAuthenticateSuccessfully() throws Exception {

        // Given
        String request = """
                {
                    "username": "admin@domain.com",
                    "password": "password"
                }
                """;

        var user = new User().setId(new ObjectId())
                .setUsername("admin")
                .setAuthorities(List.of("ADMIN"));

        // When
        doReturn(user).when(userService).findUserMatchingPassword(eq("admin@domain.com"), eq("password"));

        mockMvc.perform(
                post(URI_AUTHENTICATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        ).andExpect(status().isOk());
    }

    /**
     * Tests authentication failure when user is not found with given credentials.
     */
    @Test
    void testAuthenticationFailedWhenUserIsNotFound() throws Exception {

        // Given
        String request = """
                {
                    "username": "admin@domain.com",
                    "password": "password"
                }
                """;

        // When
        doThrow(UserNotFoundException.class).when(userService).findUserMatchingPassword(eq("admin@domain.com"), eq("password"));

        // Then
        String responseString = mockMvc.perform(
                        post(URI_AUTHENTICATE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        var response = objectMapper.readValue(responseString, HttpResponseException.ResponseBody.class);

        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Invalid username or password");

    }

    /**
     * Tests authentication validation failure when username field is missing from request.
     */
    @Test
    void testAuthenticationFailedWhenNoEmailIsPassedExpectErrorResponse() throws Exception {
        // Given
        String request = """
                {
                    "password": "password"
                }
                """;

        mockMvc.perform(
                        post(URI_AUTHENTICATE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fields.username").value("must not be blank"));
    }
}