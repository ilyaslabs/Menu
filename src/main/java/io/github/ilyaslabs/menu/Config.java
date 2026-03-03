package io.github.ilyaslabs.menu;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Configuration
@ComponentScan("io.github.ilyaslabs.menu")
@EnableMongoRepositories({
        "io.github.ilyaslabs.menu.auth.db.dao"
})
@ConfigurationPropertiesScan({"io.github.ilyaslabs.menu.auth.config"})
public class Config {
}
