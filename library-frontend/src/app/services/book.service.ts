import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { Book } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  constructor(private http: HttpClient) {}

  list(): Observable<Book[]> {
    return this.http.get<Book[]>(`${API_URL}/api/books`);
  }

  search(query: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${API_URL}/api/books/search`, { params: { q: query } });
  }

  findById(id: number): Observable<Book> {
    return this.http.get<Book>(`${API_URL}/api/books/${id}`);
  }

  create(payload: any): Observable<Book> {
    return this.http.post<Book>(`${API_URL}/api/books`, payload);
  }

  update(id: number, payload: any): Observable<Book> {
    return this.http.put<Book>(`${API_URL}/api/books/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/api/books/${id}`);
  }

  updateCopies(id: number, payload: any): Observable<Book> {
    return this.http.patch<Book>(`${API_URL}/api/books/${id}/copies`, payload);
  }
}
