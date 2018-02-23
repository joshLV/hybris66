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
import { Ticket } from '../models/ticket';
import { Router } from '@angular/router';
var AddSupportTicketComponent = (function () {
    function AddSupportTicketComponent(supportTicketsService, router) {
        this.supportTicketsService = supportTicketsService;
        this.router = router;
        this.ticket = new Ticket('', 'MEDIUM', 'OPEN', null, null, null, null, null, null, null);
        this.isCreateInProgress = false;
    }
    AddSupportTicketComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.supportTicketsService.getTicketTypes().subscribe(function (data) {
            _this.ticketTypes = data;
            _this.ticket.type = _this.ticketTypes[0].type;
        });
    };
    AddSupportTicketComponent.prototype.createTicket = function () {
        var _this = this;
        if (this.canCreateTicket()) {
            this.isCreateInProgress = true;
            this.supportTicketsService.createTicket(this.ticket)
                .subscribe(function () { _this.router.navigate(['/my-account/support-tickets']); _this.isCreateInProgress = false; }, function (err) { _this.showError(err); });
        }
    };
    AddSupportTicketComponent.prototype.showError = function (error) {
        this.error = error;
    };
    AddSupportTicketComponent.prototype.canCreateTicket = function () {
        return !this.isBlank(this.ticket.shortDescription) && !this.isCreateInProgress;
    };
    AddSupportTicketComponent.prototype.isBlank = function (str) {
        return (!str || /^\s*$/.test(str));
    };
    AddSupportTicketComponent.prototype.goBack = function () {
        this.router.navigate(['/my-account/support-tickets']);
    };
    return AddSupportTicketComponent;
}());
AddSupportTicketComponent = __decorate([
    Component({
        selector: 'y-main',
        templateUrl: './add-support-ticket.component.html',
        styleUrls: ['./add-support-ticket.component.css'],
        providers: [SupportTicketsService]
    }),
    __metadata("design:paramtypes", [SupportTicketsService, Router])
], AddSupportTicketComponent);
export { AddSupportTicketComponent };
//# sourceMappingURL=add-support-ticket.component.js.map