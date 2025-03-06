import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Category } from '../models/category.model';
import { environment } from '../../environments/environment';
import { Transfer } from '../models/transfer.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = `${environment.apiUrl}/categories`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * Get all categories
   */
  getCategories(): Observable<Category[]> {
    return this.http.get<any[]>(this.apiUrl)
      .pipe(
        map(categories => categories.map(category => new Category(category)))
      );
  }
  
  /**
   * Get a single category by ID
   */
  getCategory(id: number): Observable<Category> {
    return this.http.get<any>(`${this.apiUrl}/${id}`)
      .pipe(
        map(category => new Category(category))
      );
  }
  
  /**
   * Create a new category
   */
  createCategory(category: Category): Observable<Category> {
    return this.http.post<any>(this.apiUrl, category)
      .pipe(
        map(createdCategory => new Category(createdCategory))
      );
  }
  
  /**
   * Update an existing category
   */
  updateCategory(id: number, category: Partial<Category>): Observable<Category> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, category)
      .pipe(
        map(updatedCategory => new Category(updatedCategory))
      );
  }
  
  /**
   * Delete a category
   */
  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  /**
   * Get transfers for a specific category
   */
  getCategoryTransfers(id: number, params?: any): Observable<Transfer[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/transfers`, { params })
      .pipe(
        map(transfers => transfers.map(transfer => new Transfer(transfer)))
      );
  }
  
  /**
   * Search for transfers within a category by date range
   */
  searchTransfers(id: number, startDate: Date, endDate: Date): Observable<Transfer[]> {
    const params = { 
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0]
    };
    
    return this.http.get<any[]>(`${this.apiUrl}/${id}/transfers/search`, { params })
      .pipe(
        map(transfers => transfers.map(transfer => new Transfer(transfer)))
      );
  }
  
  /**
   * Get all top-level categories
   */
  getTopLevelCategories(): Observable<Category[]> {
    return this.http.get<any[]>(`${this.apiUrl}/top`)
      .pipe(
        map(categories => categories.map(category => new Category(category)))
      );
  }
  
  /**
   * Get subcategories for a specific category
   */
  getSubcategories(id: number): Observable<Category[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/subcategories`)
      .pipe(
        map(categories => categories.map(category => new Category(category)))
      );
  }
}