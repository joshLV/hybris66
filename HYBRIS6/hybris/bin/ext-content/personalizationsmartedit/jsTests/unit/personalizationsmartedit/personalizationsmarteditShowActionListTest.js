describe('personalizationsmarteditShowActionListModule', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var $componentController, personalizationsmarteditUtils;

    var mockSelectedItems = [{
        containerId: '1234',
        visible: false
    }, {
        containerId: '5678',
        visible: false
    }];

    beforeEach(module('personalizationsmarteditCommons', function($provide) {
        personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['getClassForElement', 'getLetterForElement']);
        $provide.value("personalizationsmarteditUtils", personalizationsmarteditUtils);
    }));

    beforeEach(module('personalizationsmarteditContextServiceModule', function($provide) {
        mockModules.personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCombinedView']);
        $provide.value('personalizationsmarteditContextService', mockModules.personalizationsmarteditContextService);
    }));

    beforeEach(module('personalizationsmarteditShowActionListModule'));
    beforeEach(inject(function(_$componentController_) {
        $componentController = _$componentController_;

        mockModules.personalizationsmarteditContextService.getCombinedView.and.callFake(function() {
            return {
                selectedItems: mockSelectedItems
            };
        });

        personalizationsmarteditUtils.getClassForElement.and.returnValue(function() {
            return 'classForElement';
        });
        personalizationsmarteditUtils.getLetterForElement.and.returnValue('letterForElement');
    }));

    describe('Component API', function() {

        it('should have proper api when initialized without parameters', function() {
            var ctrl = $componentController('personalizationsmarteditShowActionList', null);

            expect(ctrl.selectedItems).not.toBeDefined();
            expect(ctrl.getClassForElement).not.toBeDefined();
            expect(ctrl.getLetterForElement).not.toBeDefined();
            expect(ctrl.$onInit).toBeDefined();
        });

        it('should have proper api when initialized with parameters', function() {
            var bindings = {
                component: {
                    containerId: '1234'
                }
            };

            var ctrl = $componentController('personalizationsmarteditShowActionList', null, bindings);
            ctrl.$onInit();

            expect(ctrl.selectedItems.length).toBe(2);
            expect(ctrl.getClassForElement).toBeDefined();
            expect(ctrl.getLetterForElement).toBeDefined();
            expect(ctrl.$onInit).toBeDefined();

            expect(ctrl.component.containerId).toEqual('1234');
        });
    });
});
