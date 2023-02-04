package de.jeggers.apiconnector.app.users.web;

import de.jeggers.apiconnector.app.users.domain.FetchWeatherService;
import de.jeggers.apiconnector.app.users.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FetchWeatherService fetchWeatherService;

    @Transactional(readOnly = true)
    @GetMapping("/users")
    public ResponseEntity<CountriesDTO> users() {
        List<CountryDTO> countriesWithUsers = new ArrayList<>();
        Map<String, List<UserDTO>> countriesWithUserDTOs = StreamSupport
            .stream(userRepository.findAll().spliterator(), false)
            .map(UserDTO::new).collect(Collectors.groupingBy(UserDTO::getCountry));

        countriesWithUserDTOs.forEach((country, userDTOS) -> countriesWithUsers.add(new CountryDTO(userDTOS, country)));
        return ResponseEntity.ok(new CountriesDTO(countriesWithUsers));
    }

    @GetMapping("/weather")
    public ResponseEntity<String> weather(@RequestParam String longitude, @RequestParam String latitude) {
        return ResponseEntity.ok(fetchWeatherService.getWeather(longitude, latitude).get());
    }

}
