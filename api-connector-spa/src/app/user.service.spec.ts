import { TestBed } from '@angular/core/testing';

import {CountriesDTO, UserService} from './user.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should request users', () => {

    const countriesWithUserData: CountriesDTO = {
      countries: [
        {name: "Sweden",
          users: [
            {gender: "female", name: "Anne", country: "Belgium", email: "a@b.com", longitude: "12.3", latitude: "2.3"}
          ]
        }
      ]
    };

    service.getUsers().subscribe(countries => {
      expect(countries).toEqual(countriesWithUserData);
    });

    const fakeRequest = httpMock.expectOne({ url: 'http://localhost:12080/users', method: 'GET' });
    fakeRequest.flush(countriesWithUserData, { status: 200, statusText: 'OK' });
    httpMock.verify();
  });
});
