import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginInfo } from '../models/models.login';
import { map } from 'rxjs/operators';
import { AlertService } from './alert.service';
import { JWTToken } from '../utils/utils.jwt';
import { Router } from '@angular/router';



@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient,
              private router: Router) { }

  login(loginInfo: LoginInfo) {
    return this.http.post<LoginInfo>('/api/login', loginInfo, { observe: 'response' })
        .pipe(map(res => {
                const token = res.headers.get('Authorization');
                if (token) {
                  console.log(token);

                  const jwtToken = new JWTToken(token);
                  localStorage.setItem('currentUser', loginInfo.email);
                  localStorage.setItem('token', token);
                  localStorage.setItem('role', jwtToken.getClaim('ROLE'));
                  localStorage.setItem('id', jwtToken.getClaim('ID'));
                }
                return res;
        }));

  }

  logout() {
    localStorage.clear();
    this.router.navigateByUrl('/');
  }

  isAuthenticated(): boolean {
    const token = new JWTToken(localStorage.getItem('token'));

    return token && !token.isExpired();
  }
}
