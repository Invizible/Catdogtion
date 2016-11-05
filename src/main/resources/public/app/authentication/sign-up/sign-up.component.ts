import { Component, OnInit } from '@angular/core';
import { UserService } from '../../user.service';
import { Router } from '@angular/router';
import { RegistrationUser } from './registration-user';

@Component({
  selector: 'app-sign-up',
  templateUrl: 'sign-up.component.html'
})
export class SignUpComponent implements OnInit {
  user: RegistrationUser = new RegistrationUser();


  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  signUp(): void {
    this.userService.signUp(this.user).subscribe(
      user => this.router.navigate(['/sign-in'])
    );
  }
}
