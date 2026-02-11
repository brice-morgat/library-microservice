import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { Loan } from '../models/loan.model';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  constructor(private http: HttpClient) {}

  list(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${API_URL}/api/loans`);
  }

  findByUser(userId: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${API_URL}/api/loans/user/${userId}`);
  }

  findByBook(bookId: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${API_URL}/api/loans/book/${bookId}`);
  }

  borrow(payload: { userId: number; bookId: number }): Observable<Loan> {
    return this.http.post<Loan>(`${API_URL}/api/loans/borrow`, payload);
  }

  returnLoan(id: number, payload?: { returnDate?: string }): Observable<Loan> {
    return this.http.post<Loan>(`${API_URL}/api/loans/${id}/return`, payload ?? {});
  }

  reserve(payload: { userId: number; bookId: number }): Observable<any> {
    return this.http.post(`${API_URL}/api/loans/reserve`, payload);
  }
}
