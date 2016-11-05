import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignInComponent } from "./sign-in/sign-in.component";
import { SignUpComponent } from "./sign-up/sign-up.component";
import { FormsModule } from "@angular/forms";
import { RecaptchaModule } from 'ng2-recaptcha';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RecaptchaModule.forRoot()
  ],
  declarations: [
    SignInComponent,
    SignUpComponent
  ],
  providers: [
  ]
})
export class AuthenticationModule { }
