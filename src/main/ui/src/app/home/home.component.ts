import { Component, OnInit } from '@angular/core';
import { PostService } from '../services/post.service';
import { UserService } from '../services/user.service';
import { ProfileStats } from '../models/models.profile-stats';
import { SafeUrl } from '@angular/platform-browser';
import { Post } from '../models/models.post';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private postService: PostService,
              private userService: UserService,
              private alertService: AlertService) { }

  private pictureSrc: SafeUrl;
  private profileStats: ProfileStats;
  private postIDs: number[];

  private newPost: Post = new Post();
  private newMediaPost: Post = new Post();
  private media: File = null;
  private mediaType: string = null;
  private preview: SafeUrl = null;

  ngOnInit() {
    this.pictureSrc = null;
    this.profileStats = new ProfileStats();
    this.postIDs = null;

    this.userService.getProfilePicture()
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

    this.userService.getProfileStats().subscribe(
      response => {
        this.profileStats = response.body;
      },
      error => {
        console.log(error);
      }
    );
    this.postService.getUserFeed().subscribe(
      response => {
        this.postIDs = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

  newPostSubmit() {
    this.newPost.type = 'text';
    this.postService.post(this.newPost).subscribe(
      response => {
        this.profileStats.posts++;
        this.newPost = new Post();
        this.postIDs = [response.body.payload, ...this.postIDs];
      },
      error => {
        this.alertService.error('Your post could not be uploaded');
        console.log(error);
      }
    );
  }

  newMediaPostSubmit() {

    if (this.media === null) {
      this.alertService.error('You must first specify a media file');
      return;
    }

    this.newPost.type = 'media';
    const postBlob = new Blob([JSON.stringify(this.newMediaPost)],
                              {type: 'application/json'});

    const uploadData = new FormData();
    uploadData.append('file', this.media);
    uploadData.append('post', postBlob);

    this.postService.postMedia(uploadData).subscribe(
      response => {
        this.profileStats.posts++;
        this.newMediaPost = new Post();
        this.media = null;
        this.preview = null;
        this.postIDs = [response.body.payload, ...this.postIDs];
      },
      error => {
        this.alertService.error('Your post could not be uploaded');
        console.log(error);
      }
    );

  }

  onFileChanged(event) {
    this.preview = null;
    this.media = event.target.files[0];

    console.log(event.target.files[0]);

    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = e => this.preview = reader.result;

      reader.readAsDataURL(file);
    }
  }
}
