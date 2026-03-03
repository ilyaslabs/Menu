package io.github.ilyaslabs.menu.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Data
public class AuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
