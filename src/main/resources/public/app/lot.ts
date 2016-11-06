import { Image } from './image';
import { User } from './user';

export class Lot {
  id: number;
  name: string;
  description: string;
  images: Image[];
  auctioneer: User;
}