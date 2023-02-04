package de.jeggers.apiconnector.app.users.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class FetchWeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(FetchWeatherService.class);

    private String weatherAPIURL = "https://weatherdbi.herokuapp.com/data/weather/";

    public Optional<String> getWeather(String latidude, String longitude) {
        try {
            final String weatherJsonString = callWeatherAPI(weatherAPIURL + latidude +","+ longitude);
            String temp = getWeatherFrom(weatherJsonString);
            return Optional.ofNullable(temp);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    static public String callWeatherAPI(String url) throws RestClientException {
        try {
            LOG.debug("callWeatherApi using url: " + url + " to get temperature");
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, String.class);
        }
        catch (RestClientException e) {
            LOG.debug("Error while fetching users from api " + url + " \n + " + e.getMessage());
            throw e;
        }
    }

    static public String getWeatherFrom(String usersAsJson) throws Exception {
        String temp = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode result = mapper.readTree(usersAsJson);

            temp = result.get("currentConditions").get("temp").get("c").asText();

        } catch (Exception e) {
            LOG.warn("error while deserialising:" +usersAsJson + ", \n + " + e.getMessage());
            throw e;
        }

        return temp;
    }
}
