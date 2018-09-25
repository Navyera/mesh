import { Component, Input, OnInit } from '@angular/core';
import { AlertService } from '../services/alert.service';
import { Alert, AlertType } from '../models/models.alert';

import {trigger,
        state,
        style,
        animate,
        transition
      } from '@angular/animations';


@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css'],
  animations: [
    trigger('alertState', [
      state('active',   style({
      })),
      transition('void => active', [style({opacity: 0}),
                                    animate(400) ]),
      transition('active => void', [animate(400),
                                    style({opacity: 0})])
    ])
  ]
})
export class AlertComponent implements OnInit {

  alerts: Alert[] = [];

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.getAlert().subscribe((alert: Alert) => {
      if (!alert) {
        this.alerts = this.alerts.filter(x => x.sticky);
        return;
      }
      this.alerts.push(alert);
      this.setDeath(alert);
    });
  }

  setDeath(alert: Alert) {
    setTimeout(() => this.removeAlert(alert), 2000);
  }

  removeAlert(alert: Alert) {
    this.alerts = this.alerts.filter(x => x !== alert);
  }

  cssClass(alert: Alert) {
    if (!alert) {
      return;
    }

    switch (alert.type) {
      case AlertType.Success:
          return 'alert alert-success';
      case AlertType.Error:
          return 'alert alert-danger';
      case AlertType.Info:
          return 'alert alert-info';
      case AlertType.Warning:
          return 'alert alert-warning';
    }
  }

}
