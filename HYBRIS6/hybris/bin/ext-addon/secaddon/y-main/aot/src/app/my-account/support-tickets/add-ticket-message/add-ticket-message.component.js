var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AddTicketMessagePopupComponent } from "./add-ticket-message-poup-component";
import { Ticket } from "../models/ticket";
import { SupportTicketsService } from "../service/support-tickets.service";
import { Transcript } from "../models/transcript";
var AddTicketMessageComponent = (function () {
    function AddTicketMessageComponent(supportTicketsService, updateTicketPopupComponent) {
        this.supportTicketsService = supportTicketsService;
        this.updateTicketPopupComponent = updateTicketPopupComponent;
        this.onMessageAdded = new EventEmitter();
        this.isUpdateInProgress = false;
    }
    AddTicketMessageComponent.prototype.addMessage = function () {
        this.updateTicketPopupComponent.showPopup();
    };
    AddTicketMessageComponent.prototype.addTicketMessage = function () {
        var _this = this;
        if (this.canNotUpdate()) {
            return;
        }
        this.isUpdateInProgress = true;
        var transcript = this.prepareTranscript();
        this.supportTicketsService.addMessage(this.ticket.id, transcript).subscribe(function () {
            _this.refreshTicket();
            _this.updateTicketPopupComponent.closePopup();
            _this.clearForm();
            _this.isUpdateInProgress = false;
        });
    };
    AddTicketMessageComponent.prototype.prepareTranscript = function () {
        return new Transcript(null, this.ticketMessage);
    };
    AddTicketMessageComponent.prototype.canNotUpdate = function () {
        return this.isBlank(this.ticketMessage) || this.isUpdateInProgress;
    };
    AddTicketMessageComponent.prototype.isBlank = function (str) {
        return (!str || /^\s*$/.test(str));
    };
    AddTicketMessageComponent.prototype.refreshTicket = function () {
        var _this = this;
        this.supportTicketsService.getTicket(this.ticket.id).subscribe(function (data) { _this.ticket = data.json(); _this.onMessageAdded.emit(_this.ticket); });
    };
    AddTicketMessageComponent.prototype.clearForm = function () {
        this.ticketMessage = "";
    };
    return AddTicketMessageComponent;
}());
__decorate([
    Input(),
    __metadata("design:type", Ticket)
], AddTicketMessageComponent.prototype, "ticket", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], AddTicketMessageComponent.prototype, "onMessageAdded", void 0);
AddTicketMessageComponent = __decorate([
    Component({
        selector: 'add-ticket-message',
        templateUrl: './add-ticket-message.component.html',
        providers: [SupportTicketsService, AddTicketMessagePopupComponent]
    }),
    __metadata("design:paramtypes", [SupportTicketsService, AddTicketMessagePopupComponent])
], AddTicketMessageComponent);
export { AddTicketMessageComponent };
//# sourceMappingURL=add-ticket-message.component.js.map