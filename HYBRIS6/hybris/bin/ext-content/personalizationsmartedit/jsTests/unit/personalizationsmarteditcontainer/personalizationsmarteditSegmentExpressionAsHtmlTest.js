describe('personalizationsmarteditSegmentExpressionAsHtml', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var $componentController;

    beforeEach(module('personalizationsmarteditManagerModule'));
    beforeEach(module('personalizationsmarteditSegmentExpressionAsHtml'));
    beforeEach(inject(function(_$componentController_) {
        $componentController = _$componentController_;
    }));

    describe('Component API', function() {

        it('should have proper api when initialized', function() {
            var ctrl = $componentController('personalizationsmarteditSegmentExpressionAsHtml', null);

            expect(ctrl.segmentExpression).toEqual({});
            expect(ctrl.operators).toEqual(['AND', 'OR', 'NOT']);
            expect(ctrl.emptyGroup).toEqual('[]');
            expect(ctrl.emptyGroupAndOperators).toEqual(['AND', 'OR', 'NOT', '[]']);
            expect(ctrl.getExpressionAsArray).toBeDefined();
        });

    });

});
