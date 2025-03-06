import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent implements OnInit {
  categoryForm: FormGroup;
  isEditMode: boolean = false;
  categoryId: number | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.categoryForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: [''],
      parentId: [null],
      isActive: [true]
    });
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

  loadCategory(id: number): void {
    this.categoryService.getCategory(id).subscribe(
      (category: Category) => {
        this.categoryForm.patchValue(category);
      },
      (error) => {
        console.error('Error loading category:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      const categoryData: Category = this.categoryForm.value;
      
      if (this.isEditMode && this.categoryId) {
        this.categoryService.updateCategory(this.categoryId, categoryData).subscribe(
          () => {
            this.router.navigate(['/categories']);
          },
          (error) => {
            console.error('Error updating category:', error);
          }
        );
      } else {
        this.categoryService.createCategory(categoryData).subscribe(
          () => {
            this.router.navigate(['/categories']);
          },
          (error) => {
            console.error('Error creating category:', error);
          }
        );
      }
    }
  }
}