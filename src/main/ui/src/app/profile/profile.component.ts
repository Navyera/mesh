import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../services/user.service';
import { AlertService } from '../services/alert.service';
import { SafeUrl } from '@angular/platform-browser';
import { Profile } from '../models/models.profile';
import { copyAnimationEvent } from '@angular/animations/browser/src/render/shared';
import { ProfileView } from '../models/models.profile-view';
import { Permissions, PermissionsManager } from '../models/models.permissions';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(private userService: UserService,
              private alertService: AlertService) {}

  private model: ProfileView;
  private initialModel: ProfileView;
  private pictureSrc: SafeUrl;
  private permissionsMgr = new PermissionsManager(new Permissions());
  private newSkill = '';

  ngOnInit() {
    this.initialModel = new ProfileView(new Profile(), new Permissions());
    this.model = new ProfileView(new Profile(), new Permissions());

    this.userService.getProfilePicture()
    .subscribe(
      response => {
        console.log(response);
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

    this.userService.getProfile()
    .subscribe(
      response => {
        // Copy response data into handler objects.
        this.initialModel.profileDTO = response.body.profileDTO;
        this.initialModel.permissionsDTO.assign(response.body.permissionsDTO);

        // Create a second copy, to track changes.
        this.model.profileDTO = this.copy(response.body.profileDTO);
        this.model.permissionsDTO.assign(response.body.permissionsDTO);

        // Make permission manager object track the current model.
        this.permissionsMgr.changePermissonsObj(this.model.permissionsDTO);
      },
      error => {
        this.alertService.error('Could not load your profile');
        console.log(error);
      }
    );
  }

  onSubmit() {
    this.userService.updateProfile(this.model).subscribe(
      response => {
        this.alertService.success('Your profile was updated successfully');
        this.initialModel.profileDTO = this.copy(this.model.profileDTO);
      },
      error => {
        console.log(error);
        this.alertService.error('Your profile could not be updated');
      }
    );
  }

  profileChanged() {
    if (JSON.stringify(this.initialModel) !== JSON.stringify(this.model)) {
      return true;
    }
    return false;
  }

  addSkill() {

    if (this.newSkill === '') {
      return;
    }
    if (!this.model.profileDTO.skills.includes(this.newSkill)) {
      this.model.profileDTO.skills.push(this.newSkill);
      this.newSkill = '';
    } else {
      this.alertService.error('Duplicate skill description');
    }
  }

  deleteSkill(skill: string) {
    this.model.profileDTO.skills = this.model.profileDTO.skills.filter(x => x !== skill);
  }

  copy(obj: any) {
    return JSON.parse(JSON.stringify(obj));
  }

  onSelectionChange(event: any) {
    console.log(event.target.value);
  }
}
