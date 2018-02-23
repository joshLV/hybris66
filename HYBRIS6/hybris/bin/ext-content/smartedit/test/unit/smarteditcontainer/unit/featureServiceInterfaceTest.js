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
describe('featureServiceInterface', function() {

    var FeatureServiceInterface, featureService, $q, $rootScope;

    beforeEach(module('featureServiceModule'));

    beforeEach(inject(function(_FeatureServiceInterface_, _$q_, _$rootScope_) {
        FeatureServiceInterface = _FeatureServiceInterface_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    beforeEach(function() {
        featureService = new FeatureServiceInterface();
    });

    describe('permissions', function() {
        var FEATURE_KEY = 'key';
        var configuration;

        beforeEach(function() {
            configuration = {
                key: FEATURE_KEY,
                nameI18nKey: 'nameI18nKey',
                enablingCallback: function() {},
                disablingCallback: function() {}
            };
        });
    });

    it('register throws exception if key is not provided', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            nameI18nKey: 'nameI18nKey',
            enablingCallback: jasmine.createSpy("enablingCallback"),
            disablingCallback: jasmine.createSpy("disablingCallback")
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.key.error.required");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register throws exception if nameI18nKey is not provided', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            key: 'somekey',
            enablingCallback: jasmine.createSpy("enablingCallback"),
            disablingCallback: jasmine.createSpy("disablingCallback")
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.nameI18nKey.error.required");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register throws exception if enablingCallback is not provided', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            key: 'somekey',
            nameI18nKey: 'nameI18nKey',
            disablingCallback: jasmine.createSpy("disablingCallback")
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.enablingCallback.error.not.function");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register throws exception if enablingCallback is not a function', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            key: 'somekey',
            nameI18nKey: 'nameI18nKey',
            enablingCallback: 'somestringinsteadofafunction',
            disablingCallback: jasmine.createSpy("disablingCallback")
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.enablingCallback.error.not.function");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register throws exception if disablingCallback is not provided', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            key: 'somekey',
            nameI18nKey: 'nameI18nKey',
            enablingCallback: jasmine.createSpy("enablingCallback")
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.disablingCallback.error.not.function");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register throws exception if disablingCallback is not a function', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var configuration = {
            key: 'somekey',
            nameI18nKey: 'nameI18nKey',
            enablingCallback: jasmine.createSpy("enablingCallback"),
            disablingCallback: 'somestringinsteadofafunction'
        };

        expect(function() {
            featureService.register(configuration);
        }).toThrowError("featureService.configuration.disablingCallback.error.not.function");

        expect(featureService._registerAliases).not.toHaveBeenCalled();
    });

    it('register delegates to _registerAliases everything but the callbacks that are kept along with the key', function() {

        spyOn(featureService, '_registerAliases').and.returnValue();

        var enablingCallback = jasmine.createSpy("enablingCallback");
        var disablingCallback = jasmine.createSpy("disablingCallback");

        var configuration = {
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            someotherkey: 'someotherkey',
            enablingCallback: enablingCallback,
            disablingCallback: disablingCallback
        };

        expect(featureService.featuresToAlias).toBeUndefined();

        featureService.register(configuration);

        expect(featureService.featuresToAlias).toEqual({
            somekey: {
                enablingCallback: jasmine.any(Function),
                disablingCallback: jasmine.any(Function)
            }
        });

        expect(featureService._registerAliases).toHaveBeenCalledWith({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            someotherkey: 'someotherkey'
        });

    });

    it('GIVEN that feature alias is found in the same frame, THEN enable will call the enabling callback is called and no request is sent across the gateway', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');
        spyOn(featureService, '_remoteEnablingFromInner').and.returnValue();
        spyOn(featureService, '_remoteDisablingFromInner').and.returnValue();

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService.enable('key1');

        expect(enablingCallback).toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect(featureService._remoteEnablingFromInner).not.toHaveBeenCalled();
        expect(featureService._remoteDisablingFromInner).not.toHaveBeenCalled();
    });

    it('GIVEN that feature alias is not found in the same frame, THEN enable will send a request across the gateway to call remote enablingCallback', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');
        spyOn(featureService, '_remoteEnablingFromInner').and.returnValue();
        spyOn(featureService, '_remoteDisablingFromInner').and.returnValue();

        featureService.featuresToAlias = {
            'key2': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService.enable('key1');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect(featureService._remoteEnablingFromInner).toHaveBeenCalledWith('key1');
        expect(featureService._remoteDisablingFromInner).not.toHaveBeenCalled();
    });

    it('GIVEN that feature alias is found in the same frame, THEN disable will call the disabling callback is called and no request is sent across the gateway', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');
        spyOn(featureService, '_remoteEnablingFromInner').and.returnValue();
        spyOn(featureService, '_remoteDisablingFromInner').and.returnValue();

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService.disable('key1');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).toHaveBeenCalled();
        expect(featureService._remoteEnablingFromInner).not.toHaveBeenCalled();
        expect(featureService._remoteDisablingFromInner).not.toHaveBeenCalled();
    });

    it('GIVEN that feature alias is not found in the same frame, THEN disable will send a request across the gateway to call remote disablingCallback', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');
        spyOn(featureService, '_remoteEnablingFromInner').and.returnValue();
        spyOn(featureService, '_remoteDisablingFromInner').and.returnValue();

        featureService.featuresToAlias = {
            'key2': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService.disable('key1');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect(featureService._remoteEnablingFromInner).not.toHaveBeenCalled();
        expect(featureService._remoteDisablingFromInner).toHaveBeenCalledWith('key1');
    });

    it('leaves _registerAliases unimplemented', function() {
        expect(FeatureServiceInterface.prototype._registerAliases).toBeEmptyFunction();
    });

    it('leaves addToolbarItem unimplemented', function() {
        expect(FeatureServiceInterface.prototype.addToolbarItem).toBeEmptyFunction();
    });

    it('leaves addDecorator unimplemented', function() {
        expect(FeatureServiceInterface.prototype.addDecorator).toBeEmptyFunction();
    });

    it('leaves addContextualMenuButton unimplemented', function() {
        expect(FeatureServiceInterface.prototype.addContextualMenuButton).toBeEmptyFunction();
    });

    it('leaves _remoteEnablingFromInner unimplemented', function() {
        expect(FeatureServiceInterface.prototype._remoteEnablingFromInner).toBeEmptyFunction();
    });

    it('leaves _remoteDisablingFromInner unimplemented', function() {
        expect(FeatureServiceInterface.prototype._remoteDisablingFromInner).toBeEmptyFunction();
    });

});
