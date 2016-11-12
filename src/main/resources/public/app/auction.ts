import { User } from './user';

export class Auction {
  id: number;
  participants: User[] = [];
  startDate: Date;
  endDate: Date;
  wonPrice: number;
  winner: User;
}
