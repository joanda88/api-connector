package de.jeggers.apiconnector.app.users.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UpdateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FetchUserService fetchUserService;

    @InjectMocks
    private UpdateUserService service;

    @Test
    public void updateUsersFromAPIVeraendertDBNichtWennDasFetchenDerUserMisslang() {
        when(fetchUserService.getUsers()).thenReturn(Optional.empty());
        service.updateUsersFromAPI();
        verify(userRepository, times(0)).synchronizeUsersInDB(any(), eq(false));
    }

    @Test
    public void updateUsersFromAPISpeichertNeueUserInDerDB() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUserName("Gerhard");
        user.setCountry("Germany");
        user.setUserName("gh@test.de");
        user.setGender("male");

        when(fetchUserService.getUsers()).thenReturn(Optional.of(Collections.singletonList(user)));
        service.updateUsersFromAPI();
        verify(userRepository, times(1)).synchronizeUsersInDB(any(), eq(false));
    }
}

