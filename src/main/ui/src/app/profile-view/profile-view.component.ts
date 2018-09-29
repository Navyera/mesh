import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Profile } from '../models/models.profile';
import { SafeUrl } from '@angular/platform-browser';
import { FriendService } from '../services/friend.service';
import { AlertService } from '../services/alert.service';
import { MessagingService } from '../services/messaging.service';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {

  private profileID: number;
  private model: Profile = new Profile('', '', '', '', '', null);
  private pictureSrc: SafeUrl = null;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private friendService: FriendService,
              private alertService: AlertService,
              private messagingService: MessagingService) { }

  ngOnInit() {

    this.route.params.subscribe(params => this.profileID = params.profileID);

    this.friendService.getProfilePicture(this.profileID)
    .subscribe(
      response => {
        if (response.body !== null) {
          const base64data = response.body.image;
          const mimeType = response.body.type;
          this.pictureSrc = 'data:' + mimeType + ';base64,' + base64data;
        }
      },
      error => {
        console.log(error);
      }
    );

    this.friendService.getProfile(this.profileID)
    .subscribe(
      response => {
        this.model = response.body.profileDTO;
      },

      error => {
        console.log(error);
      }
    );
  }

  addFriend() {
    this.friendService.addFriend(this.profileID).subscribe(
      response => {
        switch (response.body.status) {
          case 'SELF_ERROR':
            this.alertService.error('You cannot add yourself');
          break;
          case 'INACTIVE':
            this.alertService.info('Friend request sent');
          break;
          case 'ACTIVATED':
            this.alertService.info('That user has already added you! You are now friends');
          break;
          case 'ALREADY_ACTIVE':
            this.alertService.warn('You are already friends with that user');
          break;
        }
      },
      error => {
        switch (error.status) {
          case 409:
            this.alertService.error('You have already sent a friend request');
            break;
          default:
            break;
        }
        console.log(error);
      }
    );
  }

  goToNetworkView() {
    if (this.profileID.toString() === localStorage.getItem('id')) {
      this.router.navigate(['/user/network']);
    } else {
      this.router.navigate(['/user/network-view', this.profileID]);
    }
  }

  goToMessaging() {
    if (this.profileID.toString() === localStorage.getItem('id')) {
      this.router.navigate(['/user/messaging']);
    } else {
      this.messagingService.initiateConversation(this.profileID).subscribe(
        response => {
          const convID = response.body.payload;
          this.messagingService.registerSelection(convID);
          this.router.navigate(['/user/messaging'], { queryParams: {redirect: true}});
        },
        error => {
          console.log(error);
          switch (error.status) {
            case 400:
              this.alertService.error('You must first connect with that user to initiate a conversation');
            break;
            default:
              this.alertService.error('Unexpected error occured');
          }
        }
      );
    }
  }

}
