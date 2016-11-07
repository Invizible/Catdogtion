import { Image } from './image';
import { User } from './user';

export class Lot {
  id: number;
  name: string;
  description: string;
  startingPrice: number;
  creationDate: Date;
  images: Image[];
  auctioneer: User;
}