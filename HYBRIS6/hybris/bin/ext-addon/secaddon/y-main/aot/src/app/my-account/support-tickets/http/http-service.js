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
import { Http, Headers, } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
var HttpService = (function () {
    function HttpService(http) {
        this.http = http;
    }
    HttpService.prototype.get = function (url, options) {
        this.addXRequestTokenHeader(options);
        return this.http.get(url, options).catch(this.handleError(this));
    };
    HttpService.prototype.post = function (url, body, options) {
        var updatedOptions = this.updateHeaders(options);
        return this.http.post(url, body, updatedOptions).catch(this.handleError(this));
    };
    HttpService.prototype.put = function (url, body, options) {
        var updatedOptions = this.updateHeaders(options);
        return this.http.put(url, body, updatedOptions).catch(this.handleError(this));
    };
    HttpService.prototype.handleError = function (self) {
        return function (res) {
            return Observable.throw(res.json());
        };
    };
    HttpService.prototype.updateHeaders = function (options) {
        if (!options) {
            options = { headers: new Headers() };
        }
        this.addCsrfTokenHeader(options);
        this.addXRequestTokenHeader(options);
        return options;
    };
    HttpService.prototype.addCsrfTokenHeader = function (options) {
        var token = ACC.config.CSRFToken;
        options.headers.set('CSRFToken', "" + token);
    };
    HttpService.prototype.addXRequestTokenHeader = function (options) {
        options.headers.set('X-Requested-With', 'XMLHttpRequest');
    };
    return HttpService;
}());
HttpService = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [Http])
], HttpService);
export { HttpService };
//# sourceMappingURL=http-service.js.map