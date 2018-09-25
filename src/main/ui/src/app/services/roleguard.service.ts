import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { AlertService } from './alert.service';


@Injectable({
  providedIn: 'root'
})
export class RoleguardService implements CanActivate {

  constructor(private router: Router,
              private authService: AuthenticationService,
              private alertService: AlertService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    const expectedRoles = route.data.expectedRoles;
    const actualRole   = localStorage.getItem('role');

    if (!this.authService.isAuthenticated()) {
      this.alertService.warn('Please login again', true);
      this.router.navigate(['login'], {queryParams: { returnUrl: state.url}});
      return false;
    }
    if (!expectedRoles.includes(actualRole)) {
      this.alertService.warn('Unauthorized route', true);
      this.router.navigate(['login'], {queryParams: { returnUrl: state.url}});
      return false;
    }
    return true;
  }
}
