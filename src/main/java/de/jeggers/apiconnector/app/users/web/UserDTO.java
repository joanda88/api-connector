package de.jeggers.apiconnector.app.users.web;

import de.jeggers.apiconnector.app.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDTO {
    final String name;
    final String gender;
    final String email;
    final String country;

    final String longitude;

    final String latitude;

    UserDTO(User user) {
        this.country = user.getCountry();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.name = user.getUserName();
        this.longitude = user.getLongitude();
        this.latitude = user.getLatitude();
    }
}
