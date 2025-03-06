import { Component } from '@angular/core';

/**
 * Root component for the financial management application
 * 
 * This component serves as the main container for the application
 * and includes the navbar, sidebar, and router outlet.
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Finance Manager';
}