import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../shared/material.module';
import { CategoryListComponent } from './category-list/category-list.component';
import { CategoryFormComponent } from './category-form/category-form.component';
import { categoryRoutes } from './category.routes';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CategoryListComponent,
    CategoryFormComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(categoryRoutes),
    ReactiveFormsModule,
    MaterialModule,
    SharedModule
  ]
})
export class CategoryModule { }