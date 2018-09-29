import { Component, OnInit, Input } from '@angular/core';
import { FriendService } from '../services/friend.service';
import { SafeUrl } from '@angular/platform-browser';
import { Profile } from '../models/models.profile';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileCardComponent implements OnInit {

  @Input() userID: number;
  @Input() notification: Boolean = false;
  private pictureSrc: SafeUrl;
  private model: Profile;
  private hidden: Boolean = false;

  constructor(private router: Router,
              private friendService: FriendService) { }


  ngOnInit() {

    this.model = new Profile('', '', '', '', '', null);
    this.pictureSrc = null;

    this.friendService.getProfilePicture(this.userID)
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

    this.friendService.getProfile(this.userID)
    .subscribe(
      response => {
        this.model = response.body.profileDTO;
      },

      error => {
        console.log(error);
      }
    );
  }

  onFriendAccept() {
    this.friendService.acceptFriend(this.userID).subscribe(
      response => {
        this.hidden = true;
      },
      error => {
        console.log(error);
      }
    );
  }

  onFriendDecline() {
    this.friendService.declineFriend(this.userID).subscribe(
      response => {
        this.hidden = true;
      },
      error => {
        console.log(error);
      }
    );
  }
}
