package io.github.ilyaslabs.menu.auth.db.document;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Document(collection = "users")
@Data
@Accessors(chain = true)
public class User {

    @Id
    private ObjectId id;

    private String username;
    private String password;
    private String email;

    private List<String> authorities;

    private Boolean isEnabled;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
