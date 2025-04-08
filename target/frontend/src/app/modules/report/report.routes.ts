import { Routes } from '@angular/router';
import { ReportListComponent } from '../../components/report-list/report-list.component';
import { ReportFormComponent } from '../../components/report-form/report-form.component';
import { AuthGuard } from '../../guards/auth.guard';

export const reportRoutes: Routes = [
    {
        path: '',
        component: ReportListComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'create',
        component: ReportFormComponent,
        canActivate: [AuthGuard]
    },
    {
        path: ':id',
        component: ReportFormComponent,
        canActivate: [AuthGuard]
    }
];