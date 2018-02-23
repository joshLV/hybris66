var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AddSupportTicketComponent } from './support-tickets/add-support-ticket/add-support-ticket.component';
import { ListSupportTicketsComponent } from './support-tickets/list-support-tickets/list-support-tickets.component';
import { PaginationAndSortComponent } from './support-tickets/pagintion-and-sorting/pagintion-and-sorting.component';
import { SupportTicketsService } from './support-tickets/service/support-tickets.service';
import { HttpService } from './support-tickets/http/http-service';
import { TicketUtils } from './support-tickets/utils/ticket-utils';
import { SupportTicketDetailsComponent } from './support-tickets/support-ticket-details/support-ticket-details.component';
import { PaginationAndSortService } from "./support-tickets/service/pagination-and-sort.service";
import { AddTicketMessageComponent } from "./support-tickets/add-ticket-message/add-ticket-message.component";
var MyAccountModule = (function () {
    function MyAccountModule() {
    }
    return MyAccountModule;
}());
MyAccountModule = __decorate([
    NgModule({
        imports: [
            CommonModule,
            FormsModule
        ],
        declarations: [
            AddSupportTicketComponent,
            ListSupportTicketsComponent,
            AddTicketMessageComponent,
            SupportTicketDetailsComponent,
            PaginationAndSortComponent
        ],
        exports: [
            AddSupportTicketComponent,
            ListSupportTicketsComponent,
            AddTicketMessageComponent,
            SupportTicketDetailsComponent,
            PaginationAndSortComponent
        ],
        providers: [
            PaginationAndSortService,
            SupportTicketsService,
            HttpService,
            TicketUtils
        ]
    })
], MyAccountModule);
export { MyAccountModule };
//# sourceMappingURL=my-account.module.js.map