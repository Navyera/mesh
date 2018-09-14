import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { RegisterInfo } from '../models/models.register';
import { RegisterService } from '../services/register.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  model: RegisterInfo = new RegisterInfo('', '', null, null, '', '', '');

  constructor(private router: Router,
              private registerService: RegisterService) { }

  ngOnInit() {
  }

  onSubmit() {
    this.registerService.register(this.model)
        .pipe(first())
        .subscribe(
          data => {
            console.log(data);
          },
          error => {
            console.log(error);
          });
  }
}
