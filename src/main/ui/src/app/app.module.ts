import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './routing/app-routing.module';
import { CheckPasswordDirective } from '../directives/check-password.directive';
import { AlertComponent } from './alert/alert.component';
import { AdminComponent } from './admin/admin.component';

import { StringToDatePipe } from './utils/utils.string-to-date.pipe';
import { JwtInterceptor } from './utils/utils.jwt-interceptor';
import { UserComponent } from './user/user.component';
import { HomeComponent } from './home/home.component';
import { NetworkComponent } from './network/network.component';
import { JobsComponent } from './jobs/jobs.component';
import { MessagingComponent } from './messaging/messaging.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { ProfileComponent } from './profile/profile.component';
import { SettingsComponent } from './settings/settings.component';
import { ProfileCardComponent } from './profile-card/profile-card.component';
import { ProfileViewComponent } from './profile-view/profile-view.component';
import { PostComponent } from './post/post.component';
import { CommentComponent } from './comment/comment.component';
import { NotificationViewComponent } from './notification-view/notification-view.component';
import { ChatProfileComponent } from './chat-profile/chat-profile.component';
import { ChatViewComponent } from './chat-view/chat-view.component';
import { AdminProfileViewComponent } from './admin-profile-view/admin-profile-view.component';
import { NetworkViewComponent } from './network-view/network-view.component';
import { UserListItemComponent } from './user-list-item/user-list-item.component';
import { UserListModalComponent } from './user-list-modal/user-list-modal.component';
import { JobViewComponent } from './job-view/job-view.component';


@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    CheckPasswordDirective,
    HomeComponent,
    AlertComponent,
    AdminComponent,
    UserComponent,
    HomeComponent,
    NetworkComponent,
    JobsComponent,
    MessagingComponent,
    NotificationsComponent,
    ProfileComponent,
    SettingsComponent,
    ProfileCardComponent,
    ProfileViewComponent,
    PostComponent,
    CommentComponent,
    StringToDatePipe,
    NotificationViewComponent,
    ChatProfileComponent,
    ChatViewComponent,
    AdminProfileViewComponent,
    NetworkViewComponent,
    UserListItemComponent,
    UserListModalComponent,
    JobViewComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  entryComponents: [PostComponent, UserListModalComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
