package de.jeggers.apiconnector.app.users.domain;

import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FetchUserServiceTest {

    @InjectMocks
    private FetchUserService service;

    final String testJsonUserResultsFromExternalAPI =
        "{\"results\":[{\"gender\":\"female\",\"name\":{\"title\":\"Mrs\",\"first\":\"Harriet\",\"last\":\"Loth\"},\"location\":{\"street\":{\"number\":1600,\"name\":\"Mittelstraße\"},\"city\":\"Zeven\",\"state\":\"Saarland\",\"country\":\"Germany\",\"postcode\":89548,\"coordinates\":{\"latitude\":\"-50.1137\",\"longitude\":\"148.3099\"},\"timezone\":{\"offset\":\"+9:00\",\"description\":\"Tokyo, Seoul, Osaka, Sapporo, Yakutsk\"}},\"email\":\"harriet.loth@example.com\",\"login\":{\"uuid\":\"687e2afb-eb7e-43a0-9c4e-ca706b173bd8\",\"username\":\"tinyrabbit710\",\"password\":\"deejay\",\"salt\":\"UB4oMaxW\",\"md5\":\"b6ab89da6f29c5063368de4bcbc8a08b\",\"sha1\":\"531680e21f525de66016f78135868c0346b54d95\",\"sha256\":\"edb38cfe5be6a3d933d97a2686af5b95b0c4c0430a9eee502d17ce06721ddd61\"},\"dob\":{\"date\":\"1970-01-14T01:03:52.416Z\",\"age\":52},\"registered\":{\"date\":\"2003-03-02T04:19:46.822Z\",\"age\":19},\"phone\":\"0704-1785364\",\"cell\":\"0171-4066706\",\"id\":{\"name\":\"SVNR\",\"value\":\"63 130170 L 786\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/37.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/37.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/37.jpg\"},\"nat\":\"DE\"}],\"info\":{\"seed\":\"a49c177656da80f0\",\"results\":1,\"page\":1,\"version\":\"1.4\"}}";

    @Test
    public void extrahiertUserAusJsonAntwort() throws Exception {
        List<User> users = FetchUserService.getUsersFrom(testJsonUserResultsFromExternalAPI);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUserName()).isEqualTo("tinyrabbit710");
        assertThat(users.get(0).getGender()).isEqualTo("female");
        assertThat(users.get(0).getEmail()).isEqualTo("harriet.loth@example.com");
        assertThat(users.get(0).getCountry()).isEqualTo("Germany");
        assertThat(users.get(0).getLatitude()).isEqualTo("-50.1137");
        assertThat(users.get(0).getLongitude()).isEqualTo("148.3099");
    }

    @Test
    public void wennDeserialisierungScheitertWirdEinEmptyOptionalZurueckgegeben() {
        assertThrows(JsonParseException.class, () -> {
            FetchUserService.getUsersFrom("invalides json");
        });
    }

    @Test
    public void liefertUserVonExternerAPI() {
        ReflectionTestUtils.setField(service, "externalUsersAPIURL", "https://randomuser.me/api/?&results=");
        Optional<List<User>> users = service.getUsers();
        assertThat(users).isNotEmpty();
        assertThat(users.get().size()).isEqualTo(50);
    }

    @Test
    public void liefertLeeresOptionalWennFetchScheitert() {
        ReflectionTestUtils.setField(service, "externalUsersAPIURL", "falscheURL");
        Optional<List<User>> users = service.getUsers();
        assertThat(users).isEmpty();
    }

}

