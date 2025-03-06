import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Finance Manager';
  
  constructor(private authService: AuthService) {}
  
  ngOnInit() {
    // Check if the user is already logged in (token exists in local storage)
    this.authService.autoLogin();
  }
}