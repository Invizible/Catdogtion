import { Component, OnInit, OnDestroy } from '@angular/core';
import { AccountService } from '../account.service';
import { Subscription } from 'rxjs';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private userAuthenticatedSubscription: Subscription;

  isCollapsed: boolean = true;
  authenticatedUser: User;

  constructor(
    private accountService: AccountService,
    private router: Router
  ) { }

  ngOnInit() {
    this.userAuthenticatedSubscription = this.accountService.userAuthenticated.subscribe(
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
    this.accountService.getAuthenticatedUser()
      .subscribe(
        user => this.authenticatedUser = user,
        error => console.log('No Authenticated User! Please sign in!')
      );
  }

  logout(): void {
    this.accountService.logout(
      () => {
        this.authenticatedUser = null;
        this.router.navigate(['/home'])
      }
    );
  }
}
