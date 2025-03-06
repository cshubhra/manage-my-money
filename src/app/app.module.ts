import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { CategoryFormComponent } from './components/category-form/category-form.component';

const routes: Routes = [
  { path: 'categories', component: CategoryListComponent },
  { path: 'categories/new', component: CategoryFormComponent },
  { path: 'categories/:id/edit', component: CategoryFormComponent },
  { path: '', redirectTo: '/categories', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    CategoryListComponent,
    CategoryFormComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }