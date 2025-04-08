import { Routes } from '@angular/router';
import { CategoryListComponent } from '../../components/category-list/category-list.component';
import { CategoryFormComponent } from '../../components/category-form/category-form.component';
import { AuthGuard } from '../../guards/auth.guard';

export const categoryRoutes: Routes = [
  {
    path: '',
    component: CategoryListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create',
    component: CategoryFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'edit/:id',
    component: CategoryFormComponent,
    canActivate: [AuthGuard]
  }
];