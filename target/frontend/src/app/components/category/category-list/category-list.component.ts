import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models/category.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

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
    private snackBar: MatSnackBar,
    private dialog: MatDialog
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

  onDelete(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Delete Category', message: 'Are you sure you want to delete this category?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.categoryService.deleteCategory(id)
          .subscribe({
            next: () => {
              this.loadCategories();
              this.snackBar.open('Category deleted successfully', 'Close', { duration: 3000 });
            },
            error: (error) => {
              this.snackBar.open('Error deleting category', 'Close', { duration: 3000 });
              this.loading = false;
            }
          });
      }
    });
  }
}