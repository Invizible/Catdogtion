import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CollapseDirective } from 'ng2-bootstrap';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from "./app-routing/app-routing.module";
import { AuthenticationModule } from "./authentication/authentication.module";
import { UserService } from './user.service';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CollapseDirective,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    AuthenticationModule
  ],
  providers: [
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
