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
import { PaginationAndSortService } from "../service/pagination-and-sort.service";
var PaginationAndSortComponent = (function () {
    function PaginationAndSortComponent(pagingAndSortService) {
        this.pagingAndSortService = pagingAndSortService;
    }
    PaginationAndSortComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.selectedSort = this.pagingAndSortService.getSortValue();
        this.pagingAndSortService.paginationSource$.subscribe(function () {
            _this.selectedSort = _this.pagingAndSortService.getSortValue();
        });
    };
    PaginationAndSortComponent.prototype.sort = function (selectedSort) {
        this.pagingAndSortService.sort(selectedSort);
    };
    PaginationAndSortComponent.prototype.next = function () {
        this.pagingAndSortService.next();
    };
    PaginationAndSortComponent.prototype.previous = function () {
        this.pagingAndSortService.previous();
    };
    PaginationAndSortComponent.prototype.goToPage = function (number) {
        if (number !== this.getPageNumber()) {
            this.pagingAndSortService.goToPage(number);
        }
    };
    PaginationAndSortComponent.prototype.getTotalPages = function () {
        return this.pagingAndSortService.getTotalPages();
    };
    PaginationAndSortComponent.prototype.getPageNumber = function () {
        return this.pagingAndSortService.getPageNumber();
    };
    PaginationAndSortComponent.prototype.getSorts = function () {
        return this.pagingAndSortService.getSorts();
    };
    PaginationAndSortComponent.prototype.hasPagingData = function () {
        return this.pagingAndSortService.getPagingData();
    };
    PaginationAndSortComponent.prototype.createRange = function (number) {
        var items = [];
        for (var i = 1; i <= number; i++) {
            items.push(i);
        }
        return items;
    };
    return PaginationAndSortComponent;
}());
PaginationAndSortComponent = __decorate([
    Component({
        selector: 'sort-page-tickets',
        templateUrl: './pagintion-and-sorting.component.html'
    }),
    __metadata("design:paramtypes", [PaginationAndSortService])
], PaginationAndSortComponent);
export { PaginationAndSortComponent };
//# sourceMappingURL=pagintion-and-sorting.component.js.map