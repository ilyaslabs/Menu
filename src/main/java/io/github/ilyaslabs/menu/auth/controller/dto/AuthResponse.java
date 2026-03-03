package io.github.ilyaslabs.menu.auth.controller.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Data
public class AuthResponse {

    private String token;
    private Long tokenExpiration; // token expiration in seconds
    private String refreshToken;
    private Long refreshTokenExpiration; // refresh token expiration in seconds

    private String userId;
    private String username;

    private List<String> authorities;
}
