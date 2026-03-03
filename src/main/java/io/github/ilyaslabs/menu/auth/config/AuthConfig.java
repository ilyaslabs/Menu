package io.github.ilyaslabs.menu.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@ConfigurationProperties(prefix = "auth")
public record AuthConfig(
    String issuer
) {
}
