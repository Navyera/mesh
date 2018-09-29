import { Component, OnInit } from '@angular/core';
import { FriendService } from '../services/friend.service';
import { AlertService } from '../services/alert.service';
import { NotificationService } from '../services/notification.service';
import { Notification } from '../models/models.notification';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  constructor(private friendService: FriendService,
              private alertService: AlertService,
              private notificationService: NotificationService) { }

  private pendingIDs: number[] = null;
  private notifications: Notification[] = null;

  ngOnInit() {
    this.friendService.getPendingRequests().subscribe(
      response => {
        this.pendingIDs = response.body;
      },
      error => {
        console.log(error);
      }
    );

    this.notificationService.getNotifications().subscribe(
      response => {
        this.notifications = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

}
