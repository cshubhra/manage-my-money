import { Routes } from '@angular/router';
import { TransferListComponent } from '../../components/transfer-list/transfer-list.component';
import { TransferFormComponent } from '../../components/transfer-form/transfer-form.component';
import { AuthGuard } from '../../guards/auth.guard';

export const transferRoutes: Routes = [
  {
    path: '',
    component: TransferListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create',
    component: TransferFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'edit/:id',
    component: TransferFormComponent,
    canActivate: [AuthGuard]
  }
];