package de.lasse.duden.database.Users;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findUserBySubject(String subject);
    User findUserBySessionToken(String sessionToken);

}
