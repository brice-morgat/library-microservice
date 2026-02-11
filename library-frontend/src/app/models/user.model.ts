export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  membershipNumber: string;
  membershipDate: string;
  membershipType: 'STANDARD' | 'PREMIUM';
  active: boolean;
  roles: string[];
}
