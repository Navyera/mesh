import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Profile} from '../models/models.profile';
import {ProfileView} from '../models/models.profile-view';
import {UserListItem} from '../models/models.user-list-item';

@Injectable({
  providedIn: 'root'
})
export class FriendService {

  constructor(private http: HttpClient) { }


  getLightProfile(userID: number) {
    return this.http.get<Profile>('/api/users/profile/light/' + userID, {observe: 'response'});
  }
  getProfile(userID: number) {
    return this.http.get<ProfileView>('/api/users/profile/' + userID, {observe: 'response'});
  }

  getProfilePicture(userID: number) {
    return this.http.get<any>('/api/content/profile_picture/' + userID, {observe: 'response' });
  }

  addFriend(userID: number) {
    return this.http.post<any>('/api/users/friends/add/' + userID, null, {observe: 'response'});
  }

  getFriends() {
    return this.http.get<number[]>('/api/users/friends', {observe : 'response'});
  }

  getFriendsOf(userID: number) {
    return this.http.get<number[]>('api/users/friends/' + userID, {observe: 'response'});
  }

  getPendingRequests() {
    return this.http.get<number[]>('/api/users/friends/pending', {observe: 'response'});
  }

  acceptFriend(userID: number) {
    return this.http.post('/api/users/friends/accept/' + userID, null, {observe: 'response'});
  }

  declineFriend(userID: number) {
    return this.http.post('/api/users/friends/decline/' + userID, null, {observe: 'response'});
  }

  getSearchResults(searchTerms: string) {
    return this.http.post<UserListItem[]>('/api/users/network/search', {payload: searchTerms}, {observe: 'response'});
  }

}
