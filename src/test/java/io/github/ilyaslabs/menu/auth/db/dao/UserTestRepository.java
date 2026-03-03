package io.github.ilyaslabs.menu.auth.db.dao;

import io.github.ilyaslabs.menu.auth.db.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
public interface UserTestRepository extends MongoRepository<User, ObjectId> {
}