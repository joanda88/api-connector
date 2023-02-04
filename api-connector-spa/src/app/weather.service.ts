import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {isDevMode} from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  baseUrl = "URLForBackendinProduction" // dies ist die URL unter der das Backend im produktiven Betrieb erreichbar ist

  constructor(private http: HttpClient) {
    if (isDevMode()) {
      this.baseUrl = "http://localhost:12080";
    }
  }

  private getUrl(): string {
    return this.baseUrl + '/weather/';
  }

  getWeather(longitude: String, latitude: String): Observable<String | null> {

    let params: HttpParams = new HttpParams();
    params = params.append("longitude", longitude.toString());
    params = params.append("latitude", latitude.toString());

    console.log(params)

    return this.http
      .get<String>(this.getUrl(), {
        params: params,
        observe: 'response',
        responseType: 'json'
      })
      .pipe(
        map(dto => (dto.ok && dto.body) ? dto.body : null)
      );
  }
}
