import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../user';
import { UserService } from '../../user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-sign-in',
  templateUrl: 'sign-in.component.html'
})
export class SignInComponent implements OnInit {
  user: User = new User();

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  signIn(): void {
    this.userService.signIn(this.user, () => this.router.navigate(['/home']));
  }
}
