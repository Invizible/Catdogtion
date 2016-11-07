import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../account.service';
import { Router } from '@angular/router';
import { RegistrationUser } from './registration-user';

@Component({
  selector: 'app-sign-up',
  templateUrl: 'sign-up.component.html'
})
export class SignUpComponent implements OnInit {
  user: RegistrationUser = new RegistrationUser();


  constructor(
    private accountService: AccountService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  signUp(): void {
    this.accountService.signUp(this.user).subscribe(
      user => this.router.navigate(['/sign-in'])
    );
  }
}
