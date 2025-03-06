import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Finance App';
  
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Check if there's a token in local storage on app initialization
    this.authService.checkAuthentication();
  }
}