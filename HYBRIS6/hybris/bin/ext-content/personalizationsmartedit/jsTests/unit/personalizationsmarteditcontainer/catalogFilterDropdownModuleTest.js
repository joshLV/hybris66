describe('catalogFilterDropdownModule', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var $componentController;

    beforeEach(module('catalogFilterDropdownModule'));
    beforeEach(inject(function(_$componentController_) {
        $componentController = _$componentController_;
    }));

    describe('Component API', function() {

        it('should have proper api before initialized', function() {
            var ctrl = $componentController('catalogFilterDropdown', null);

            expect(ctrl.items).not.toBeDefined();
            expect(ctrl.selectedId).not.toBeDefined();
            expect(ctrl.onSelectCallback).not.toBeDefined();
            expect(ctrl.$onInit).toBeDefined();
            expect(ctrl.onChange).toBeDefined();
            expect(ctrl.fetchStrategy).toBeDefined();
        });

        it('should have proper api after initialized', function() {
            var ctrl = $componentController('catalogFilterDropdown', null);
            ctrl.onSelectCallback = function(item) {};

            ctrl.$onInit();

            expect(ctrl.items.length).toBe(3);
            expect(ctrl.selectedId).toBe(ctrl.items[1].id);
            expect(ctrl.$onInit).toBeDefined();
            expect(ctrl.onChange).toBeDefined();
            expect(ctrl.fetchStrategy).toBeDefined();
        });

    });

});
