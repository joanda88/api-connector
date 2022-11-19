package de.jeggers.apiconnector.app.users.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FetchUserService {
    private static final Logger LOG = LoggerFactory.getLogger(FetchUserService.class);
    private static final int usersCountForEachSynchronization = 50;

    @Value("${users_url}")
    private String externalUsersAPIURL;

    public Optional<List<User>> getUsers() {
        try {
            final String userJsonString = callRandomUserAPI(externalUsersAPIURL + usersCountForEachSynchronization);
            List<User> userList = getUsersFrom(userJsonString);
            return Optional.of(userList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    static public String callRandomUserAPI(String url) throws RestClientException {
        try {
            LOG.debug("callRandomUserAPI using url: " + url + " to synchronize users");
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, String.class);
        }
        catch (RestClientException e) {
            LOG.debug("Error while fetching users from api " + url + " \n + " + e.getMessage());
            throw e;
        }
    }

    static public List<User> getUsersFrom(String usersAsJson) throws Exception {
        List<User> userList = new ArrayList<User>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode result = mapper.readTree(usersAsJson);

            result.get("results").elements().forEachRemaining(userJsonNode -> {
                 final User user = new User()  ;
                 user.setUserName(userJsonNode.get("login").get("username").asText());
                 user.setGender(userJsonNode.get("gender").asText());
                 user.setCountry(userJsonNode.get("location").get("country").asText());
                 user.setEmail(userJsonNode.get("email").asText());
                 user.setId(UUID.fromString(userJsonNode.get("login").get("uuid").asText()));
                 userList.add(user);
            });

        } catch (Exception e) {
            LOG.warn("error while deserialising:" +usersAsJson + ", \n + " + e.getMessage());
            throw e;
        }

        return userList;
    }
}
