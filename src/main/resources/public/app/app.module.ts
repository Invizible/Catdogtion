import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CollapseDirective, ProgressbarModule } from 'ng2-bootstrap';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from "./app-routing/app-routing.module";
import { AuthenticationModule } from "./authentication/authentication.module";
import { AccountService } from './account.service';
import { LotComponent } from './lot/lot.component';
import { LotService } from './lot.service';
import { SearchComponent } from './search/search.component';
import { LotDetailsComponent } from './lot-details/lot-details.component';
import { CarouselModule } from 'ng2-bootstrap/components/carousel';
import { LocationStrategy, HashLocationStrategy } from "@angular/common";
import { LotModalComponent } from './lot-modal/lot-modal.component';
import { ModalModule } from 'ng2-bootstrap/components/modal';
import { UPLOAD_DIRECTIVES } from 'ng2-uploader/ng2-uploader';
import { CookieService } from "angular2-cookie/services/cookies.service";
import { ImageService } from "./image.service";
import { MarkdownToHtmlPipe } from './markdown-to-html.pipe';
import { AuctionService } from './auction.service';
import { StompService } from './stomp.service';
import { AuctionControlsComponent } from './auction-controls/auction-controls.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CollapseDirective,
    HomeComponent,
    LotComponent,
    SearchComponent,
    LotDetailsComponent,
    LotModalComponent,
    UPLOAD_DIRECTIVES,
    MarkdownToHtmlPipe,
    AuctionControlsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    AuthenticationModule,
    CarouselModule,
    ModalModule,
    ProgressbarModule
  ],
  providers: [
    AccountService,
    LotService,
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    CookieService,
    ImageService,
    AuctionService,
    StompService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
