import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';

import { TransferListComponent } from '../../components/transfer-list/transfer-list.component';
import { TransferFormComponent } from '../../components/transfer-form/transfer-form.component';
import { transferRoutes } from './transfer.routes';

@NgModule({
  declarations: [
    TransferListComponent,
    TransferFormComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(transferRoutes),
    ReactiveFormsModule,
    MaterialModule
  ]
})
export class TransferModule { }