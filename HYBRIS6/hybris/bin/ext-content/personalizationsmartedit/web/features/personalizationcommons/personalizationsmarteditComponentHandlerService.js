angular.module('personalizationsmarteditComponentHandlerServiceModule', ['componentHandlerServiceModule'])
    .constant('COMPONENT_CONTAINER_TYPE', 'CxCmsComponentContainer')
    .factory('personalizationsmarteditComponentHandlerService', function(componentHandlerService, CONTAINER_TYPE_ATTRIBUTE, COMPONENT_CONTAINER_TYPE, CONTAINER_ID_ATTRIBUTE, TYPE_ATTRIBUTE, CONTENT_SLOT_TYPE) {

        var self = this;
        angular.extend(self, componentHandlerService);

        self.getParentContainerForComponent = function(component) {
            var parent = component.closest('[' + CONTAINER_TYPE_ATTRIBUTE + '=' + COMPONENT_CONTAINER_TYPE + ']');
            return parent;
        };

        self.getParentContainerIdForComponent = function(component) {
            var parent = component.closest('[' + CONTAINER_TYPE_ATTRIBUTE + '=' + COMPONENT_CONTAINER_TYPE + ']');
            return parent.attr(CONTAINER_ID_ATTRIBUTE);
        };

        self.getParentSlotForComponent = function(component) {
            var parent = component.closest('[' + TYPE_ATTRIBUTE + '=' + CONTENT_SLOT_TYPE + ']');
            return parent;
        };

        self.getParentSlotIdForComponent = function(component) {
            return componentHandlerService.getParentSlotForComponent(component);
        };

        return self;
    });
