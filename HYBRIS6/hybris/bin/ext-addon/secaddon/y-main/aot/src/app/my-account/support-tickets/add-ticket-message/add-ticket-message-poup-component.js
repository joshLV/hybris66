var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Injectable, NgZone } from "@angular/core";
var AddTicketMessagePopupComponent = (function () {
    function AddTicketMessagePopupComponent(zone) {
        this.zone = zone;
    }
    AddTicketMessagePopupComponent.prototype.showPopup = function () {
        this.zone.runOutsideAngular(function () {
            $.colorbox({
                href: "#ct-add-new-msg",
                maxWidth: "100%",
                width: 525,
                opacity: 0.7,
                title: '<div class="headline"><span class="headline-text"></span></div>',
                inline: true,
                close: '<span class="glyphicon glyphicon-remove"></span>',
                onOpen: function () {
                    $('#ct-add-new-msg').fadeIn();
                },
                onComplete: function () {
                    $('#cboxTitle span').text($('.ct-add-new-msg-btn').text());
                    $('button[id="updateTicket"]').attr('disabled', 'disabled');
                },
                onCleanup: function () {
                    $('#ct-add-new-msg').hide();
                }
            });
        });
    };
    AddTicketMessagePopupComponent.prototype.closePopup = function () {
        $.colorbox.close();
    };
    return AddTicketMessagePopupComponent;
}());
AddTicketMessagePopupComponent = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [NgZone])
], AddTicketMessagePopupComponent);
export { AddTicketMessagePopupComponent };
//# sourceMappingURL=add-ticket-message-poup-component.js.map