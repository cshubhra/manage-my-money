import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      (categories: Category[]) => {
        this.categories = categories;
        this.updateCategoryNames();
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe(
        () => {
          this.categories = this.categories.filter(category => category.id !== id);
        },
        (error) => {
          console.error('Error deleting category:', error);
        }
      );
    }
  }

  updateCategoryNames(): void {
    this.categories.forEach(category => {
      category.name = this.categoryService.getCategoryName(category.id);
    });
  }

  getCategoryName(id: number): string {
    return this.categoryService.getCategoryName(id);
  }
}