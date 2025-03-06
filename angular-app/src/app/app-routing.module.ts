import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
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
import { AuthGuard } from './guards/auth.guard';

/**
 * Application routing configuration
 * 
 * This module defines all the routes for the application and applies 
 * the AuthGuard to protected routes that require authentication.
 */
const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  
  // Categories
  { path: 'categories', component: CategoryListComponent, canActivate: [AuthGuard] },
  { path: 'categories/new', component: CategoryEditComponent, canActivate: [AuthGuard] },
  { path: 'categories/:id', component: CategoryEditComponent, canActivate: [AuthGuard] },
  
  // Transfers
  { path: 'transfers', component: TransferListComponent, canActivate: [AuthGuard] },
  { path: 'transfers/new', component: TransferEditComponent, canActivate: [AuthGuard] },
  { path: 'transfers/:id', component: TransferEditComponent, canActivate: [AuthGuard] },
  
  // Currencies
  { path: 'currencies', component: CurrencyListComponent, canActivate: [AuthGuard] },
  { path: 'currencies/new', component: CurrencyEditComponent, canActivate: [AuthGuard] },
  { path: 'currencies/:id', component: CurrencyEditComponent, canActivate: [AuthGuard] },
  
  // Exchanges
  { path: 'exchanges', component: ExchangeListComponent, canActivate: [AuthGuard] },
  { path: 'exchanges/new', component: ExchangeEditComponent, canActivate: [AuthGuard] },
  { path: 'exchanges/:id', component: ExchangeEditComponent, canActivate: [AuthGuard] },
  
  // Reports
  { path: 'reports', component: ReportListComponent, canActivate: [AuthGuard] },
  { path: 'reports/new', component: ReportEditComponent, canActivate: [AuthGuard] },
  { path: 'reports/:id', component: ReportEditComponent, canActivate: [AuthGuard] },
  
  // Goals
  { path: 'goals', component: GoalListComponent, canActivate: [AuthGuard] },
  { path: 'goals/new', component: GoalEditComponent, canActivate: [AuthGuard] },
  { path: 'goals/:id', component: GoalEditComponent, canActivate: [AuthGuard] },
  
  // Debtor & Creditor
  { path: 'debtors', component: DebtorListComponent, canActivate: [AuthGuard] },
  { path: 'creditors', component: CreditorListComponent, canActivate: [AuthGuard] },
  
  // Import
  { path: 'import', component: ImportComponent, canActivate: [AuthGuard] },
  
  // User Settings
  { path: 'settings', component: UserSettingsComponent, canActivate: [AuthGuard] },
  
  // Wild card route - redirect to dashboard
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }