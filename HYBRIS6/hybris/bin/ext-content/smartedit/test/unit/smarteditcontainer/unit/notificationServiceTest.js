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
describe('notificationService', function() {
    var DUMMY_ID = 'dummy.id';
    var DUMMY_ID1 = DUMMY_ID + '1';
    var DUMMY_ID2 = DUMMY_ID + '2';
    var DUMMY_ID3 = DUMMY_ID + '3';
    var DUMMY_INVALID_ID = 'dummy.invalid.id';

    var DUMMY_TEMPLATE = '<div>this is a dummy template</div>';
    var DUMMY_TEMPLATE_URL = 'dummyTemplateUrl.html';

    var DUMMY_CONFIGURATION = {
        id: DUMMY_ID,
        template: DUMMY_TEMPLATE
    };

    var notificationService, NotificationServiceInterface, SE_NOTIFICATION_SERVICE_GATEWAY_ID, EVENT_NOTIFICATION_CHANGED, gatewayProxy, systemEventService;

    /*
     * This setup method creates a mock for the system event service. It is used to
     * validate that the notification service triggers the EVENT_NOTIFICATION_CHANGED
     * event when an notification is pushed.
     */
    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent']);
        $provide.value('systemEventService', systemEventService);
    }));

    /*
     * This setup method creates a mock for the gateway proxy. It is used to validate
     * that the notification service initializes itself properly to be proxied
     * across the two applications.
     */
    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('notificationServiceModule'));

    beforeEach(inject(function(_notificationService_, _NotificationServiceInterface_, _SE_NOTIFICATION_SERVICE_GATEWAY_ID_, _EVENT_NOTIFICATION_CHANGED_) {
        notificationService = _notificationService_;
        EVENT_NOTIFICATION_CHANGED = _EVENT_NOTIFICATION_CHANGED_;

        NotificationServiceInterface = _NotificationServiceInterface_;
        SE_NOTIFICATION_SERVICE_GATEWAY_ID = _SE_NOTIFICATION_SERVICE_GATEWAY_ID_;
    }));

    describe('initialization', function() {
        it('extends the NotificationServiceInterface', function() {
            expect(notificationService instanceof NotificationServiceInterface).toBe(true);
        });

        it('invokes the gatway proxy with the proper parameter values', function() {
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(
                notificationService, ['pushNotification', 'removeNotification', 'removeAllNotifications'],
                SE_NOTIFICATION_SERVICE_GATEWAY_ID);
        });
    });

    describe('pushNotification', function() {
        it('throws an error if no configuration is given', function() {
            expect(function() {
                notificationService.pushNotification(null);
            }).toThrow('notificationService.pushNotification: Configuration is required');
        });

        it('throws an error if the configuration does not contain a unique identifier', function() {
            expect(function() {
                notificationService.pushNotification({
                    template: DUMMY_TEMPLATE
                });
            }).toThrow('notificationService.pushNotification: Configuration must contain an ID');
        });

        it('throws an error if the configuration contains a unique identifier that is an empty string', function() {
            expect(function() {
                notificationService.pushNotification({
                    id: '',
                    template: DUMMY_TEMPLATE
                });
            }).toThrow('notificationService.pushNotification: Notification ID cannot be an empty string');
        });

        it('throws an error if the configuration does not contain a template or template URL', function() {
            expect(function() {
                notificationService.pushNotification({
                    id: DUMMY_ID
                });
            }).toThrow('notificationService.pushNotification: Configuration must contain a template or template URL');
        });

        it('throws an error if the configuration contains both a template and a template URL', function() {
            expect(function() {
                notificationService.pushNotification({
                    id: DUMMY_ID,
                    template: DUMMY_TEMPLATE,
                    templateUrl: DUMMY_TEMPLATE_URL
                });
            }).toThrow('notificationService.pushNotification: Configuration cannot contain both a template and template URL; use one or the other');
        });

        it('throws an error when the configuration contains a unique identifier that already exists', function() {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // When/Then
            expect(function() {
                notificationService.pushNotification(DUMMY_CONFIGURATION);
            }).toThrow('notificationService.pushNotification: Notification already exists with ID "' + DUMMY_ID + '"');
        });

        it('creates an notification with the proper ID and template', function() {
            // Given
            var configuration = {
                id: DUMMY_ID,
                template: DUMMY_TEMPLATE
            };

            // When
            notificationService.pushNotification(configuration);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toEqual(jasmine.objectContaining(configuration));
        });

        it('creates an notification with the proper ID and template URL', function() {
            // Given
            var configuration = {
                id: DUMMY_ID,
                template: DUMMY_TEMPLATE_URL
            };

            // When
            notificationService.pushNotification(configuration);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toEqual(jasmine.objectContaining(configuration));
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when an notification is added to the list', function() {
            // When
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // Then
            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_NOTIFICATION_CHANGED);
        });
    });

    describe('removeNotification', function() {
        it('removes the notification with the given ID from the list', function() {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // When
            notificationService.removeNotification(DUMMY_ID);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toBeFalsy();
        });

        it('removes nothing when no notification with the given ID exists', function() {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            // When
            notificationService.removeNotification(DUMMY_INVALID_ID);

            // Then
            expect(notificationService.getNotifications().length).toEqual(2);
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when an notification is removed from the list', function() {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID,
                template: DUMMY_TEMPLATE
            });

            systemEventService.sendAsynchEvent.calls.reset();

            // When
            notificationService.removeNotification(DUMMY_ID);

            // Then
            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_NOTIFICATION_CHANGED);
        });
    });

    describe('removeAllNotifications', function() {
        it('removes all the notificationes from the list', function() {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE
            });

            // When
            notificationService.removeAllNotifications();

            // Then
            expect(notificationService.getNotification(DUMMY_ID1)).toBeFalsy();
            expect(notificationService.getNotification(DUMMY_ID2)).toBeFalsy();
            expect(notificationService.getNotification(DUMMY_ID3)).toBeFalsy();
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when the notificationes are removed from the list', function() {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE
            });

            systemEventService.sendAsynchEvent.calls.reset();

            // When
            notificationService.removeAllNotifications();

            // Then
            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_NOTIFICATION_CHANGED);
        });
    });

    describe('getNotification', function() {
        it('returns the notification with the given ID', function() {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // When
            var result = notificationService.getNotification(DUMMY_ID);

            // Then
            expect(result).toEqual(jasmine.objectContaining(DUMMY_CONFIGURATION));
        });
    });

    describe('getNotifications', function() {
        it('returns the list of notificationes', function() {
            // Given
            var configuration1 = {
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE,
            };

            var configuration2 = {
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE,
            };

            var configuration3 = {
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE,
            };

            notificationService.pushNotification(configuration1);
            notificationService.pushNotification(configuration2);
            notificationService.pushNotification(configuration3);

            // When
            var notifications = notificationService.getNotifications();

            // Then
            /*
             * NOTE: The notifications in the list should be in reverse order, since we return the
             * reversed list.
             */
            expect(notifications.length).toBe(3);
            expect(notifications[0]).toEqual(jasmine.objectContaining(configuration3));
            expect(notifications[1]).toEqual(jasmine.objectContaining(configuration2));
            expect(notifications[2]).toEqual(jasmine.objectContaining(configuration1));
        });
    });
});
