import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { User } from '../models/models.user';
import { AuthenticationService } from '../services/authentication.service';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  private users: User[] = null;

  private selectedUsers: any = {};

  constructor(private adminService: AdminService,
              private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.adminService.getUserList().subscribe(
      response => {
        this.users = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

  onUserSelect(userID: number) {
    this.adminService.registerSelection(userID);
  }




  toggleUserSelect(userID: number) {
    if (userID in this.selectedUsers) {
      delete this.selectedUsers[userID];
    } else {
      this.selectedUsers[userID] = true;
    }
  }

  selectAllUsers() {
    this.users.forEach( (x) => this.selectedUsers[x.userID] = true);
  }

  downloadXML() {
    let userIDs: number[];
    userIDs = Object.keys(this.selectedUsers).map( (x) => parseInt(x, 10));
    this.adminService.downloadXML(userIDs).subscribe(
      response => {
        const blob = new Blob([response.body], {type: 'text/xml'});
        saveAs(blob, 'users.xml');
      },

      error => {
        console.log(error);
      }
    );
  }


  onLogout() {
    this.authenticationService.logout();
  }
}
