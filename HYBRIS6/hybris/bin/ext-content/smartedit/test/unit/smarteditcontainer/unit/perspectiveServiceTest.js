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
describe('outer perspectiveService', function() {

    var $log, $q, $rootScope, storageService, gatewayProxy, systemEventService, Perspective, PerspectiveServiceInterface, perspectiveService, featureService, crossFrameEventService, iFrameManager, NONE_PERSPECTIVE, ALL_PERSPECTIVE, EVENT_PERSPECTIVE_CHANGED, EVENT_PERSPECTIVE_ADDED, EVENTS, permissionService;

    beforeEach(module('eventServiceModule', function($provide) {

        EVENTS = {
            LOGOUT: 'logout',
            EXPERIENCE_UPDATE: 'clear'
        };
        $provide.value('EVENTS', EVENTS);

        NONE_PERSPECTIVE = 'se.none';
        $provide.value('NONE_PERSPECTIVE', NONE_PERSPECTIVE);

        ALL_PERSPECTIVE = 'se.all';
        $provide.value('ALL_PERSPECTIVE', ALL_PERSPECTIVE);
    }));

    beforeEach(module('perspectiveServiceModule', function($provide) {

        iFrameManager = jasmine.createSpyObj('iFrameManager', ['showWaitModal', 'hideWaitModal']);
        $provide.value('iFrameManager', iFrameManager);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        featureService = jasmine.createSpyObj('featureService', ['enable', 'disable', 'getFeatureProperty']);
        $provide.value('featureService', featureService);

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler']);
        $provide.value('systemEventService', systemEventService);

        storageService = jasmine.createSpyObj('storageService', ['getValueFromCookie', 'putValueInCookie']);
        $provide.value('storageService', storageService);

        EVENT_PERSPECTIVE_CHANGED = 'EVENT_PERSPECTIVE_CHANGED';
        $provide.value('EVENT_PERSPECTIVE_CHANGED', EVENT_PERSPECTIVE_CHANGED);

        EVENT_PERSPECTIVE_ADDED = 'EVENT_PERSPECTIVE_ADDED';
        $provide.value('EVENT_PERSPECTIVE_ADDED', EVENT_PERSPECTIVE_ADDED);

        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);
        $provide.value('crossFrameEventService', crossFrameEventService);

        permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);
        $provide.value('permissionService', permissionService);
    }));

    beforeEach(inject(function(_$log_, _$q_, _$rootScope_, _Perspective_, _PerspectiveServiceInterface_, _perspectiveService_, _featureService_, _EVENTS_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        $log = _$log_;
        Perspective = _Perspective_;
        perspectiveService = _perspectiveService_;
        PerspectiveServiceInterface = _PerspectiveServiceInterface_;
        featureService = _featureService_;
        EVENTS = _EVENTS_;
    }));

    /*
     * Default returns values for:
     * featureService
     * permissionService
     */
    beforeEach(function() {
        featureService.getFeatureProperty.and.returnValue($q.when([]));
        permissionService.isPermitted.and.returnValue($q.when(true));
    });

    it('extends PerspectiveServiceInterface', function() {
        expect(perspectiveService instanceof PerspectiveServiceInterface).toBe(true);
    });

    describe('initialization', function() {

        it('initializes and invokes gatewayProxy', function() {
            expect(perspectiveService.gatewayId).toBe("perspectiveService");
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(perspectiveService, ['register', 'switchTo', 'hasActivePerspective', 'isEmptyPerspectiveActive', 'selectDefault', 'refreshPerspective']);
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.LOGOUT, jasmine.any(Function));
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.EXPERIENCE_UPDATE, jasmine.any(Function));
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.AUTHORIZATION_SUCCESS, jasmine.any(Function));
        });

        it('_registerEventHandlers will register event handlers', function() {
            // GIVEN
            spyOn(perspectiveService, '_registerEventHandlers').and.callThrough();
            spyOn(perspectiveService, '_clearPerspectiveFeatures').and.callFake(function() {});
            spyOn(perspectiveService, '_onLogoutPerspectiveCleanup').and.callFake(function() {});
            systemEventService.registerEventHandler.calls.reset();

            // WHEN
            perspectiveService._registerEventHandlers();

            // THEN
            expect(systemEventService.registerEventHandler.calls.count()).toBe(3);
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.LOGOUT, jasmine.any(Function));
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.EXPERIENCE_UPDATE, jasmine.any(Function));
            expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.AUTHORIZATION_SUCCESS, jasmine.any(Function));

            var clearCallback = systemEventService.registerEventHandler.calls.argsFor(0)[1];
            clearCallback();
            expect(perspectiveService._clearPerspectiveFeatures).toHaveBeenCalled();

            var logoutCallback = systemEventService.registerEventHandler.calls.argsFor(1)[1];
            logoutCallback();
            expect(perspectiveService._onLogoutPerspectiveCleanup).toHaveBeenCalled();
        });

        it('GIVEN perspectives are initialized', function() {
            // WHEN
            var perspectives = getPerspectives();
            var nonePerspective = findPerspective(perspectives, NONE_PERSPECTIVE);
            var allPerspective = findPerspective(perspectives, ALL_PERSPECTIVE);

            // THEN
            expect(perspectives.length).toBe(2);
            expect(nonePerspective.name).toBe('se.perspective.none.name');
            expect(allPerspective.name).toBe('se.perspective.all.name');
        });

    });

    describe('register', function() {
        it('throws error if key is not provided', function() {
            expect(function() {
                perspectiveService.register({
                    nameI18nKey: 'somenameI18nKey',
                    features: ['abc', 'xyz']
                });
            }).toThrowError("perspectiveService.configuration.key.error.required");
        });

        it('throws error if nameI18nKey is not provided', function() {
            expect(function() {
                perspectiveService.register({
                    key: 'somekey',
                    features: ['abc', 'xyz']
                });
            }).toThrowError("perspectiveService.configuration.nameI18nKey.error.required");
        });

        it('throws error if features is not provided and perspective is neither se.none nor se.all', function() {
            expect(function() {
                perspectiveService.register({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey'
                });
            }).toThrowError("perspectiveService.configuration.features.error.required");
        });

        it('throws error is features is empty', function() {
            expect(function() {
                perspectiveService.register({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey',
                    features: []
                });
            }).toThrowError("perspectiveService.configuration.features.error.required");
        });

        it('is successful when perspective key is ' + NONE_PERSPECTIVE + ' and features are not provided', function() {
            // GIVEN
            var configuration = {
                key: NONE_PERSPECTIVE,
                nameI18nKey: 'somenameI18nKey'
            };

            // WHEN
            perspectiveService.register(configuration);
            var perspective = getPerspective(configuration.key);

            // THEN
            expect(perspective).toBeDefined();
        });

        it('is successful when perspective key is ' + ALL_PERSPECTIVE + ' and features are not provided', function() {
            // GIVEN
            var configuration = {
                key: ALL_PERSPECTIVE,
                nameI18nKey: 'somenameI18nKey'
            };

            // WHEN
            perspectiveService.register(configuration);

            var perspective = getPerspective(configuration.key);

            // THEN
            expect(perspective).toBeTruthy();
        });

        it('GIVEN that perspective configuration has features, THEN register pushes to the features list a Perspective instantiated from configuration and sends a notification', function() {
            // GIVEN
            perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2']
            });

            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);

            // WHEN
            var perspectives = getPerspectives();

            // THEN Expect to have 2 default + 1 registered
            expect(perspectives.length).toBe(3);
            var perspective = perspectives[2];

            expect(perspective instanceof Perspective).toBe(true);

            expect(perspective).toEqual(jasmine.objectContaining({
                key: 'somekey',
                name: 'somenameI18nKey',
                system: true,
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2'],
                descriptionI18nKey: 'somedescriptionI18nKey'
            }));
        });

        it('does not override existing perspectives but merges features and nested perspectives', function() {
            // GIVEN
            perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2']
            });

            perspectiveService.register({
                key: 'somekey3',
                nameI18nKey: 'somenameI18nKey3',
                descriptionI18nKey: 'somedescriptionI18nKey3',
                features: ['zzz'],
                perspectives: ['xxx']
            });

            perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey2',
                descriptionI18nKey: 'somedescriptionI18nKey2',
                features: ['xyz', 'def'],
                perspectives: ['persp2', 'persp3']
            });

            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);

            // WHEN
            var perspectives = getPerspectives();
            var res1 = findPerspective(perspectives, 'somekey');
            var res2 = findPerspective(perspectives, 'somekey3');

            // THEN
            expect(res1).toEqual(jasmine.objectContaining({
                key: 'somekey',
                name: 'somenameI18nKey',
                system: true,
                features: ['abc', 'xyz', 'def'],
                perspectives: ['persp1', 'persp2', 'persp3'],
                descriptionI18nKey: 'somedescriptionI18nKey'
            }));

            expect(res2).toEqual(jasmine.objectContaining({
                key: 'somekey3',
                name: 'somenameI18nKey3',
                system: true,
                features: ['zzz'],
                perspectives: ['xxx'],
                descriptionI18nKey: 'somedescriptionI18nKey3'
            }));

        });

        it('adds a perspective with its permissions and sends an ' + EVENT_PERSPECTIVE_ADDED + ' event', function() {
            // GIVEN
            var configuration = {
                key: 'perspectiveKey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1'],
                permissions: ['permission1', 'permission2']
            };

            permissionService.isPermitted.and.returnValue($q.when(true));

            // WHEN
            perspectiveService.register(configuration);

            var perspective = getPerspective(configuration.key);

            // THEN
            expect(perspective.permissions).toEqual(configuration.permissions);
            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);
        });

        it('does not override existing perspectives but merges permissions and sends an ' + EVENT_PERSPECTIVE_ADDED + ' event', function() {
            // GIVEN
            var configuration1 = {
                key: 'perspectiveKey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1'],
                permissions: ['permission1', 'permission2']
            };

            var configuration2 = {
                key: 'perspectiveKey',
                nameI18nKey: 'somenameI18nKey2',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1'],
                permissions: ['permission2', 'permission3']
            };

            permissionService.isPermitted.and.returnValue($q.when(true));

            // WHEN
            perspectiveService.register(configuration1);
            perspectiveService.register(configuration2);
            var perspective = getPerspective(configuration1.key);

            // THEN
            expect(perspective.permissions).toEqual(['permission1', 'permission2', 'permission3']);
            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);
        });
    });

    describe('_fetchAllFeatures', function() {
        it('collects all features of nested perspectives, returns a set of unique features, and log message if a nested perspective does not exist', function() {
            //GIVEN
            spyOn($log, 'debug').and.returnValue();

            var perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            var perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            var perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: []
            };
            perspectiveService.register(perspective1);
            perspectiveService.register(perspective2);
            perspectiveService.register(perspective4);

            var holder = [];

            // WHEN
            perspectiveService._fetchAllFeatures(perspective1, holder);

            // THEN
            expect(holder).toEqual(['feat1', 'feat2', 'feat3', 'feat4']);
            expect($log.debug).toHaveBeenCalledWith('nested perspective persp3 was not found in the registry');
        });
    });

    describe('refreshPerspective', function() {
        it('should select the default perspective if there is no active perspective', function() {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue();
            storageService.getValueFromCookie.and.returnValue($q.when(null));

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            perspectiveService.refreshPerspective();
            $rootScope.$digest();

            //THEN
            expect(storageService.getValueFromCookie).toHaveBeenCalledWith("smartedit-perspectives", true);
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
        });

        it('will publish a perspective refreshed event after a successful refreshPerspective', function() {
            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2']
            };

            perspectiveService.register(perspective0);
            perspectiveService.switchTo(perspective0.key);

            $rootScope.$digest();

            expect(featureService.enable.calls.count()).toBe(2);
            expect(featureService.enable.calls.argsFor(0)).toEqual(['feat0']);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['feat2']);
            expect(featureService.disable).not.toHaveBeenCalled();

            // WHEN
            perspectiveService.refreshPerspective();
            $rootScope.$digest();

            // THEN
            expect(featureService.enable.calls.count()).toBe(4);
            expect(featureService.enable.calls.argsFor(2)).toEqual(['feat0']);
            expect(featureService.enable.calls.argsFor(3)).toEqual(['feat2']);

            expect(featureService.disable.calls.count()).toBe(2);
            expect(featureService.disable.calls.argsFor(0)).toEqual(['feat0']);
            expect(featureService.disable.calls.argsFor(1)).toEqual(['feat2']);

            expect(crossFrameEventService.publish).toHaveBeenCalledWith('EVENT_PERSPECTIVE_REFRESHED', true);
        });

        it('WILL select NONE_PERSPECTIVE if the active perspective is not permitted', function() {
            var PERMISSION1 = 'permission1';

            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2'],
                permissions: [PERMISSION1]
            };

            perspectiveService.register(perspective0);
            perspectiveService.switchTo(perspective0.key);

            permissionService.isPermitted.and.callFake(function() {
                return $q.when(false);
            });

            // WHEN
            perspectiveService.refreshPerspective();
            $rootScope.$digest();

            expect(featureService.enable).not.toHaveBeenCalled();

            expect(featureService.disable.calls.count()).toBe(2);
            expect(featureService.disable.calls.argsFor(0)).toEqual(['feat0']);
            expect(featureService.disable.calls.argsFor(1)).toEqual(['feat2']);

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(EVENT_PERSPECTIVE_CHANGED, false);
        });
    });

    describe('switchTo', function() {
        it('WILL silently do nothing if trying to switch to the same perspecitve as the activate one', function() {

            // GIVEN
            perspectiveService.register({
                key: 'aperspective',
                nameI18nKey: 'perspective.none.name',
                descriptionI18nKey: 'perspective.none.description',
                features: ["fakeFeature"]
            });

            // WHEN
            perspectiveService.switchTo('aperspective');
            perspectiveService.switchTo('aperspective');
            $rootScope.$digest();

            // THEN
            expect(storageService.putValueInCookie).toHaveBeenCalledTimes(1);
            expect(iFrameManager.showWaitModal).toHaveBeenCalledTimes(1);
            expect(featureService.enable).toHaveBeenCalledTimes(1);
            expect(featureService.disable).not.toHaveBeenCalled();
        });

        it('WILL throw an error if required perspective is not found', function() {
            // GIVEN
            spyOn(perspectiveService, '_findByKey').and.returnValue(null);

            // WHEN/THEN
            expect(function() {
                perspectiveService.switchTo('aperspective');
            }).toThrowError("switchTo() - Couldn't find perspective with key aperspective");

            expect(perspectiveService._findByKey).toHaveBeenCalledWith('aperspective');
            expect(storageService.putValueInCookie).not.toHaveBeenCalled();
            expect(iFrameManager.showWaitModal).not.toHaveBeenCalled();
            expect(featureService.enable).not.toHaveBeenCalled();
            expect(featureService.disable).not.toHaveBeenCalled();
        });

        it('NONE_PERSPECTIVE WILL publish a rerender/false and hide the wait modal', function() {
            // WHEN
            perspectiveService.switchTo(NONE_PERSPECTIVE);
            $rootScope.$digest();

            // THEN
            expect(iFrameManager.showWaitModal).toHaveBeenCalled();
            expect(iFrameManager.hideWaitModal).toHaveBeenCalled();
        });


        it('WILL activate all (nested) features of a perspective and notify to rerender', function() {
            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0']
            };
            var perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            var perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            var perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: []
            };
            perspectiveService.register(perspective0);
            perspectiveService.register(perspective1);
            perspectiveService.register(perspective2);
            perspectiveService.register(perspective4);

            // WHEN
            perspectiveService.switchTo('persp1');
            $rootScope.$digest();

            // THEN
            expect(storageService.putValueInCookie).toHaveBeenCalledWith('smartedit-perspectives', 'persp1', true);
            expect(iFrameManager.showWaitModal).toHaveBeenCalled();
            expect(featureService.enable.calls.count()).toBe(4);
            expect(featureService.enable.calls.argsFor(0)).toEqual(['feat1']);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['feat2']);
            expect(featureService.enable.calls.argsFor(2)).toEqual(['feat3']);
            expect(featureService.enable.calls.argsFor(3)).toEqual(['feat4']);
            expect(iFrameManager.hideWaitModal).not.toHaveBeenCalled();
        });

        it('WILL disable features of previous perspective not present in new one and activate features of new perspective not present in previous one', function() {
            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2', 'feat3']
            };

            var perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            var perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            var perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: []
            };
            perspectiveService.register(perspective0);
            perspectiveService.register(perspective1);
            perspectiveService.register(perspective2);
            perspectiveService.register(perspective4);

            // WHEN
            perspectiveService.switchTo('persp0');
            perspectiveService.switchTo('persp1');
            $rootScope.$digest();

            // THEN
            expect(storageService.putValueInCookie).toHaveBeenCalledWith('smartedit-perspectives', 'persp1', true);
            expect(featureService.disable.calls.count()).toBe(1);
            expect(featureService.disable).toHaveBeenCalledWith('feat0');
            expect(iFrameManager.showWaitModal).toHaveBeenCalled();
            expect(featureService.enable.calls.count()).toBe(5);
            expect(featureService.enable.calls.argsFor(3)).toEqual(['feat1']);
            expect(featureService.enable.calls.argsFor(4)).toEqual(['feat4']);
            expect(iFrameManager.hideWaitModal).not.toHaveBeenCalled();
        });

        it('WILL throw an error WHEN the perspective is not found', function() {
            expect(
                function() {
                    perspectiveService.switchTo('whatever');
                }
            ).toThrow();
            expect(iFrameManager.showWaitModal).not.toHaveBeenCalled();
        });

        it('will publish a perspective changed event after a successful switch', function() {
            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2', 'feat3']
            };

            perspectiveService.register(perspective0);

            // WHEN
            perspectiveService.switchTo(perspective0.key);
            $rootScope.$digest();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(EVENT_PERSPECTIVE_CHANGED, true);
        });

        it('enable function called when feature has permission', function() {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue($q.when(['se.fake.permission']));
            permissionService.isPermitted.and.returnValue($q.when(true));

            var perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            perspectiveService.register(perspective);
            perspectiveService.switchTo(perspective.key);
            $rootScope.$digest();

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([{
                names: ['se.fake.permission']
            }]);
            expect(featureService.enable).toHaveBeenCalled();
        });

        it('when getFeatureProperty for permissions returns undefined, an empty array is used for permission.names', function() {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue($q.when(undefined));

            var perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            perspectiveService.register(perspective);
            perspectiveService.switchTo(perspective.key);
            $rootScope.$digest();

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([{
                names: []
            }]);
        });

        it('enable function is not called when feature does not have permission', function() {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue($q.when(['se.fake.permission']));
            permissionService.isPermitted.and.returnValue($q.when(false));

            var perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            perspectiveService.register(perspective);
            perspectiveService.switchTo(perspective.key);
            $rootScope.$digest();

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([{
                names: ['se.fake.permission']
            }]);
            expect(featureService.enable).not.toHaveBeenCalled();
        });
    });

    describe('clearActivePerspective', function() {
        it('WILL delete active perspective', function() {
            // GIVEN
            var perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2', 'feat3']
            };

            var perspective1 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3']
            };

            perspectiveService.register(perspective0);
            perspectiveService.register(perspective1);

            // WHEN
            perspectiveService.switchTo(perspective0.key);
            $rootScope.$digest();

            var perspectives = getPerspectives();

            // THEN
            expect(perspectives.length).toBe(4); //includes 2 default perspectives

            perspectiveService.clearActivePerspective();
            expect(perspectiveService.getActivePerspective()).toBeNull();
        });
    });

    describe('selectDefault', function() {
        it('WILL select NONE_PERSPECTIVE if none is found in smartedit-perspectives cookie', function() {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue();
            spyOn(perspectiveService, '_disableAllFeaturesForPerspective').and.returnValue();
            storageService.getValueFromCookie.and.returnValue($q.when(null));

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            var result = perspectiveService.selectDefault();
            $rootScope.$digest();

            //THEN
            result.then(function() {
                expect(storageService.getValueFromCookie).toHaveBeenCalledWith("smartedit-perspectives", true);
                expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
                expect(perspectiveService._disableAllFeaturesForPerspective).not.toHaveBeenCalled();
            });
        });

        it('WILL select the perspective by disabling all features first if a perspective already exists', function() {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue();
            spyOn(perspectiveService, '_disableAllFeaturesForPerspective').and.returnValue();
            storageService.getValueFromCookie.and.returnValue($q.when(ALL_PERSPECTIVE));

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            var result = perspectiveService.selectDefault();
            $rootScope.$digest();

            //THEN
            result.then(function() {
                expect(storageService.getValueFromCookie).toHaveBeenCalledWith("smartedit-perspectives", true);
                expect(perspectiveService.switchTo).toHaveBeenCalledWith(ALL_PERSPECTIVE);
                expect(perspectiveService._disableAllFeaturesForPerspective).toHaveBeenCalledWith(ALL_PERSPECTIVE);
            });
        });
    });

    describe('getActivePerspective', function() {
        it('returns null when there is no active perspective', function() {
            expect(perspectiveService.getActivePerspective()).toBeNull();
        });

        it('returns active perspective', function() {

            // GIVEN
            perspectiveService.switchTo(NONE_PERSPECTIVE);

            var nonePerspective = getPerspective(NONE_PERSPECTIVE);

            // WHEN
            var activePerspective = perspectiveService.getActivePerspective();

            // THEN
            expect(activePerspective).toEqual(nonePerspective);
        });
    });

    describe('isEmptyPerspectiveActive', function() {
        it('returns true if active perspective is NONE_PERSPECTIVE', function() {
            // GIVEN
            var nonePerspective = {
                key: NONE_PERSPECTIVE,
                nameI18nKey: NONE_PERSPECTIVE,
                features: ['feat0']
            };
            perspectiveService.register(nonePerspective);

            // WHEN
            perspectiveService.switchTo(nonePerspective.key);
            $rootScope.$digest();

            // THEN
            expect(perspectiveService.isEmptyPerspectiveActive()).toBe(true);
        });

        it('returns false if active perspective is not NONE_PERSPECTIVE', function() {
            expect(perspectiveService.isEmptyPerspectiveActive()).toBe(false);
        });
    });

    describe('cleanup', function() {
        it('WHEN _onLogoutPerspectiveCleanup is called THEN it disables all active features and removes current active perspective', function(done) {
            // GIVEN
            var features = ["feature1", "feature2"];
            var perspective = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: features
            };
            perspectiveService.register(perspective);
            perspectiveService.switchTo(perspective.key);

            $rootScope.$digest();

            spyOn(perspectiveService, '_fetchAllFeatures').and.callFake(function(perspectiveName, tempArray) {
                features.forEach(function(feature) {
                    tempArray.push(feature);
                });
            });
            spyOn(perspectiveService, 'clearActivePerspective').and.callThrough();
            spyOn(perspectiveService, '_clearPerspectiveFeatures').and.callThrough();

            // WHEN
            perspectiveService._onLogoutPerspectiveCleanup().then(function() {
                expect(featureService.disable.calls.count()).toBe(2);
                expect(featureService.disable).toHaveBeenCalledWith(features[0]);
                expect(featureService.disable).toHaveBeenCalledWith(features[1]);
                expect(perspectiveService.clearActivePerspective).toHaveBeenCalled();
                done();
            });
            $rootScope.$digest();
        });
    });

    describe('getPerspectives', function() {

        it('returns perspectives for which user is granted permission', function() {

            // GIVEN
            var PERMISSION1 = 'permission1';
            var PERMISSION2 = 'permission2';

            var perspectiveConfig1 = {
                key: 'perspectiveKey1',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                permissions: [PERMISSION1]
            };

            var perspectiveConfig2 = {
                key: 'perspectiveKey2',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                permissions: [PERMISSION2]
            };

            var perspectiveConfig3 = {
                key: 'perspectiveKey3',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz']
            };

            permissionService.isPermitted.and.callFake(function(permissions) {
                switch (permissions[0].names[0]) {
                    case PERMISSION1:
                        return $q.when(false);

                    case PERMISSION2:
                        return $q.when(true);
                }
            });

            perspectiveService.register(perspectiveConfig1);
            perspectiveService.register(perspectiveConfig2);
            perspectiveService.register(perspectiveConfig3);

            // WHEN
            var perspectives = getPerspectives();

            var perspective1 = findPerspective(perspectives, perspectiveConfig1.key);
            var perspective2 = findPerspective(perspectives, perspectiveConfig2.key);
            var perspective3 = findPerspective(perspectives, perspectiveConfig3.key);

            // THEN
            expect(perspective1).toBeUndefined();
            expect(perspective2).toBeDefined();
            expect(perspective3).toBeDefined();

            expect(permissionService.isPermitted).toHaveBeenCalledTimes(2);
            expect(permissionService.isPermitted.calls.argsFor(0)[0]).toEqual([jasmine.objectContaining({
                names: perspectiveConfig1.permissions
            })]);
            expect(permissionService.isPermitted.calls.argsFor(1)[0]).toEqual([jasmine.objectContaining({
                names: perspectiveConfig2.permissions
            })]);
        });

    });

    /*
     * This function is used to simply calling perspectiveService.getPerspectives(). It returns
     * a promise, so here, $rootScope.$apply() is called to resolve it immediately to return the
     * list. It avoids repeating the process in every test.
     */
    function getPerspectives() {
        var perspectives = [];

        perspectiveService.getPerspectives().then(function(result) {
            perspectives = result;
        });

        $rootScope.$apply();

        return perspectives;
    }

    function getPerspective(key) {
        return findPerspective(getPerspectives(), key);
    }

    function findPerspective(perspectives, key) {
        return perspectives.find(function(perspective) {
            return perspective.key === key;
        });
    }

});
