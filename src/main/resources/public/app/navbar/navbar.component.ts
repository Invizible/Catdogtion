import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../user.service';
import { Subscription } from 'rxjs';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit, OnDestroy {
  private userAuthenticatedSubscription: Subscription;

  isCollapsed: boolean = true;
  authenticatedUser: User;

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit() {
    this.userAuthenticatedSubscription = this.userService.userAuthenticated.subscribe(
      (authenticated) => {
        if (authenticated) {
          this.loadAuthenticatedUser();
        }
      }
    );

    this.loadAuthenticatedUser();
  }

  ngOnDestroy(): void {
    this.userAuthenticatedSubscription.unsubscribe();
  }

  private loadAuthenticatedUser(): void {
    this.userService.getAuthenticatedUser()
      .subscribe(
        user => this.authenticatedUser = user
      );
  }

  logout(): void {
    this.userService.logout().subscribe(
      () => {
        this.authenticatedUser = null;
        this.router.navigate(['/home'])
      }
    );
  }
}
