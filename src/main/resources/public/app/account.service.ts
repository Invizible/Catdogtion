import { Injectable } from '@angular/core';
import { User } from './user';
import { Http, Headers } from '@angular/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class AccountService {
  private usersUrl: string = '/api/users';

  private authenticatedUserSource = new BehaviorSubject<boolean>(false);
  userAuthenticated = this.authenticatedUserSource.asObservable();

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

  getAuthenticatedUser(): Observable<User> {
    return this.http.get(`${this.usersUrl}/search/findCurrentUser`)
      .map(resp => resp.json() as User)
  }

  logout(callback): void {
    this.http.post('/api/logout', '').subscribe(
      () => {
        // to get a new csrf token call the api
        this.http.options('api').subscribe(callback);
      }
    );
  }

  signUp(user: User): Observable<User> {
    return this.http.post('api/account/registration', user)
      .map(resp => resp.json() as User);
  }
}
