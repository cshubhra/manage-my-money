import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { CategoryFormComponent } from './components/category-form/category-form.component';
import { categoryRoutes } from './category.routes';

@NgModule({
  declarations: [
    CategoryListComponent,
    CategoryFormComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(categoryRoutes),
    ReactiveFormsModule,
    MaterialModule
  ]
})
export class CategoryModule { }