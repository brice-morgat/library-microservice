import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    InputTextareaModule,
    ToastModule,
    RouterLink
  ],
  templateUrl: './books.component.html',
  styleUrl: './books.component.scss'
})
export class BooksComponent implements OnInit {
  books: Book[] = [];
  loading = false;
  dialogVisible = false;
  editing: Book | null = null;
  searchTerm = '';

  form = this.fb.group({
    isbn: ['', Validators.required],
    title: ['', Validators.required],
    description: [''],
    publicationYear: [null as number | null],
    publisher: [''],
    totalCopies: [1, Validators.required],
    availableCopies: [1, Validators.required],
    authorFirstName: ['', Validators.required],
    authorLastName: ['', Validators.required],
    authorBiography: [''],
    categoryName: ['', Validators.required],
    categoryDescription: ['']
  });

  constructor(private fb: FormBuilder, private bookService: BookService, private message: MessageService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.loading = true;
    this.bookService.list().subscribe({
      next: books => {
        this.books = books;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger les livres' });
      }
    });
  }

  search(): void {
    if (!this.searchTerm) {
      this.loadBooks();
      return;
    }
    this.bookService.search(this.searchTerm).subscribe({
      next: books => this.books = books,
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Recherche échouée' })
    });
  }

  openCreate(): void {
    this.editing = null;
    this.form.reset({
      totalCopies: 1,
      availableCopies: 1
    });
    this.dialogVisible = true;
  }

  openEdit(book: Book): void {
    this.editing = book;
    this.form.reset({
      isbn: book.isbn,
      title: book.title,
      description: book.description,
      publicationYear: book.publicationYear ?? null,
      publisher: book.publisher,
      totalCopies: book.totalCopies,
      availableCopies: book.availableCopies,
      authorFirstName: book.author?.firstName ?? '',
      authorLastName: book.author?.lastName ?? '',
      authorBiography: book.author?.biography ?? '',
      categoryName: book.category?.name ?? '',
      categoryDescription: book.category?.description ?? ''
    });
    this.dialogVisible = true;
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.getRawValue();
    const request = this.editing
      ? this.bookService.update(this.editing.id, payload)
      : this.bookService.create(payload);

    request.subscribe({
      next: () => {
        this.dialogVisible = false;
        this.loadBooks();
        this.message.add({ severity: 'success', summary: 'OK', detail: 'Livre enregistré' });
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible d\'enregistrer' })
    });
  }

  remove(book: Book): void {
    this.bookService.delete(book.id).subscribe({
      next: () => {
        this.loadBooks();
        this.message.add({ severity: 'success', summary: 'OK', detail: 'Livre supprimé' });
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Suppression impossible' })
    });
  }
}
