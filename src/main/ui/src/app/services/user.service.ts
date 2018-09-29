import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Settings } from '../models/models.settings';
import { Profile } from '../models/models.profile';
import { ProfileView } from '../models/models.profile-view';
import { ProfileStats } from '../models/models.profile-stats';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }


  getProfile() {
    return this.http.get<ProfileView>('/api/users/profile', {observe: 'response'});
  }

  getProfileStats() {
    return this.http.get<ProfileStats>('/api/users/profile/stats', {observe: 'response'});
  }

  updateProfile(profileView: ProfileView) {
    return this.http.post('/api/users/profile', profileView, {observe: 'response'});
  }

  getSettings() {
    return this.http.get<Settings>('/api/users/settings', {observe: 'response'});
  }

  updateUserDetails(newInfo: any) {
    return this.http.put('/api/users/settings/user_details', newInfo, {observe: 'response'});
  }

  updateEmail(newInfo: any) {
    return this.http.put('/api/users/settings/email', newInfo, {observe: 'response'});
  }

  updatePassword(newInfo: any) {
    return this.http.post('/api/users/settings/password', newInfo, {observe: 'response'});
  }

  updateProfilePicture(profilePicture: FormData) {
    return this.http.post('/api/content/profile_picture', profilePicture, {observe: 'response'});
  }

  getProfilePicture() {
    return this.http.get<any>('/api/content/profile_picture', {observe: 'response' });
  }


}
