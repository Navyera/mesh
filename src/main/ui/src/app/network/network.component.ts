import { Component, OnInit } from '@angular/core';
import { FriendService } from '../services/friend.service';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {

  constructor(private friendService: FriendService) { }

  private friendIDs: number[] = null;

  ngOnInit() {
    this.friendService.getFriends().subscribe(
      response => {
        console.log(response);
        this.friendIDs = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

}
