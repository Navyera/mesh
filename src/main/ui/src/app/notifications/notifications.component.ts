import { Component, OnInit } from '@angular/core';
import { FriendService } from '../services/friend.service';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  constructor(private friendService: FriendService,
              private alertService: AlertService) { }

  private pendingIDs: number[] = null;

  ngOnInit() {
    this.friendService.getPendingRequests().subscribe(
      response => {
        this.pendingIDs = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

}
