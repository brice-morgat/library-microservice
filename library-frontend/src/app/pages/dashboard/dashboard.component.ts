import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BookService } from '../../services/book.service';
import { UserService } from '../../services/user.service';
import { LoanService } from '../../services/loan.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, CardModule, ButtonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  counts = { books: 0, users: 0, loans: 0 };
  featuredBooks: Book[] = [];

  constructor(
    private bookService: BookService,
    private userService: UserService,
    private loanService: LoanService
  ) {}

  ngOnInit(): void {
    this.bookService.list().subscribe(list => {
      this.counts.books = list.length;
      this.featuredBooks = list.slice(0, 6);
    });
    this.userService.list().subscribe(list => this.counts.users = list.length);
    this.loanService.list().subscribe(list => this.counts.loans = list.length);
  }
}
