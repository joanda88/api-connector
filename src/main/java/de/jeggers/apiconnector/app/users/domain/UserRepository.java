package de.jeggers.apiconnector.app.users.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    @Transactional
    default void synchronizeUsersInDB(List<User> userList, boolean keepExisting) {
        if(!keepExisting) {
            this.deleteAll();
        }
        this.saveAll(userList);
    }
}
