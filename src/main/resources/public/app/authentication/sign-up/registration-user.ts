import { User } from '../../user';

export class RegistrationUser extends User {
  repeatPassword: string;
  captcha: string;
}
