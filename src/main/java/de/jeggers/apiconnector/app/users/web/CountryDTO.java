package de.jeggers.apiconnector.app.users.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CountryDTO {
     final List<UserDTO> users;
     final String name;
}
