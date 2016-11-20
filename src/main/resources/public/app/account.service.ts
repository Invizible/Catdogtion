import { Injectable } from '@angular/core';
import { User } from './user';
import { Http, Headers } from '@angular/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class AccountService {
  private usersUrl: string = '/api/users';

  private authenticatedUserSource = new BehaviorSubject<boolean>(false);
  userAuthenticated = this.authenticatedUserSource.asObservable();

  private authenticatedUser: User;

  constructor(private http: Http) {
  }

  signIn(user: User, callback, errorCallback?): void {
    this.http.post('/api/authentication', `username=${user.username}&password=${user.password}`,
      {headers: new Headers({'Content-Type': 'application/x-www-form-urlencoded'})})
      .subscribe(
        () => {
          this.authenticatedUserSource.next(true);
          callback();
        },
        errorCallback
      );
  }

  getAuthenticatedUser(callback): void {
    let userObservable = this.authenticatedUser ? Observable.of(this.authenticatedUser)
      : this.http.get(`${this.usersUrl}/search/findCurrentUser`)
      .map(resp => this.authenticatedUser = resp.json() as User);

    userObservable.subscribe(
      callback,
      () => console.log('No Authenticated User! Please sign in!')
    );
  }

  logout(callback): void {
    this.http.post('/api/logout', '').subscribe(
      () => {
        // to get a new csrf token call the api
        this.http.options('api').subscribe(() => {
          this.authenticatedUser = null;
          callback();
        });
      }
    );
  }

  signUp(user: User): Observable<User> {
    return this.http.post('api/account/registration', user)
      .map(resp => resp.json() as User);
  }
}
