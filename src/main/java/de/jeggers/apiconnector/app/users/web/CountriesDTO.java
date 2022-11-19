package de.jeggers.apiconnector.app.users.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class CountriesDTO {
    final List<CountryDTO> countries;
}
