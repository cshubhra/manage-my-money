import { Component, OnInit, Input } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Observable } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  @Input() drawer: MatSidenav;
  
  isLoggedIn$: Observable<boolean>;
  currentUser$: Observable<User | null>;
  
  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
    this.isLoggedIn$ = this.currentUser$.pipe(map(user => !!user));
  }
  
  ngOnInit(): void {}
  
  toggleSidenav(): void {
    this.drawer.toggle();
  }
  
  logout(): void {
    this.authService.logout();
  }
}