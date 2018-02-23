angular.module('personalizationsmarteditContextMenu', [
        'personalizationsmarteditComponentHandlerServiceModule',
        'gatewayProxyModule'
    ])
    .factory('personalizationsmarteditContextModal', function(gatewayProxy) {

        var PersonalizationsmarteditContextModal = function() { //NOSONAR
            this.gatewayId = "personalizationsmarteditContextModal";
            gatewayProxy.initForService(this);
        };

        PersonalizationsmarteditContextModal.prototype.openDeleteAction = function(configuration) {};

        PersonalizationsmarteditContextModal.prototype.openAddAction = function(configuration) {};

        PersonalizationsmarteditContextModal.prototype.openEditAction = function(configuration) {};

        PersonalizationsmarteditContextModal.prototype.openEditComponentAction = function(configuration) {};

        return new PersonalizationsmarteditContextModal();
    })
    .factory('personalizationsmarteditContextModalHelper', function(personalizationsmarteditContextModal, personalizationsmarteditContextService, personalizationsmarteditComponentHandlerService) {
        var helper = {};

        var getSelectedVariationCode = function() {
            if (personalizationsmarteditContextService.getCombinedView().enabled) {
                return personalizationsmarteditContextService.getCombinedView().customize.selectedVariations.code;
            }
            return personalizationsmarteditContextService.getCustomize().selectedVariations.code;
        };

        var getSelectedCustomization = function(customizationCode) {
            if (personalizationsmarteditContextService.getCombinedView().enabled) {
                var customization = personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization;
                if (!customization && customizationCode) {
                    customization = personalizationsmarteditContextService.getCombinedView().selectedItems.filter(function(elem) {
                        return elem.customization.code === customizationCode;
                    })[0].customization;
                }
                return customization;
            }
            return personalizationsmarteditContextService.getCustomize().selectedCustomization;
        };

        helper.openDeleteAction = function(config) {
            var configProperties = angular.fromJson(config.properties);
            var configurationToPass = {};
            configurationToPass.containerId = config.containerId;
            configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
            configurationToPass.slotId = config.slotId;
            configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
            configurationToPass.selectedVariationCode = configProperties.smarteditPersonalizationVariationId || null;
            configurationToPass.selectedCustomizationCode = configProperties.smarteditPersonalizationCustomizationId || null;
            var componentCatalog = configProperties.smarteditCatalogVersionUuid.split('\/');
            configurationToPass.componentCatalog = componentCatalog[0];
            configurationToPass.componentCatalogVersion = componentCatalog[1];
            var contextCustomization = getSelectedCustomization(configurationToPass.selectedCustomizationCode);
            configurationToPass.catalog = contextCustomization.catalog;
            configurationToPass.catalogVersion = contextCustomization.catalogVersion;

            return personalizationsmarteditContextModal.openDeleteAction(configurationToPass);
        };

        helper.openAddAction = function(config) {
            var configProperties = angular.fromJson(config.properties);
            var configurationToPass = {};
            configurationToPass.componentType = config.componentType;
            configurationToPass.componentId = config.componentId;
            configurationToPass.containerId = config.containerId;
            configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
            configurationToPass.slotId = config.slotId;
            configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
            configurationToPass.selectedVariationCode = getSelectedVariationCode();
            var componentCatalog = configProperties.smarteditCatalogVersionUuid.split('\/');
            configurationToPass.componentCatalog = componentCatalog[0];
            var contextCustomization = getSelectedCustomization();
            configurationToPass.catalog = contextCustomization.catalog;
            configurationToPass.selectedCustomizationCode = contextCustomization.code;
            var slot = personalizationsmarteditComponentHandlerService.getParentSlotForComponent(config.element);
            var slotCatalog = personalizationsmarteditComponentHandlerService.getCatalogVersionUuid(slot).split('\/');
            configurationToPass.slotCatalog = slotCatalog[0];

            return personalizationsmarteditContextModal.openAddAction(configurationToPass);
        };

        helper.openEditAction = function(config) {
            var configProperties = angular.fromJson(config.properties);
            var configurationToPass = {};
            configurationToPass.componentType = config.componentType;
            configurationToPass.componentId = config.componentId;
            configurationToPass.containerId = config.containerId;
            configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
            configurationToPass.slotId = config.slotId;
            configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
            configurationToPass.selectedVariationCode = configProperties.smarteditPersonalizationVariationId || null;
            configurationToPass.selectedCustomizationCode = configProperties.smarteditPersonalizationCustomizationId || null;
            configurationToPass.componentUuid = configProperties.smarteditComponentUuid || null;

            return personalizationsmarteditContextModal.openEditAction(configurationToPass);
        };

        helper.openEditComponentAction = function(config) {
            var configProperties = angular.fromJson(config.properties);
            var configurationToPass = {};
            configurationToPass.smarteditComponentType = configProperties.smarteditComponentType;
            configurationToPass.smarteditComponentUuid = configProperties.smarteditComponentUuid;
            configurationToPass.smarteditCatalogVersionUuid = configProperties.smarteditCatalogVersionUuid;
            return personalizationsmarteditContextModal.openEditComponentAction(configurationToPass);
        };

        return helper;
    });
