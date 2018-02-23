var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { Injectable } from "@angular/core";
import { PagingData } from "../models/pagingData";
import { Subject } from "rxjs/Subject";
var DEFAULT_SORT = "metadata.modifiedAt:desc";
var PaginationAndSortService = (function () {
    function PaginationAndSortService() {
        this.pagingData = new PagingData(1, 5, DEFAULT_SORT);
        this.paginationSource = new Subject();
        this.paginationSource$ = this.paginationSource.asObservable();
        this.sortMap = new Map([["DATE UPDATED DESC", "metadata.modifiedAt:desc"],
            ["DATE UPDATED ASC", "metadata.modifiedAt:asc"],
            ["TICKET ID DESC", "id:desc"],
            ["TICKET ID ASC", "id:asc"]]);
    }
    PaginationAndSortService.prototype.getSorts = function () {
        return Array.from(this.sortMap.keys());
    };
    PaginationAndSortService.prototype.next = function () {
        if (this.pagingData.pageNumber < this.totalPages) {
            this.pagingData.pageNumber = this.pagingData.pageNumber + 1;
            this.setPagination(this.pagingData);
        }
    };
    PaginationAndSortService.prototype.previous = function () {
        if (this.pagingData.pageNumber !== 1) {
            this.pagingData.pageNumber = this.pagingData.pageNumber - 1;
            this.setPagination(this.pagingData);
        }
    };
    PaginationAndSortService.prototype.goToPage = function (number) {
        this.pagingData.pageNumber = number;
        this.setPagination(this.pagingData);
    };
    PaginationAndSortService.prototype.sort = function (selectedSort) {
        this.pagingData.sort = this.getSortKey(selectedSort);
        this.setPagination(this.pagingData);
    };
    PaginationAndSortService.prototype.setTotalItems = function (totalItems) {
        this.totalItems = totalItems;
        this.totalPages = Math.ceil(totalItems / this.pagingData.pageSize);
    };
    PaginationAndSortService.prototype.setPagination = function (pageableData) {
        this.pagingData = pageableData;
        this.paginationSource.next(this.pagingData);
    };
    PaginationAndSortService.prototype.getSortKey = function (value) {
        return this.sortMap.has(value)
            ? this.sortMap.get(value)
            : DEFAULT_SORT;
    };
    PaginationAndSortService.prototype.getSortValue = function () {
        var _this = this;
        var val = Array.from(this.sortMap.keys())[0];
        this.sortMap.forEach(function (key, value) {
            if (key === _this.pagingData.sort) {
                val = value;
                return;
            }
        });
        return val;
    };
    PaginationAndSortService.prototype.getPagingData = function () {
        return this.pagingData;
    };
    PaginationAndSortService.prototype.getPageNumber = function () {
        return this.pagingData.pageNumber;
    };
    PaginationAndSortService.prototype.getTotalPages = function () {
        return this.totalPages;
    };
    return PaginationAndSortService;
}());
PaginationAndSortService = __decorate([
    Injectable()
], PaginationAndSortService);
export { PaginationAndSortService };
//# sourceMappingURL=pagination-and-sort.service.js.map