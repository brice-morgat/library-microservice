import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { LoanService } from '../../services/loan.service';
import { Loan } from '../../models/loan.model';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TagModule,
    ToastModule
  ],
  templateUrl: './loans.component.html',
  styleUrl: './loans.component.scss'
})
export class LoansComponent implements OnInit {
  loans: Loan[] = [];
  loading = false;

  form = this.fb.group({
    userId: [null as number | null, Validators.required],
    bookId: [null as number | null, Validators.required]
  });

  constructor(private fb: FormBuilder, private loanService: LoanService, private message: MessageService) {}

  ngOnInit(): void {
    this.loadLoans();
  }

  loadLoans(): void {
    this.loading = true;
    this.loanService.list().subscribe({
      next: loans => {
        this.loans = loans;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger les emprunts' });
      }
    });
  }

  borrow(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.getRawValue() as { userId: number; bookId: number };
    this.loanService.borrow(payload).subscribe({
      next: () => {
        this.form.reset();
        this.loadLoans();
        this.message.add({ severity: 'success', summary: 'OK', detail: 'Emprunt créé' });
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Emprunt impossible' })
    });
  }

  returnLoan(loan: Loan): void {
    this.loanService.returnLoan(loan.id).subscribe({
      next: () => {
        this.loadLoans();
        this.message.add({ severity: 'success', summary: 'OK', detail: 'Livre retourné' });
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Retour impossible' })
    });
  }

  statusSeverity(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'PENDING':
        return 'warning';
      case 'OVERDUE':
        return 'danger';
      case 'RETURNED':
        return 'info';
      default:
        return 'info';
    }
  }
}
