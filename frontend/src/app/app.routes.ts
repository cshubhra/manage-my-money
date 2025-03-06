import { Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { CategoryDetailComponent } from './components/categories/category-detail/category-detail.component';
import { CategoryFormComponent } from './components/categories/category-form/category-form.component';
import { TransfersComponent } from './components/transfers/transfers.component';
import { TransferDetailComponent } from './components/transfers/transfer-detail/transfer-detail.component';
import { TransferFormComponent } from './components/transfers/transfer-form/transfer-form.component';
import { ExchangesComponent } from './components/exchanges/exchanges.component';
import { ExchangeFormComponent } from './components/exchanges/exchange-form/exchange-form.component';
import { ReportsComponent } from './components/reports/reports.component';
import { AuthGuard } from './guards/auth.guard';

export const appRoutes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'categories', component: CategoriesComponent, canActivate: [AuthGuard] },
  { path: 'categories/new', component: CategoryFormComponent, canActivate: [AuthGuard] },
  { path: 'categories/:id', component: CategoryDetailComponent, canActivate: [AuthGuard] },
  { path: 'categories/:id/edit', component: CategoryFormComponent, canActivate: [AuthGuard] },
  { path: 'transfers', component: TransfersComponent, canActivate: [AuthGuard] },
  { path: 'transfers/new', component: TransferFormComponent, canActivate: [AuthGuard] },
  { path: 'transfers/:id', component: TransferDetailComponent, canActivate: [AuthGuard] },
  { path: 'transfers/:id/edit', component: TransferFormComponent, canActivate: [AuthGuard] },
  { path: 'exchanges', component: ExchangesComponent, canActivate: [AuthGuard] },
  { path: 'exchanges/new', component: ExchangeFormComponent, canActivate: [AuthGuard] },
  { path: 'reports', component: ReportsComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];