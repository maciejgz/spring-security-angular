import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AppService} from "./app.service";
import {Router} from "@angular/router";
import {finalize} from "rxjs/operators";
import * as Julian from 'julian-date/lib/julian.js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Demo';
  greeting = {};


  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.app.authenticate(undefined, undefined);

    let j = new Julian();
    console.log("date: " + j.getDate());
    console.log("julian date: " + j.julian());
  }

  logout() {
    let userToken = sessionStorage.getItem("user_token");
    const headers = new HttpHeaders(userToken ? {
      authorization: userToken
    } : {});
    this.http.post('http://localhost:8082/logout', {}, {headers: headers}).pipe(
      finalize(() => {
        this.app.authenticated = false;
      })).subscribe();
  }
}
