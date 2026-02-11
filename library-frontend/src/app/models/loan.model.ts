export interface Loan {
  id: number;
  userId: number;
  bookId: number;
  borrowDate?: string;
  dueDate?: string;
  returnDate?: string;
  status: string;
  message?: string;
}
