import { Component } from '@angular/core';
import { HostListener } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'signup-login-test';

// @HostListener('window:beforeunload', ['$event'])
// beforeunloadHandler(event) {
//     localStorage.clear();
// }

}
