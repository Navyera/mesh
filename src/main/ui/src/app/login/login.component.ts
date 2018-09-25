import { Component, OnInit } from '@angular/core';
import { LoginInfo } from '../models/models.login';
import { AuthenticationService } from '../services/authentication.service';
import { AlertService } from '../services/alert.service';
import { Router, ActivatedRoute } from '@angular/router';
import { SelectMultipleControlValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: LoginInfo =  new LoginInfo('', '');

  loading: Boolean = false;
  returnUrl: string;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private alertService: AlertService,
    private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.authenticationService.logout();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/user/home';
  }


  onSubmit() {
    this.loading = true;
    this.authenticationService
        .login(new LoginInfo(this.model.email, this.model.password))
        .subscribe(
          data => {
              this.alertService.success('Login successful', true);
              this.router.navigateByUrl(this.returnUrl);
          },
          error => {
            switch (error.status) {
              case 403:
                this.alertService.error('Invalid e-mail/password');
                break;
              default:
                this.alertService.error('An unexpected error occured');
                break;
            }
            console.log(error);
          });

  }

}
