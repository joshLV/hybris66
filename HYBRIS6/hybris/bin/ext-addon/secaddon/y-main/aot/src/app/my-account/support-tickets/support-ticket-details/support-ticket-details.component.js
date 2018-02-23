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
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { SupportTicketsService } from "../service/support-tickets.service";
import { TicketUtils } from '../utils/ticket-utils';
var SupportTicketDetailsComponent = (function () {
    function SupportTicketDetailsComponent(supportTicketsService, route, router, datePipe, ticketUtils) {
        this.supportTicketsService = supportTicketsService;
        this.route = route;
        this.router = router;
        this.datePipe = datePipe;
        this.ticketUtils = ticketUtils;
        this.dateFormat = 'dd-MM-yyyy HH:mm';
    }
    SupportTicketDetailsComponent.prototype.ngOnInit = function () {
        this.getTicket();
    };
    SupportTicketDetailsComponent.prototype.getTicket = function () {
        var _this = this;
        this.route.params.subscribe(function (params) { return _this.supportTicketsService.getTicket(params['id']).subscribe(function (data) {
            _this.ticket = data.json();
            _this.lang = _this.ticketUtils.getCurrentLanguage();
        }); });
    };
    SupportTicketDetailsComponent.prototype.goBack = function () {
        this.router.navigate(['/my-account/support-tickets']);
    };
    SupportTicketDetailsComponent.prototype.formatDate = function (date) {
        return this.datePipe.transform(date, this.dateFormat);
    };
    SupportTicketDetailsComponent.prototype.onMessageAdded = function (ticket) {
        this.ticket = ticket;
    };
    SupportTicketDetailsComponent.prototype.getType = function (ticket) {
        return ticket.typeDescription[this.lang];
    };
    SupportTicketDetailsComponent.prototype.getStatus = function (ticket) {
        return ticket.statusDescription[this.lang];
    };
    SupportTicketDetailsComponent.prototype.getMessageAuthor = function (owner) {
        var author = "Customer Service";
        if (owner.isCustomer) {
            author = owner.displayName;
        }
        return author;
    };
    return SupportTicketDetailsComponent;
}());
SupportTicketDetailsComponent = __decorate([
    Component({
        selector: 'y-main',
        templateUrl: './support-ticket-details.component.html',
        providers: [SupportTicketsService, DatePipe]
    }),
    __metadata("design:paramtypes", [SupportTicketsService, ActivatedRoute, Router, DatePipe, TicketUtils])
], SupportTicketDetailsComponent);
export { SupportTicketDetailsComponent };
//# sourceMappingURL=support-ticket-details.component.js.map