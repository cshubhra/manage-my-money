import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];
  displayedColumns: string[] = ['name', 'description', 'status', 'actions'];
  loading = false;

  constructor(
    private categoryService: CategoryService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.loading = true;
    this.categoryService.getCategories()
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.loading = false;
        },
        error: (error) => {
          this.snackBar.open('Error loading categories', 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id)
        .subscribe({
          next: () => {
            this.loadCategories();
            this.snackBar.open('Category deleted successfully', 'Close', { duration: 3000 });
          },
          error: (error) => {
            this.snackBar.open('Error deleting category', 'Close', { duration: 3000 });
          }
        });
    }
  }
}