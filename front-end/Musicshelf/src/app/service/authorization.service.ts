import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  loginUrl: string = '';
  signupUrl: string = '';

  constructor(private http:HttpClient) {
      this.loginUrl = "http://localhost:8080/api/user/login";
      this.signupUrl="http://localhost:8080/api/user/register";
   }

   login(user : User) : Observable<any>{
      return this.http.post<any>(this.loginUrl, user);
   }

   signup(user : User) : Observable<any>{
    return this.http.post<any>(this.signupUrl, user);
   }
}
