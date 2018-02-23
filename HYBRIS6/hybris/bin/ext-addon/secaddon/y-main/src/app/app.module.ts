import { BrowserModule } from '@angular/platform-browser';
import { NgModule} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

import { MyAccountModule } from './my-account/my-account.module';

import { AppComponent } from './app.component';
import { AddSupportTicketComponent } from './my-account/support-tickets/add-support-ticket/add-support-ticket.component';
import { ListSupportTicketsComponent } from './my-account/support-tickets/list-support-tickets/list-support-tickets.component';
import { SupportTicketDetailsComponent } from './my-account/support-tickets/support-ticket-details/support-ticket-details.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MyAccountModule,
    RouterModule.forRoot([
      {
        path: 'my-account/add-support-ticket',
        component: AddSupportTicketComponent
      },
      {
        path: 'my-account/support-tickets',
        component: ListSupportTicketsComponent
      },
      {
        path: 'my-account/support-ticket/:id',
        component: SupportTicketDetailsComponent
      }
    ])
  ],
  declarations: [
    AppComponent
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
