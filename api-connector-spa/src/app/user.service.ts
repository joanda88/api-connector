import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {isDevMode} from '@angular/core';

export class UserDTO {
  gender: string;
  name: string;
  email: string;
  country: string;

  constructor(gender: string, name: string, email: string, country: string) {
    this.country = country;
    this.email = email;
    this.name = name;
    this.gender = gender;
  }
}

export class CountryDTO {
  users: UserDTO[]
  name: String

  constructor(users: UserDTO[], name: string) {
    this.name = name
    this.users = users;
  }
}

export class CountriesDTO {
  countries: CountryDTO[]

  constructor(countries: CountryDTO[]) {
    this.countries = countries;
  }
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl = "URLForBackendinProduction" // dies ist die URL unter der das Backend im produktiven Betrieb erreichbar ist

  constructor(private http: HttpClient) {
    if (isDevMode()) {
      this.baseUrl = "http://localhost:12080";
    }
  }

  private getUrl(): string {
    return this.baseUrl + '/users';
  }

  getUsers(): Observable<CountriesDTO | null> {
    return this.http
      .get<CountriesDTO>(this.getUrl(), {
        observe: 'response',
        responseType: 'json'
      })
      .pipe(
        map(dto => (dto.ok && dto.body) ? dto.body : null)
      );
  }
}
