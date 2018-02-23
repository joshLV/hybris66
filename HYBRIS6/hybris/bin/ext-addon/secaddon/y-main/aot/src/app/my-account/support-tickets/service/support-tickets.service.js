var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable } from '@angular/core';
import { Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import { TicketType } from '../models/ticketType';
import { HttpService } from '../http/http-service';
import { TicketUtils } from '../utils/ticket-utils';
var SupportTicketsService = (function () {
    function SupportTicketsService(httpService, ticketUtils) {
        this.httpService = httpService;
        this.ticketUtils = ticketUtils;
    }
    SupportTicketsService.prototype.getTicketTypes = function () {
        var _this = this;
        return this.httpService.get('my-account/json/support-ticket/ticketTypes', this.prepareHeaders()).map(function (res) {
            var convertedTypes = [];
            var language = _this.ticketUtils.getCurrentLanguage();
            for (var _i = 0, _a = res.json(); _i < _a.length; _i++) {
                var ticketType = _a[_i];
                convertedTypes.push(new TicketType(ticketType.active, ticketType.description[language], ticketType.type));
            }
            return convertedTypes;
        });
    };
    SupportTicketsService.prototype.createTicket = function (ticket) {
        return this.httpService.post('my-account/json/support-ticket/add-support-ticket', ticket, this.prepareHeaders());
    };
    SupportTicketsService.prototype.getTickets = function (pageData) {
        return this.httpService.get("my-account/json/support-ticket" +
            "?sort=" + pageData.sort +
            "&pageNumber=" + pageData.pageNumber +
            "&pageSize=" + pageData.pageSize, this.prepareHeaders())
            .map(function (res) { return res; }).share();
    };
    SupportTicketsService.prototype.getTicket = function (id) {
        return this.httpService.get('my-account/json/support-ticket/' + id, this.prepareHeaders())
            .map(function (res) { return res; });
    };
    SupportTicketsService.prototype.addMessage = function (id, transcript) {
        return this.httpService.post('my-account/json/support-ticket/' + id + '/conversations', transcript, this.prepareHeaders())
            .map(function (res) { return res; });
    };
    SupportTicketsService.prototype.prepareHeaders = function () {
        var headers = new Headers({ 'Content-Type': 'application/json', 'hybris-languages': null });
        return new RequestOptions({ headers: headers });
    };
    return SupportTicketsService;
}());
SupportTicketsService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [HttpService, TicketUtils])
], SupportTicketsService);
export { SupportTicketsService };
//# sourceMappingURL=support-tickets.service.js.map