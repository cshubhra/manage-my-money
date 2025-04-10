import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TransferService } from '../../services/transfer.service';
import { CategoryService } from '../../services/category.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html',
  styleUrls: ['./transfer-form.component.scss']
})
export class TransferFormComponent implements OnInit {
  transferForm: FormGroup;
  categories: any[] = [];
  isEditMode = false;
  transferId: number | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private transferService: TransferService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.transferForm = this.createTransferForm();
  }

  ngOnInit(): void {
    this.loadCategories();
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.transferId = +params['id'];
        this.loadTransfer(this.transferId);
      }
    });
  }

  createTransferForm(): FormGroup {
    return this.fb.group({
      transferDate: ['', Validators.required],
      items: this.fb.array([])
    });
  }

  get items(): FormArray {
    return this.transferForm.get('items') as FormArray;
  }

  addItem(): void {
    const itemForm = this.fb.group({
      description: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]],
      quantity: ['', [Validators.required, Validators.min(1)]],
      categoryId: ['', Validators.required]
    });

    this.items.push(itemForm);
  }

  removeItem(index: number): void {
    this.items.removeAt(index);
  }

  loadCategories(): void {
    this.categoryService.getCategories()
      .subscribe({
        next: (data) => {
          this.categories = data;
        },
        error: (error) => {
          this.snackBar.open('Error loading categories', 'Close', { duration: 3000 });
        }
      });
  }

  loadTransfer(id: number): void {
    this.loading = true;
    this.transferService.getTransfer(id)
      .subscribe({
        next: (transfer) => {
          this.transferForm.patchValue({
            transferDate: transfer.transferDate
          });

          transfer.items.forEach(item => {
            const itemForm = this.fb.group({
              description: [item.description, Validators.required],
              amount: [item.amount, [Validators.required, Validators.min(0)]],
              quantity: [item.quantity, [Validators.required, Validators.min(1)]],
              categoryId: [item.categoryId, Validators.required]
            });
            this.items.push(itemForm);
          });

          this.loading = false;
        },
        error: (error) => {
          this.snackBar.open('Error loading transfer', 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
  }

  onSubmit(): void {
    if (this.transferForm.valid) {
      this.loading = true;
      const transferData = this.transferForm.value;

      const request = this.isEditMode && this.transferId
        ? this.transferService.updateTransfer(this.transferId, transferData)
        : this.transferService.createTransfer(transferData);

      request.subscribe({
        next: () => {
          this.snackBar.open(
            `Transfer ${this.isEditMode ? 'updated' : 'created'} successfully`,
            'Close',
            { duration: 3000 }
          );
          this.router.navigate(['/transfers']);
        },
        error: (error) => {
          this.snackBar.open(
            `Error ${this.isEditMode ? 'updating' : 'creating'} transfer`,
            'Close',
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }
}