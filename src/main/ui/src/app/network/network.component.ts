import {Component, OnDestroy, OnInit} from '@angular/core';
import {FriendService} from '../services/friend.service';
import {UserListItem} from '../models/models.user-list-item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserListModalComponent} from '../user-list-modal/user-list-modal.component';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit, OnDestroy {

  constructor(private friendService: FriendService,
              private modalService: NgbModal) { }

  private friendIDs: number[] = null;

  private searchTerms: string = null;
  private searchResults: UserListItem[] = null;

  ngOnInit() {
    this.friendService.getFriends().subscribe(
      response => {
        this.friendIDs = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

  onSubmit() {
    if (!this.searchTerms || this.searchTerms.length === 0) {
      return;
    }

    this.friendService.getSearchResults(this.searchTerms).subscribe(
      response => {
        this.searchResults = response.body;
        this.modalOpen();
      },
      error => {
        console.log(error);
      }
    );


  }

  modalOpen() {
    const modalRef = this.modalService.open(UserListModalComponent);
    modalRef.componentInstance.profiles = this.searchResults;
    modalRef.componentInstance.title = 'Search Results';
    modalRef.componentInstance.emptyMsg = 'No Results...';
  }

  ngOnDestroy() {
    this.modalService.dismissAll();
  }

}
