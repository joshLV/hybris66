var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { MyAccountModule } from './my-account/my-account.module';
import { AppComponent } from './app.component';
import { AddSupportTicketComponent } from './my-account/support-tickets/add-support-ticket/add-support-ticket.component';
import { ListSupportTicketsComponent } from './my-account/support-tickets/list-support-tickets/list-support-tickets.component';
import { SupportTicketDetailsComponent } from './my-account/support-tickets/support-ticket-details/support-ticket-details.component';
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    NgModule({
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
        bootstrap: [AppComponent]
    })
], AppModule);
export { AppModule };
//# sourceMappingURL=app.module.js.map