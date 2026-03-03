package io.github.ilyaslabs.menu.auth.db.dao;

import io.github.ilyaslabs.menu.auth.db.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByEmailAndIsEnabledAndDeletedAtIsNull(String email, Boolean isEnabled);
}
