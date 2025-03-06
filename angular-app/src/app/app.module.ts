import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { SidebarComponent } from './components/shared/sidebar/sidebar.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CategoryListComponent } from './components/categories/category-list/category-list.component';
import { CategoryEditComponent } from './components/categories/category-edit/category-edit.component';
import { TransferListComponent } from './components/transfers/transfer-list/transfer-list.component';
import { TransferEditComponent } from './components/transfers/transfer-edit/transfer-edit.component';
import { CurrencyListComponent } from './components/currencies/currency-list/currency-list.component';
import { CurrencyEditComponent } from './components/currencies/currency-edit/currency-edit.component';
import { ExchangeListComponent } from './components/exchanges/exchange-list/exchange-list.component';
import { ExchangeEditComponent } from './components/exchanges/exchange-edit/exchange-edit.component';
import { ReportListComponent } from './components/reports/report-list/report-list.component';
import { ReportEditComponent } from './components/reports/report-edit/report-edit.component';
import { GoalListComponent } from './components/goals/goal-list/goal-list.component';
import { GoalEditComponent } from './components/goals/goal-edit/goal-edit.component';
import { DebtorListComponent } from './components/debtors/debtor-list/debtor-list.component';
import { CreditorListComponent } from './components/creditors/creditor-list/creditor-list.component';
import { ImportComponent } from './components/import/import.component';
import { UserSettingsComponent } from './components/user/user-settings/user-settings.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';

/**
 * Main Angular module for the financial management application
 * 
 * This module imports all the necessary Angular modules and declares all components
 * used throughout the application.
 */
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    NavbarComponent,
    SidebarComponent,
    DashboardComponent,
    CategoryListComponent,
    CategoryEditComponent,
    TransferListComponent,
    TransferEditComponent,
    CurrencyListComponent,
    CurrencyEditComponent,
    ExchangeListComponent,
    ExchangeEditComponent,
    ReportListComponent,
    ReportEditComponent,
    GoalListComponent,
    GoalEditComponent,
    DebtorListComponent,
    CreditorListComponent,
    ImportComponent,
    UserSettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }