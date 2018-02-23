/**
 * @fileoverview This file is generated by the Angular template compiler.
 * Do not edit.
 * @suppress {suspiciousCode,uselessCode,missingProperties,missingOverride}
 */
/* tslint:disable */
import * as import0 from '@angular/core';
import * as import1 from '@angular/common';
import * as import2 from '../../../../../../src/app/my-account/support-tickets/list-support-tickets/list-support-tickets.component';
import * as import3 from '../pagintion-and-sorting/pagintion-and-sorting.component.ngfactory';
import * as import4 from '../../../../../../src/app/my-account/support-tickets/pagintion-and-sorting/pagintion-and-sorting.component';
import * as import5 from '../../../../../../src/app/my-account/support-tickets/service/pagination-and-sort.service';
import * as import6 from '../../../../../../src/app/my-account/support-tickets/service/support-tickets.service';
import * as import7 from '../../../../../../src/app/my-account/support-tickets/http/http-service';
import * as import8 from '../../../../../../src/app/my-account/support-tickets/utils/ticket-utils';
import * as import9 from '@angular/router';
var styles_ListSupportTicketsComponent = [];
export var RenderType_ListSupportTicketsComponent = import0.ɵcrt({
    encapsulation: 2,
    styles: styles_ListSupportTicketsComponent,
    data: {}
});
function View_ListSupportTicketsComponent_1(l) {
    return import0.ɵvid(0, [
        (l()(), import0.ɵeld(0, null, null, 4, 'div', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Loading...'])),
        (l()(), import0.ɵted(null, ['\n    ']))
    ], null, null);
}
function View_ListSupportTicketsComponent_3(l) {
    return import0.ɵvid(0, [
        (l()(), import0.ɵeld(0, null, null, 48, 'tr', [[
                'class',
                'responsive-table-item'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'td', [[
                'class',
                'hidden-sm hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n          '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Ticket ID'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 2, 'td', [], null, [[
                null,
                'click'
            ]
        ], function (v, en, $event) {
            var ad = true;
            var co = v.component;
            if (('click' === en)) {
                var pd_0 = (co.goToDetails(v.context.$implicit.id) !== false);
                ad = (pd_0 && ad);
            }
            return ad;
        }, null, null)),
        (l()(), import0.ɵeld(0, null, null, 1, 'a', [[
                'class',
                'responsive-table-link'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, [
            '',
            ''
        ])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'td', [[
                'class',
                'hidden-sm hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n          '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Subject'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 2, 'td', [[
                'class',
                'customer-ticketing-table-subject'
            ]
        ], null, [[
                null,
                'click'
            ]
        ], function (v, en, $event) {
            var ad = true;
            var co = v.component;
            if (('click' === en)) {
                var pd_0 = (co.goToDetails(v.context.$implicit.id) !== false);
                ad = (pd_0 && ad);
            }
            return ad;
        }, null, null)),
        (l()(), import0.ɵeld(0, null, null, 1, 'a', [[
                'class',
                'responsive-table-link'
            ]
        ], [[
                8,
                'title',
                0
            ]
        ], null, null, null, null)),
        (l()(), import0.ɵted(null, [
            '',
            ''
        ])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'td', [[
                'class',
                'hidden-sm hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n          '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Date Created'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, 'td', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, [
            '',
            ''
        ])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'td', [[
                'class',
                'hidden-sm hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n          '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Date Updated'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, 'td', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, [
            '',
            ''
        ])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'td', [[
                'class',
                'hidden-sm hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n          '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Status'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, 'td', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, [
            '',
            ''
        ])),
        (l()(), import0.ɵted(null, ['\n      ']))
    ], null, function (ck, v) {
        var co = v.component;
        var currVal_0 = v.context.$implicit.ticketNumber;
        ck(v, 10, 0, currVal_0);
        var currVal_1 = import0.ɵinlineInterpolate(1, '', v.context.$implicit.shortDescription, '');
        ck(v, 19, 0, currVal_1);
        var currVal_2 = v.context.$implicit.shortDescription;
        ck(v, 20, 0, currVal_2);
        var currVal_3 = co.formatDate(v.context.$implicit.metadata.createdAt);
        ck(v, 29, 0, currVal_3);
        var currVal_4 = co.formatDate(v.context.$implicit.metadata.modifiedAt);
        ck(v, 38, 0, currVal_4);
        var currVal_5 = co.getStatus(v.context.$implicit);
        ck(v, 47, 0, currVal_5);
    });
}
function View_ListSupportTicketsComponent_2(l) {
    return import0.ɵvid(0, [
        (l()(), import0.ɵeld(0, null, null, 4, 'tbody', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵand(16777216, null, null, 1, null, View_ListSupportTicketsComponent_3)),
        import0.ɵdid(802816, null, 0, import1.NgForOf, [
            import0.ViewContainerRef,
            import0.TemplateRef,
            import0.IterableDiffers
        ], { ngForOf: [
                0,
                'ngForOf'
            ]
        }, null),
        (l()(), import0.ɵted(null, ['\n    ']))
    ], function (ck, v) {
        var co = v.component;
        var currVal_0 = co.tickets;
        ck(v, 3, 0, currVal_0);
    }, null);
}
export function View_ListSupportTicketsComponent_0(l) {
    return import0.ɵvid(0, [
        (l()(), import0.ɵeld(0, null, null, 13, 'div', [[
                'class',
                'account-section-header ${empty supportTickets ? \'\':\'no-border\'}'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n  '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Support Tickets'])),
        (l()(), import0.ɵted(null, ['\n\n\n  '])),
        (l()(), import0.ɵeld(0, null, null, 7, 'div', [[
                'class',
                'account-section-header-add pull-right'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'a', [[
                'class',
                'add-address'
            ]
        ], null, [[
                null,
                'click'
            ]
        ], function (v, en, $event) {
            var ad = true;
            var co = v.component;
            if (('click' === en)) {
                var pd_0 = (co.addNewTicket() !== false);
                ad = (pd_0 && ad);
            }
            return ad;
        }, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Add new'])),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵted(null, ['\n  '])),
        (l()(), import0.ɵted(null, ['\n'])),
        (l()(), import0.ɵted(null, ['\n'])),
        (l()(), import0.ɵeld(0, null, null, 0, 'div', [[
                'class',
                'clearfix visible-md-block visible-lg-block'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n\n\n'])),
        (l()(), import0.ɵeld(0, null, null, 66, 'div', [[
                'class',
                'customer-ticketing account-overview-table'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n\n\n  '])),
        (l()(), import0.ɵeld(0, null, null, 7, 'div', [[
                'class',
                'pagination-bar top'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'div', [[
                'class',
                'pagination-toolbar'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 1, 'sort-page-tickets', [], null, null, null, import3.View_PaginationAndSortComponent_0, import3.RenderType_PaginationAndSortComponent)),
        import0.ɵdid(114688, null, 0, import4.PaginationAndSortComponent, [import5.PaginationAndSortService], null, null),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵted(null, ['\n  '])),
        (l()(), import0.ɵted(null, ['\n\n  '])),
        (l()(), import0.ɵeld(0, null, null, 43, 'table', [[
                'class',
                'responsive-table'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵeld(0, null, null, 34, 'thead', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵeld(0, null, null, 31, 'tr', [[
                'class',
                'responsive-table-head hidden-xs'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'th', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Ticket ID'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'th', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Subject'])),
        (l()(), import0.ɵted(null, ['\n       '])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'th', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Date Created'])),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'th', [], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Date Updated'])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 4, 'th', [[
                'class',
                'supportTicketsTableState'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n        '])),
        (l()(), import0.ɵeld(0, null, null, 1, null, null, null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['Status'])),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵand(16777216, null, null, 1, null, View_ListSupportTicketsComponent_1)),
        import0.ɵdid(16384, null, 0, import1.NgIf, [
            import0.ViewContainerRef,
            import0.TemplateRef
        ], { ngIf: [
                0,
                'ngIf'
            ]
        }, null),
        (l()(), import0.ɵted(null, ['\n\n    '])),
        (l()(), import0.ɵand(16777216, null, null, 1, null, View_ListSupportTicketsComponent_2)),
        import0.ɵdid(16384, null, 0, import1.NgIf, [
            import0.ViewContainerRef,
            import0.TemplateRef
        ], { ngIf: [
                0,
                'ngIf'
            ]
        }, null),
        (l()(), import0.ɵted(null, ['\n  '])),
        (l()(), import0.ɵted(null, ['\n\n  '])),
        (l()(), import0.ɵeld(0, null, null, 9, 'div', [[
                'class',
                'pagination-bar bottom'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵeld(0, null, null, 6, 'div', [[
                'class',
                'pagination-toolbar'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 0, 'div', [[
                'class',
                'helper clearfix hidden-md hidden-lg'
            ]
        ], null, null, null, null, null)),
        (l()(), import0.ɵted(null, ['\n      '])),
        (l()(), import0.ɵeld(0, null, null, 1, 'sort-page-tickets', [], null, null, null, import3.View_PaginationAndSortComponent_0, import3.RenderType_PaginationAndSortComponent)),
        import0.ɵdid(114688, null, 0, import4.PaginationAndSortComponent, [import5.PaginationAndSortService], null, null),
        (l()(), import0.ɵted(null, ['\n    '])),
        (l()(), import0.ɵted(null, ['\n  '])),
        (l()(), import0.ɵted(null, ['\n\n'])),
        (l()(), import0.ɵted(null, ['\n\n\n']))
    ], function (ck, v) {
        var co = v.component;
        ck(v, 24, 0);
        var currVal_0 = !co.tickets;
        ck(v, 67, 0, currVal_0);
        var currVal_1 = co.tickets;
        ck(v, 70, 0, currVal_1);
        ck(v, 80, 0);
    }, null);
}
function View_ListSupportTicketsComponent_Host_0(l) {
    return import0.ɵvid(0, [
        (l()(), import0.ɵeld(0, null, null, 4, 'y-main', [], null, null, null, View_ListSupportTicketsComponent_0, RenderType_ListSupportTicketsComponent)),
        import0.ɵprd(512, null, import6.SupportTicketsService, import6.SupportTicketsService, [
            import7.HttpService,
            import8.TicketUtils
        ]),
        import0.ɵprd(512, null, import5.PaginationAndSortService, import5.PaginationAndSortService, []),
        import0.ɵprd(512, null, import1.DatePipe, import1.DatePipe, [import0.LOCALE_ID]),
        import0.ɵdid(114688, null, 0, import2.ListSupportTicketsComponent, [
            import6.SupportTicketsService,
            import5.PaginationAndSortService,
            import9.Router,
            import1.DatePipe,
            import8.TicketUtils
        ], null, null)
    ], function (ck, v) {
        ck(v, 4, 0);
    }, null);
}
export var ListSupportTicketsComponentNgFactory = import0.ɵccf('y-main', import2.ListSupportTicketsComponent, View_ListSupportTicketsComponent_Host_0, {}, {}, []);
//# sourceMappingURL=list-support-tickets.component.ngfactory.js.map