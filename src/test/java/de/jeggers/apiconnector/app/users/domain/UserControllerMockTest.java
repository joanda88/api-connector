package de.jeggers.apiconnector.app.users.domain;

import de.jeggers.apiconnector.app.users.web.CountriesDTO;
import de.jeggers.apiconnector.app.users.web.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerMockTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController controller;

    @Test
    public void liefertUserAlsCountriesDTOAus() {
        User user1 = new User();
        user1.setUserName("Li");
        user1.setCountry("China");
        User user2 = new User();
        user2.setUserName("Heiko");
        user2.setCountry("Schweiz");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<CountriesDTO> users = controller.users();

        assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getBody().getCountries().size()).isEqualTo(2);
        assertThat(users.getBody().getCountries().get(1).getName()).isEqualTo("China");
        assertThat(users.getBody().getCountries().get(0).getName()).isEqualTo("Schweiz");
        assertThat(users.getBody().getCountries().get(1).getUsers().get(0).getName()).isEqualTo("Li");
        assertThat(users.getBody().getCountries().get(0).getUsers().get(0).getName()).isEqualTo("Heiko");
    }
}
