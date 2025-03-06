import { Component, OnInit } from '@angular/core';
import { TransferService } from '../../services/transfer.service';
import { CategoryService } from '../../services/category.service';
import { Transfer } from '../../models/transfer.model';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-transfer-list',
  templateUrl: './transfer-list.component.html',
  styleUrls: ['./transfer-list.component.css']
})
export class TransferListComponent implements OnInit {
  transfers: Transfer[] = [];
  categories: Category[] = [];

  constructor(
    private transferService: TransferService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    this.loadTransfers();
    this.loadCategories();
  }

  loadTransfers(): void {
    this.transferService.getTransfers().subscribe(
      (transfers: Transfer[]) => {
        this.transfers = transfers;
      },
      (error) => {
        console.error('Error loading transfers:', error);
      }
    );
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      (categories: Category[]) => {
        this.categories = categories;
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  getCategoryName(categoryId: number): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.name : 'Unknown';
  }

  deleteTransfer(id: number): void {
    if (confirm('Are you sure you want to delete this transfer?')) {
      this.transferService.deleteTransfer(id).subscribe(
        () => {
          this.transfers = this.transfers.filter(transfer => transfer.id !== id);
        },
        (error) => {
          console.error('Error deleting transfer:', error);
        }
      );
    }
  }
}