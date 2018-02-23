/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
describe('notificationServiceInterface', function() {
    var NotificationServiceInterface;

    beforeEach(module('notificationServiceInterfaceModule'));

    beforeEach(inject(function(_NotificationServiceInterface_) {
        NotificationServiceInterface = _NotificationServiceInterface_;
    }));

    describe('initialization', function() {
        it('registers empty methods', function() {
            expect(NotificationServiceInterface.prototype.pushNotification).toBeEmptyFunction();
            expect(NotificationServiceInterface.prototype.removeNotification).toBeEmptyFunction();
            expect(NotificationServiceInterface.prototype.removeAllNotifications).toBeEmptyFunction();
        });
    });
});
