import {Component, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {RegisterInfo} from '../models/models.register';
import {RegisterService} from '../services/register.service';
import {AlertService} from '../services/alert.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  model: RegisterInfo = new RegisterInfo('', '', '', '', '', '');

  constructor(private router: Router,
              private alertService: AlertService,
              private registerService: RegisterService) { }

  ngOnInit() {
    localStorage.clear();
  }

  onSubmit() {
    this.registerService.register(this.model)
        .pipe(first())
        .subscribe(
          data => {
            this.alertService.success('Registration successful', true);
            this.router.navigate(['/login']);
          },
          error => {
            switch (error.status) {
              case 409:
                this.alertService.error('E-mail address already in use');
                break;
              default:
                this.alertService.error('An unexpected error occured');
                break;
            }
            console.log(error);
          });
  }
}
