import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { ReportListComponent } from '../../components/report-list/report-list.component';
import { ReportFormComponent } from '../../components/report-form/report-form.component';
import { reportRoutes } from './report.routes';

@NgModule({
    declarations: [
        ReportListComponent,
        ReportFormComponent
    ],
    imports: [
        CommonModule,
        RouterModule.forChild(reportRoutes),
        ReactiveFormsModule,
        MaterialModule
    ]
})
export class ReportModule { }