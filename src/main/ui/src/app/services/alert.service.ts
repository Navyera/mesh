import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { Alert, AlertType } from '../models/models.alert';
import { filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private subject = new Subject<Alert>();


  constructor(private router: Router) {
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.clear();
      }
    });
  }

  success(message: string, sticky = false) {
    this.alert(AlertType.Success, message, sticky);
  }

  error(message: string, sticky = false) {
    this.alert(AlertType.Error, message, sticky);
  }

  info(message: string, sticky = false) {
    this.alert(AlertType.Info, message, sticky);
  }

  warn(message: string, sticky = false) {
    this.alert(AlertType.Warning, message, sticky);
  }

  alert(type: AlertType, message: string, sticky = false) {
    this.subject.next(new Alert(type, message, sticky));
  }

  getAlert(): Observable<any> {
    return this.subject.asObservable();
  }

  clear() {
    this.subject.next();
  }

}
