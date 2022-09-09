import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
@Injectable()
export class AppService {

  authenticated = false;

  constructor(private http: HttpClient) {
  }

  authenticate(credentials, callback) {

    let token = credentials ? btoa(credentials.username + ':' + credentials.password) : "";
    const headers = new HttpHeaders(credentials ? {
      authorization: 'Basic ' + token
    } : {});

    sessionStorage.setItem('user_token', 'Basic ' + token);

    this.http.post('http://localhost:8082/user', null, {headers: headers}).subscribe(response => {
      if (response['name']) {
        this.authenticated = true;
      } else {
        this.authenticated = false;
      }
      return callback && callback();
    });
  }
}
