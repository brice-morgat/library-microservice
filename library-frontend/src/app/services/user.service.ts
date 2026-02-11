import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api.config';
import { User } from '../models/user.model';
import { Loan } from '../models/loan.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  list(): Observable<User[]> {
    return this.http.get<User[]>(`${API_URL}/api/users`);
  }

  findById(id: number): Observable<User> {
    return this.http.get<User>(`${API_URL}/api/users/${id}`);
  }

  create(payload: any): Observable<User> {
    return this.http.post<User>(`${API_URL}/api/users`, payload);
  }

  update(id: number, payload: any): Observable<User> {
    return this.http.put<User>(`${API_URL}/api/users/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/api/users/${id}`);
  }

  loans(id: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${API_URL}/api/users/${id}/loans`);
  }
}
