var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component } from '@angular/core';
import { SupportTicketsService } from '../service/support-tickets.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { PaginationAndSortService } from "../service/pagination-and-sort.service";
import { TicketUtils } from '../utils/ticket-utils';
var ListSupportTicketsComponent = (function () {
    function ListSupportTicketsComponent(supportTicketsService, pagingAndSortService, router, datePipe, ticketUtils) {
        this.supportTicketsService = supportTicketsService;
        this.pagingAndSortService = pagingAndSortService;
        this.router = router;
        this.datePipe = datePipe;
        this.ticketUtils = ticketUtils;
        this.dateFormat = 'dd-MM-yyyy HH:mm';
    }
    ListSupportTicketsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.supportTicketsService.getTickets(this.pagingAndSortService.getPagingData()).subscribe(function (data) {
            _this.tickets = data.json();
            _this.lang = _this.ticketUtils.getCurrentLanguage();
            _this.pagingAndSortService.setTotalItems(+data.headers.get('Hybris-Count'));
        });
        this.pagingAndSortService.paginationSource$.subscribe(function () {
            _this.onPaging();
        });
    };
    ListSupportTicketsComponent.prototype.goToDetails = function (ticketId) {
        this.router.navigate(['/my-account/support-ticket', ticketId]);
    };
    ListSupportTicketsComponent.prototype.addNewTicket = function () {
        this.router.navigate(['/my-account/add-support-ticket']);
    };
    ListSupportTicketsComponent.prototype.formatDate = function (date) {
        return this.datePipe.transform(date, this.dateFormat);
    };
    ListSupportTicketsComponent.prototype.getStatus = function (ticket) {
        return ticket.statusDescription[this.lang];
    };
    ListSupportTicketsComponent.prototype.onPaging = function () {
        var _this = this;
        this.supportTicketsService.getTickets(this.pagingAndSortService.getPagingData()).subscribe(function (data) { return _this.tickets = data.json(); });
    };
    return ListSupportTicketsComponent;
}());
ListSupportTicketsComponent = __decorate([
    Component({
        selector: 'y-main',
        templateUrl: './list-support-tickets.component.html',
        providers: [SupportTicketsService, PaginationAndSortService, DatePipe]
    }),
    __metadata("design:paramtypes", [SupportTicketsService, PaginationAndSortService,
        Router, DatePipe, TicketUtils])
], ListSupportTicketsComponent);
export { ListSupportTicketsComponent };
//# sourceMappingURL=list-support-tickets.component.js.map