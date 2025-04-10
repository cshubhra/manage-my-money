import { Component, OnInit } from '@angular/core';
import { TransferService } from '../../services/transfer.service';
import { Transfer } from '../../models/transfer.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-transfer-list',
  templateUrl: './transfer-list.component.html',
  styleUrls: ['./transfer-list.component.scss']
})
export class TransferListComponent implements OnInit {
  transfers: Transfer[] = [];
  displayedColumns: string[] = ['transferDate', 'totalAmount', 'status', 'actions'];
  loading = false;

  constructor(
    private transferService: TransferService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadTransfers();
  }

  loadTransfers(): void {
    this.loading = true;
    this.transferService.getTransfers()
      .subscribe({
        next: (data) => {
          this.transfers = data;
          this.loading = false;
        },
        error: (error) => {
          this.snackBar.open('Error loading transfers', 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
  }
}