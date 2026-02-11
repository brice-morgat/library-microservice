import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { API_URL } from '../core/api.config';

interface TokenResponse {
  token: string;
  tokenType: string;
  expiresAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly tokenKey = 'library_token';
  private readonly authState = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${API_URL}/api/auth/login`, { email, password })
      .pipe(tap(res => this.setToken(res.token)));
  }

  register(payload: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    membershipNumber: string;
    membershipType: 'STANDARD' | 'PREMIUM';
  }): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${API_URL}/api/auth/register`, payload)
      .pipe(tap(res => this.setToken(res.token)));
  }

  refresh(token: string): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${API_URL}/api/auth/refresh`, { token })
      .pipe(tap(res => this.setToken(res.token)));
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.authState.next(false);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return this.authState.value;
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) {
      return [];
    }
    const payload = this.decodePayload(token);
    const roles = payload?.roles;
    return Array.isArray(roles) ? roles : [];
  }

  hasRole(required: string | string[]): boolean {
    const roles = this.getRoles();
    const requiredRoles = Array.isArray(required) ? required : [required];
    return requiredRoles.some(role => roles.includes(role));
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
    this.authState.next(true);
    this.router.navigate(['/dashboard']);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  private decodePayload(token: string): any {
    try {
      const payload = token.split('.')[1];
      const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(json);
    } catch {
      return null;
    }
  }
}
