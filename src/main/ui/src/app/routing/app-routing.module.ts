import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from '../login/login.component';
import {RegisterComponent} from '../register/register.component';
import {HomeComponent} from '../home/home.component';
import {AdminComponent} from '../admin/admin.component';
import {RoleguardService} from '../services/roleguard.service';
import {UserComponent} from '../user/user.component';
import {NetworkComponent} from '../network/network.component';
import {JobsComponent} from '../jobs/jobs.component';
import {MessagingComponent} from '../messaging/messaging.component';
import {NotificationsComponent} from '../notifications/notifications.component';
import {ProfileComponent} from '../profile/profile.component';
import {SettingsComponent} from '../settings/settings.component';
import {ProfileViewComponent} from '../profile-view/profile-view.component';
import {NetworkViewComponent} from '../network-view/network-view.component';


const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent},
  { path: 'user',
    component: UserComponent,
    children:  [
      { path: '', redirectTo: 'home', pathMatch: 'full'},
      {
        path: 'home',
        component: HomeComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'network',
        component: NetworkComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'network-view/:userID',
        component: NetworkViewComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'jobs',
        component: JobsComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'messaging',
        component: MessagingComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'notifications',
        component: NotificationsComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'profile/:profileID',
        component: ProfileViewComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'myprofile',
        component: ProfileComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'settings',
        component: SettingsComponent,
        canActivate: [RoleguardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      }
    ]
  },

  { path: 'admin',
    component: AdminComponent,
    canActivate: [RoleguardService],
    data: {
      expectedRoles: ['ROLE_ADMIN']
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
