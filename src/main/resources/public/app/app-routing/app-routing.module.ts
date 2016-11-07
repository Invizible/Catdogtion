import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "../home/home.component";
import { SignInComponent } from "../authentication/sign-in/sign-in.component";
import { SignUpComponent } from "../authentication/sign-up/sign-up.component";
import { LotDetailsComponent } from '../lot-details/lot-details.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'sign-in',
    component: SignInComponent
  },
  {
    path: 'sign-up',
    component: SignUpComponent
  },
  {
    path: 'lot-details/:id',
    component: LotDetailsComponent
  },
  {
    path: 'my-lots',
    component: HomeComponent,
    data: [{ forCurrentUser: true }]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
