import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';

// Material Modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';

// Components
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { SidenavComponent } from './components/shared/sidenav/sidenav.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { CategoryDetailComponent } from './components/categories/category-detail/category-detail.component';
import { CategoryFormComponent } from './components/categories/category-form/category-form.component';
import { TransfersComponent } from './components/transfers/transfers.component';
import { TransferDetailComponent } from './components/transfers/transfer-detail/transfer-detail.component';
import { TransferFormComponent } from './components/transfers/transfer-form/transfer-form.component';
import { ExchangesComponent } from './components/exchanges/exchanges.component';
import { ExchangeFormComponent } from './components/exchanges/exchange-form/exchange-form.component';
import { ReportsComponent } from './components/reports/reports.component';

// Services
import { AuthService } from './services/auth.service';
import { CategoryService } from './services/category.service';
import { TransferService } from './services/transfer.service';
import { CurrencyService } from './services/currency.service';
import { ExchangeService } from './services/exchange.service';
import { ReportService } from './services/report.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Routes
import { appRoutes } from './app.routes';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    SidenavComponent,
    CategoriesComponent,
    CategoryDetailComponent,
    CategoryFormComponent,
    TransfersComponent,
    TransferDetailComponent,
    TransferFormComponent,
    ExchangesComponent,
    ExchangeFormComponent,
    ReportsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes),
    // Material Modules
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  providers: [
    AuthService,
    CategoryService,
    TransferService,
    CurrencyService,
    ExchangeService,
    ReportService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }