import { Routes } from '@angular/router';
import { CategoryListComponent } from './category-list/category-list.component';
import { CategoryFormComponent } from './category-form/category-form.component';
import { AuthGuard } from '../../guards/auth.guard';
import { AdminGuard } from '../../guards/admin.guard';

export const categoryRoutes: Routes = [
  {
    path: '',
    component: CategoryListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create',
    component: CategoryFormComponent,
    canActivate: [AuthGuard, AdminGuard]
  },
  {
    path: 'edit/:id',
    component: CategoryFormComponent,
    canActivate: [AuthGuard, AdminGuard]
  }
];