import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BookService } from '../../services/book.service';
import { LoanService } from '../../services/loan.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, CardModule, ButtonModule, TagModule, ToastModule],
  templateUrl: './book-detail.component.html',
  styleUrl: './book-detail.component.scss'
})
export class BookDetailComponent implements OnInit {
  book: Book | null = null;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private loanService: LoanService,
    private message: MessageService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      return;
    }
    this.loading = true;
    this.bookService.findById(id).subscribe({
      next: book => {
        this.book = book;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Livre introuvable' });
      }
    });
  }

  borrow(): void {
    if (!this.book) {
      return;
    }
    const userId = Number(prompt('ID utilisateur pour l\'emprunt:'));
    if (!userId) {
      return;
    }
    this.loanService.borrow({ userId, bookId: this.book.id }).subscribe({
      next: () => this.message.add({ severity: 'success', summary: 'OK', detail: 'Emprunt créé' }),
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Emprunt impossible' })
    });
  }

  reserve(): void {
    if (!this.book) {
      return;
    }
    const userId = Number(prompt('ID utilisateur pour la réservation:'));
    if (!userId) {
      return;
    }
    this.loanService.reserve({ userId, bookId: this.book.id }).subscribe({
      next: () => this.message.add({ severity: 'success', summary: 'OK', detail: 'Réservation créée' }),
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Réservation impossible' })
    });
  }
}
