package de.lasse.duden.database.Users;

import de.lasse.duden.database.Word.Word;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findUserByUsername(String username);

}
