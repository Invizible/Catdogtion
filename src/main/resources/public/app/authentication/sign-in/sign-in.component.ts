import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../user';
import { AccountService } from '../../account.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: 'sign-in.component.html'
})
export class SignInComponent implements OnInit {
  user: User = new User();

  constructor(
    private accountService: AccountService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  signIn(): void {
    this.accountService.signIn(this.user, () => this.router.navigate(['/home']));
  }
}
