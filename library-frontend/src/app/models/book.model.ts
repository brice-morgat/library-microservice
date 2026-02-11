export interface Book {
  id: number;
  isbn: string;
  title: string;
  description?: string;
  publicationYear?: number;
  publisher?: string;
  totalCopies: number;
  availableCopies: number;
  author?: Author;
  category?: Category;
}

export interface Author {
  id?: number;
  firstName: string;
  lastName: string;
  biography?: string;
}

export interface Category {
  id?: number;
  name: string;
  description?: string;
}
