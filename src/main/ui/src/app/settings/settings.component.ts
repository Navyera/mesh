import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {AlertService} from '../services/alert.service';
import {Location} from '@angular/common';
import {Settings} from '../models/models.settings';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor(private userService: UserService,
              private alertService: AlertService,
              private location: Location) { }

  private model: Settings;

  private imgPreview: any;

  ngOnInit() {
    this.model = new Settings();
    this.userService.getSettings().subscribe(
      response => {
        this.model = response.body;
      },
      error => {
        this.alertService.error('Could not load your settings');
      }
    );

  }

  userDetailsSubmit() {

    const newInfo: any = {
      firstName: this.model.firstName,
      lastName: this.model.lastName,
      phone: this.model.phone
    };

    this.userService.updateUserDetails(newInfo).subscribe(
      response => {
        this.alertService.success('User details changed successfully');
      },
      error => {
        this.alertService.error('User details update failed');
      }
    );

    this.refresh();
  }

  emailSubmit() {
    const newInfo: any = { email: this.model.email };

    this.userService.updateEmail(newInfo).subscribe(
      response => {
        this.alertService.success('Your e-mail was changed successfully');
      },
      error => {
        switch (error.status) {
          case 409:
            this.alertService.error('There is already an existing ' +
                                    'account with that email');
            break;
          default:
            this.alertService.error('Your e-mail could not be changed');
            break;
        }
      }
    );

    this.refresh();
  }

  passwordSubmit() {
    const newInfo: any = {
      oldPassword: this.model.oldPassword,
      newPassword: this.model.newPassword,
    };

    this.userService.updatePassword(newInfo).subscribe(
      response => {
        this.alertService.success('Your password was changed successfully');
      },
      error => {
        switch (error.status) {
          case 409:
            this.alertService.error('Invalid old password');
            break;
          default:
            this.alertService.error('Your password could not be changed');
            break;
        }
      }
    );
  }


  pictureSubmit() {
    const uploadData = new FormData();
    uploadData.append('file', this.model.picture);

    this.userService.updateProfilePicture(uploadData).subscribe(
      response => {
        this.alertService.success('Your picture was successfully uploaded');
      },
      error => {
        this.alertService.error('Your picture could not be uploaded');
      }
    );
  }


  onFileChanged(event) {
    this.model.picture = event.target.files[0];

    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = e => this.imgPreview = reader.result;

      reader.readAsDataURL(file);
    }
  }

  refresh() {
    this.userService.getSettings();
  }

  cancel() {
    this.location.back();
  }

}
