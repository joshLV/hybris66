angular.module('customizationsListModule', [
        'personalizationsmarteditCommons',
        'personalizationsmarteditRestServiceModule',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditPreviewServiceModule',
        'personalizationsmarteditManagerModule',
        'personalizationsmarteditContextUtilsModule',
        'eventServiceModule'
    ])
    .controller('customizationsListController',
        function($q, $filter, personalizationsmarteditContextService,
            personalizationsmarteditRestService,
            personalizationsmarteditCommerceCustomizationService,
            personalizationsmarteditMessageHandler,
            personalizationsmarteditUtils,
            personalizationsmarteditIFrameUtils,
            personalizationsmarteditDateUtils,
            personalizationsmarteditContextUtils,
            personalizationsmarteditPreviewService,
            personalizationsmarteditManager,
            systemEventService) {

            var self = this;

            //Private methods
            var updateCustomizationData = function(customization) {
                personalizationsmarteditRestService.getVariationsForCustomization(customization.code, customization).then(
                    function successCallback(response) {
                        customization.variations = response.variations || [];
                        customization.variations.forEach(function(variation) {
                            variation.numberOfCommerceActions = personalizationsmarteditCommerceCustomizationService.getCommerceActionsCount(variation);
                            variation.commerceCustomizations = personalizationsmarteditCommerceCustomizationService.getCommerceActionsCountMap(variation);
                        });
                    },
                    function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                    });
            };

            var getVisibleVariations = function(customization) {
                return personalizationsmarteditUtils.getVisibleItems(customization.variations);
            };

            var getAndSetComponentsForVariation = function(customizationId, variationId, catalog, catalogVersion) {
                personalizationsmarteditRestService.getComponenentsIdsForVariation(customizationId, variationId, catalog, catalogVersion).then(function successCallback(response) {
                    var customize = personalizationsmarteditContextService.getCustomize();
                    customize.selectedComponents = response.components;
                    personalizationsmarteditContextService.setCustomize(customize);
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponentsforvariation'));
                });
            };

            var updatePreviewTicket = function(customizationId, variationArray) {
                var previewTicketId = personalizationsmarteditContextService.getSeData().sePreviewData.previewTicketId;
                var variationKeys = personalizationsmarteditUtils.getVariationKey(customizationId, variationArray);
                personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicketId, variationKeys).then(function successCallback() {
                    var previewData = personalizationsmarteditContextService.getSeData().sePreviewData;
                    personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
                });
            };

            var refreshCustomizeContext = function() {
                var customize = personalizationsmarteditContextService.getCustomize();
                if (customize.selectedCustomization) {
                    personalizationsmarteditRestService.getCustomization(customize.selectedCustomization)
                        .then(function successCallback(response) {
                            customize.selectedCustomization = response;
                            if (customize.selectedVariations && !angular.isArray(customize.selectedVariations)) {
                                response.variations.filter(function(item) {
                                    return customize.selectedVariations.code === item.code;
                                }).forEach(function(variation) {
                                    customize.selectedVariations = variation;
                                });
                            }
                            personalizationsmarteditContextService.setCustomize(customize);
                        }, function errorCallback() {
                            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                        });
                }
            };

            //Methods
            this.initCustomization = function(customization) {
                customization.collapsed = true;
                if ((personalizationsmarteditContextService.getCustomize().selectedCustomization || {}).code === customization.code) {
                    customization.collapsed = false;
                    updateCustomizationData(customization);
                }
                personalizationsmarteditUtils.getAndSetCatalogVersionNameL10N(customization);
            };

            this.editCustomizationAction = function(customization) {
                personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
                personalizationsmarteditManager.openEditCustomizationModal(customization.code);
            };

            this.customizationClick = function(customization) {
                var combinedView = personalizationsmarteditContextService.getCombinedView();
                var currentVariations = personalizationsmarteditContextService.getCustomize().selectedVariations;
                updateCustomizationData(customization);
                var visibleVariations = getVisibleVariations(customization);
                var customize = personalizationsmarteditContextService.getCustomize();
                customize.selectedCustomization = customization;
                customize.selectedVariations = visibleVariations;
                personalizationsmarteditContextService.setCustomize(customize);
                if (visibleVariations.length > 0) {
                    var allVariations = personalizationsmarteditUtils.getVariationCodes(visibleVariations).join(",");
                    getAndSetComponentsForVariation(customization.code, allVariations, customization.catalog, customization.catalogVersion);
                }

                if ((angular.isObject(currentVariations) && !angular.isArray(currentVariations)) || combinedView.enabled) {
                    updatePreviewTicket();
                }

                combinedView.enabled = false;
                personalizationsmarteditContextService.setCombinedView(combinedView);

                self.customizationsList.filter(function(cust) {
                    return customization.name !== cust.name;
                }).forEach(function(cust, index) {
                    cust.collapsed = true;
                });
            };

            this.getSelectedVariationClass = function(variation) {
                if (angular.equals(variation.code, (personalizationsmarteditContextService.getCustomize().selectedVariations || {}).code)) {
                    return "selectedVariation";
                }
            };

            this.variationClick = function(customization, variation) {
                var customize = personalizationsmarteditContextService.getCustomize();
                customize.selectedCustomization = customization;
                customize.selectedVariations = variation;
                personalizationsmarteditContextService.setCustomize(customize);
                getAndSetComponentsForVariation(customization.code, variation.code, customization.catalog, customization.catalogVersion);
                updatePreviewTicket(customization.code, [variation]);
            };

            this.hasCommerceActions = personalizationsmarteditUtils.hasCommerceActions;

            this.getCommerceCustomizationTooltip = personalizationsmarteditUtils.getCommerceCustomizationTooltipHTML;

            this.getActivityStateForCustomization = personalizationsmarteditUtils.getActivityStateForCustomization;

            this.getActivityStateForVariation = personalizationsmarteditUtils.getActivityStateForVariation;

            this.clearAllSubMenu = function() {
                angular.forEach(self.customizationsList, function(customization) {
                    customization.subMenu = false;
                });
            };

            this.getEnablementTextForCustomization = function(customization) {
                return personalizationsmarteditUtils.getEnablementTextForCustomization(customization, 'personalization.toolbar.pagecustomizations');
            };

            this.getEnablementTextForVariation = function(variation) {
                return personalizationsmarteditUtils.getEnablementTextForVariation(variation, 'personalization.toolbar.pagecustomizations');
            };

            this.isEnabled = personalizationsmarteditUtils.isPersonalizationItemEnabled;

            this.getDatesForCustomization = function(customization) {
                var activityStr = "";
                var startDateStr = "";
                var endDateStr = "";

                if (customization.enabledStartDate || customization.enabledEndDate) {
                    startDateStr = personalizationsmarteditDateUtils.formatDateWithMessage(customization.enabledStartDate);
                    endDateStr = personalizationsmarteditDateUtils.formatDateWithMessage(customization.enabledEndDate);
                    if (!customization.enabledStartDate) {
                        startDateStr = " ...";
                    }
                    if (!customization.enabledEndDate) {
                        endDateStr = "... ";
                    }
                    activityStr += " (" + startDateStr + " - " + endDateStr + ") ";
                }
                return activityStr;
            };

            this.customizationSubMenuAction = function(customization) {
                if (!customization.subMenu) {
                    self.clearAllSubMenu();
                }
                customization.subMenu = !customization.subMenu;
            };

            this.isCustomizationFromCurrentCatalog = function(customization) {
                return personalizationsmarteditUtils.isItemFromCurrentCatalog(customization, personalizationsmarteditContextService.getSeData());
            };

            //Lifecycle methods
            this.$onInit = function() {
                systemEventService.registerEventHandler('CUSTOMIZATIONS_MODIFIED', function() {
                    refreshCustomizeContext();
                    return $q.when();
                });
            };

        })
    .component('customizationsList', {
        templateUrl: 'customizationsListTemplate.html',
        controller: 'customizationsListController',
        controllerAs: 'ctrl',
        transclude: true,
        bindings: {
            customizationsList: '<'
        }
    });
