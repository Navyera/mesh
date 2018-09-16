import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { RegisterInfo } from '../models/models.register';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(info: RegisterInfo) {
    return this.http.post('/api/users/register', info);
  }
}
