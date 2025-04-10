import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../../services/category.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Category } from '../../../models/category.model';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.scss']
})
export class CategoryFormComponent implements OnInit {
  categoryForm: FormGroup;
  isEditMode = false;
  categoryId: number | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.categoryForm = this.createForm();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.categoryId = +params['id'];
        this.loadCategory(this.categoryId);
      }
    });
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.maxLength(500)]],
      active: [true]
    });
  }

  private loadCategory(id: number): void {
    this.loading = true;
    this.categoryService.getCategory(id)
      .subscribe({
        next: (category) => {
          this.categoryForm.patchValue(category);
          this.loading = false;
        },
        error: (error) => {
          this.snackBar.open('Error loading category', 'Close', { duration: 3000 });
          this.loading = false;
          this.router.navigate(['/categories']);
        }
      });
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.loading = true;
      const category: Category = this.categoryForm.value;

      const request = this.isEditMode && this.categoryId
        ? this.categoryService.updateCategory(this.categoryId, category)
        : this.categoryService.createCategory(category);

      request.subscribe({
        next: () => {
          this.snackBar.open(
            `Category ${this.isEditMode ? 'updated' : 'created'} successfully`,
            'Close',
            { duration: 3000 }
          );
          this.router.navigate(['/categories']);
        },
        error: (error) => {
          const errorMessage = error.error?.message || 
            `Error ${this.isEditMode ? 'updating' : 'creating'} category`;
          this.snackBar.open(errorMessage, 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.categoryForm);
    }
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}