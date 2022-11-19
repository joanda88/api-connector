import { Component } from '@angular/core';
import {CountryDTO, UserDTO, UserService} from "./user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private countryWithUsersList: CountryDTO[] = [];
  private countries: Set<String> = new Set<String>();
  private NOCOUNTRYSELECTED: String = "Select Country";
  selectedCountry: String = this.NOCOUNTRYSELECTED;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.update();
  }

  update(): void {
    this.userService.getUsers().subscribe(countries => {
      if(countries !== null) {
        this.countryWithUsersList = countries.countries;
        this.extractCountries();
        this.resetSelect();
      }
    })
  }

  extractCountries(): void {
    this.countries.clear();
    this.countryWithUsersList.forEach(country => this.countries.add(country.name));
    this.countries = new Set(Array.from(this.countries).sort());
  }

  resetSelect(): void {
    this.selectedCountry = this.NOCOUNTRYSELECTED;
  }

  getCountries(): Set<String> {
    return this.countries;
  }

  getUserList(): UserDTO[] {
   if(this.selectedCountry == this.NOCOUNTRYSELECTED) {
     return this.countryWithUsersList
       .flatMap(countryWithUser => countryWithUser.users)
   }
   return this.countryWithUsersList
     .filter(countryWithUser => countryWithUser.name == this.selectedCountry)
     .flatMap(countryWithUser => countryWithUser.users);
  }
}
