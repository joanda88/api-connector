package de.jeggers.apiconnector.app.users.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
@Sql({"/data.sql"})
public class UserRepositoryIT {

    @Autowired
    private DataSource dataSource;
    private static String userId = "9ec5db59-aa78-45a6-a936-908e467d6f6e";

    @Autowired
    private UserRepository repo;

    @Test
    public void findetUserById() {
        Optional<User> optionalUser = repo.findById(UUID.fromString(userId));

        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getUserName()).isEqualTo("Frauke Schmidt");
        assertThat(optionalUser.get().getCountry()).isEqualTo("United States");
        assertThat(optionalUser.get().getGender()).isEqualTo("female");
        assertThat(optionalUser.get().getEmail()).isEqualTo("fs@test.usa");
    }

    @Test
    public void synchronizeUsersInDBSpeichertNeueUser() {
        final UUID neuerUserId = UUID.randomUUID();
        repo.synchronizeUsersInDB(Collections.singletonList(new User(
                neuerUserId,
                "testUser",
                "testUser@test.de",
                "Austria",
                "female"
        )), false);

        Optional<User> optionalUser = repo.findById(neuerUserId);

        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getUserName()).isEqualTo("testUser");
        assertThat(optionalUser.get().getCountry()).isEqualTo("Austria");
        assertThat(optionalUser.get().getGender()).isEqualTo("female");
        assertThat(optionalUser.get().getEmail()).isEqualTo("testUser@test.de");
    }

    @Test
    public void synchronizeUsersInDBBehaeltExistierendeUserBeiKeepExisitingTrue() {
        final UUID neuerUserId = UUID.randomUUID();
        repo.synchronizeUsersInDB(Collections.singletonList(new User(
                neuerUserId,
                "testUser",
                "testUser@test.de",
                "Austria",
                "female"
        )), true);

        Optional<User> optionalUserNew = repo.findById(neuerUserId);
        Optional<User> optionalUserOld = repo.findById(UUID.fromString(userId));

        assertThat(optionalUserOld).isPresent();
        assertThat(optionalUserNew).isPresent();
    }

    @Test
    public void synchronizeUsersInDBEntferntExistierendeUserBeiKeepExisitingFalse() {
        final UUID neuerUserId = UUID.randomUUID();
        repo.synchronizeUsersInDB(Collections.singletonList(new User(
                neuerUserId,
                "testUser",
                "testUser@test.de",
                "Austria",
                "female"
        )), false);

        Optional<User> optionalUserNew = repo.findById(neuerUserId);
        Optional<User> optionalUserOld = repo.findById(UUID.fromString(userId));

        assertThat(optionalUserOld).isEmpty();
        assertThat(optionalUserNew).isPresent();
    }

    @AfterEach
    public void removeTestData() {
        new ResourceDatabasePopulator(new ClassPathResource("/remove-data.sql")).execute(dataSource);
    }
}
