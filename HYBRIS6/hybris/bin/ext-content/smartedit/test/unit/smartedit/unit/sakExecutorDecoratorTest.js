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
describe(
    'Unit integration test of sakExecutorDecorator directive',
    function() {
        var decorators, decoratorService, componentHandlerService, sakExecutor, parent;
        var $q, $rootScope, $compile, parentScope, directiveScope, element, smarteditComponentType, smarteditComponentId, smarteditProperties;

        beforeEach(module('ui.bootstrap'));
        beforeEach(module('coretemplates'));

        beforeEach(module('sakExecutorDecorator', function($provide) {
            decorators = ['decorator1', 'decorator2'];
            decoratorService = jasmine.createSpyObj('decoratorService', ['getDecoratorsForComponent']);
            $provide.value('decoratorService', decoratorService);

            componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getOriginalComponent', 'getParent']);
            parent = jasmine.createSpyObj('parent', ['attr']);
            var realElement = {};
            componentHandlerService.getOriginalComponent.and.returnValue(realElement);
            componentHandlerService.getParent.and.returnValue(parent);
            $provide.value('componentHandlerService', componentHandlerService);

        }));

        // Store references to $rootScope and $compile so they are available to all tests in this describe block
        beforeEach(inject(function(_$rootScope_, _sakExecutor_, _$compile_, _$q_) {
            $compile = _$compile_;
            sakExecutor = _sakExecutor_;
            spyOn(sakExecutor, 'registerScope').and.callThrough();
            smarteditComponentType = "ContentSlot";
            smarteditComponentId = "theId";
            smarteditProperties = {
                "smarteditComponentId": smarteditComponentId,
                "smarteditComponentType": smarteditComponentType
            };
            $rootScope = _$rootScope_;
            $q = _$q_;
            parentScope = $rootScope.$new();
            parentScope.active = false;
            directiveScope = parentScope.$new();
        }));

        function compileDirective() {
            element = angular.element("<div class=\"smartEditComponentX\" data-smartedit-component-id=\"" + smarteditComponentId + "\" data-smartedit-component-type=\"" + smarteditComponentType + "\">initialContent</div>");
            $compile(element)(directiveScope);
            // fire all the watches, so the scope expressions will be evaluated
            $rootScope.$digest();
            expect(element.scope()).toBe(directiveScope);
            expect(sakExecutor.registerScope).toHaveBeenCalled();
        }

        it('sakExecutor stacks decorators in this order : decorator2, decorator1', function() {
            var deferred = $q.defer();
            deferred.resolve(decorators);
            decoratorService.getDecoratorsForComponent.and.returnValue(deferred.promise);
            compileDirective();
            expect(element.find('> div.decorator2').length).toBe(1);
            expect(element.find('> div.decorator2 > div.decorator1').length).toBe(1);

        });

        it('sakExecutor will process all decorators', function() {
            var deferred = $q.defer();
            deferred.resolve(decorators);
            decoratorService.getDecoratorsForComponent.and.returnValue(deferred.promise);
            compileDirective();
        });

        it('sakExecutor will process all decorators and will add smarteditProperties map to each scope', function() {
            var deferred = $q.defer();
            deferred.resolve(decorators);
            decoratorService.getDecoratorsForComponent.and.returnValue(deferred.promise);
            compileDirective();
            expect(element.find('[data-component-attributes]').length).toBe(2);
        });

    });
