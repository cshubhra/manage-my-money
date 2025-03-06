import { Component, OnInit } from '@angular/core';
import { TransferService } from '../../services/transfer.service';
import { CategoryService } from '../../services/category.service';
import { CurrencyService } from '../../services/currency.service';
import { AuthService } from '../../services/auth.service';
import { Transfer } from '../../models/transfer.model';
import { Category } from '../../models/category.model';
import { Currency } from '../../models/currency.model';
import { User } from '../../models/user.model';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  transfers: Transfer[] = [];
  categories: Category[] = [];
  defaultCurrency: Currency | null = null;
  currentUser: User | null = null;
  loading = true;
  
  constructor(
    private transferService: TransferService,
    private categoryService: CategoryService,
    private currencyService: CurrencyService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // Get current user
    this.authService.currentUser$
      .pipe(
        // Then get default currency
        switchMap(user => {
          this.currentUser = user;
          return this.currencyService.getDefaultCurrency();
        }),
        // Then get newest transfers
        switchMap(currency => {
          this.defaultCurrency = currency;
          return this.transferService.getNewestTransfers();
        }),
        // Then get categories
        switchMap(transfers => {
          this.transfers = transfers;
          return this.categoryService.getTopLevelCategories();
        })
      )
      .subscribe(
        categories => {
          this.categories = categories;
          this.loading = false;
        },
        error => {
          console.error('Error loading dashboard data', error);
          this.loading = false;
        }
      );
  }
}