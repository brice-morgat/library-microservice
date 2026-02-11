import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { Loan } from '../../models/loan.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    DropdownModule,
    MultiSelectModule,
    ToastModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  loading = false;
  dialogVisible = false;
  loansVisible = false;
  selectedLoans: Loan[] = [];

  roles = [
    { label: 'ADMIN', value: 'ADMIN' },
    { label: 'LIBRARIAN', value: 'LIBRARIAN' },
    { label: 'USER', value: 'USER' }
  ];

  types = [
    { label: 'STANDARD', value: 'STANDARD' },
    { label: 'PREMIUM', value: 'PREMIUM' }
  ];

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    membershipNumber: ['', Validators.required],
    membershipType: ['STANDARD', Validators.required],
    roles: [['USER']]
  });

  constructor(private fb: FormBuilder, private userService: UserService, private message: MessageService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.list().subscribe({
      next: users => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger les utilisateurs' });
      }
    });
  }

  openCreate(): void {
    this.form.reset({
      membershipType: 'STANDARD',
      roles: ['USER']
    });
    this.dialogVisible = true;
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue();
    this.userService.create(payload).subscribe({
      next: () => {
        this.dialogVisible = false;
        this.loadUsers();
        this.message.add({ severity: 'success', summary: 'OK', detail: 'Utilisateur créé' });
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Création impossible' })
    });
  }

  showLoans(user: User): void {
    this.userService.loans(user.id).subscribe({
      next: loans => {
        this.selectedLoans = loans;
        this.loansVisible = true;
      },
      error: () => this.message.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger les emprunts' })
    });
  }
}
