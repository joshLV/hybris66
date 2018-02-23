angular.module('personalizationsmarteditcontainermodule', [
        'personalizationsmarteditcontainerTemplates',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditRestServiceModule',
        'ui.bootstrap',
        'personalizationsmarteditCommons',
        'functionsModule',
        'personalizationsmarteditPreviewServiceModule',
        'personalizationsmarteditManagerModule',
        'personalizationsmarteditManagerViewModule',
        'personalizationsmarteditContextMenu',
        'featureServiceModule',
        'perspectiveServiceModule',
        'iFrameManagerModule',
        'personalizationsmarteditCombinedViewModule',
        'personalizationsmarteditSegmentViewModule',
        'personalizationsmarteditToolbarContextModule',
        'crossFrameEventServiceModule',
        'seConstantsModule',
        'personalizationsmarteditRulesAndPermissionsRegistrationModule',
        'gatewayFactoryModule',
        'yjqueryModule',
        'eventServiceModule',
        'personalizationsmarteditCustomizeViewModule'
    ])
    .factory('personalizationsmarteditIFrameUtils', function($filter, iFrameManager, personalizationsmarteditContextService, personalizationsmarteditPreviewService, personalizationsmarteditMessageHandler) {
        var iframeUtils = {};

        iframeUtils.reloadPreview = function(resourcePath, previewTicketId) {
            iFrameManager.loadPreview(resourcePath, previewTicketId);
        };

        iframeUtils.clearAndReloadPreview = function() {
            var previewTicketId = personalizationsmarteditContextService.getSeData().sePreviewData.previewTicketId;
            personalizationsmarteditPreviewService.removePersonalizationDataFromPreview(previewTicketId).then(function successCallback() {
                var previewData = personalizationsmarteditContextService.getSeData().sePreviewData;
                iframeUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
            }, function errorCallback() {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
            });
        };

        return iframeUtils;
    })
    .controller('topToolbarMenuController', function($scope, personalizationsmarteditManager, personalizationsmarteditManagerView, personalizationsmarteditIFrameUtils, personalizationsmarteditContextService, personalizationsmarteditContextUtils, personalizationsmarteditCombinedView) {
        $scope.status = {
            isopen: false
        };

        $scope.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

        $scope.createCustomizationClick = function() {
            personalizationsmarteditManager.openCreateCustomizationModal();
        };

        $scope.managerViewClick = function() {
            personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
            personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
            personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
            personalizationsmarteditManagerView.openManagerAction();
        };

        $scope.combinedViewClick = function() {
            personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
            personalizationsmarteditCombinedView.openManagerAction();
        };
        $scope.isCustomizeCustomizationSelected = function() {
            return personalizationsmarteditContextService.getCustomize().selectedCustomization;
        };
        $scope.isCombinedViewCustomizationSelected = function() {
            return personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization;
        };

    })
    .run(
        function($q, yjQuery, personalizationsmarteditContextService, personalizationsmarteditContextServiceReverseProxy, personalizationsmarteditContextModal, featureService, perspectiveService, personalizationsmarteditIFrameUtils, personalizationsmarteditContextUtils, crossFrameEventService, EVENT_PERSPECTIVE_UNLOADING, smartEditBootstrapGateway, EVENTS, systemEventService, personalizationsmarteditMessageHandler, domain, personalizationsmarteditRestService) {

            var loadCSS = function(href) {
                var cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
                yjQuery("head").append(cssLink);
            };
            loadCSS(domain + "/personalizationsmartedit/css/style.css");

            var PERSONALIZATION_PERSPECTIVE_KEY = "personalizationsmartedit.perspective";

            var PersonalizationviewContextServiceReverseProxy = new personalizationsmarteditContextServiceReverseProxy('PersonalizationCtxReverseGateway'); //NOSONAR

            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.pagecustomizations.toolbar',
                type: 'TEMPLATE',
                nameI18nKey: 'personalization.toolbar.pagecustomizations',
                priority: 4,
                section: 'left',
                include: 'personalizationsmarteditCustomizeViewWrapperTemplate.html',
                keepAliveOnClose: false,
                permissions: ['se.edit.page']
            });
            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.combinedview.toolbar',
                type: 'TEMPLATE',
                nameI18nKey: 'personalization.toolbar.combinedview.name',
                priority: 6,
                section: 'left',
                include: 'personalizationsmarteditCombinedViewMenuTemplate.html',
                keepAliveOnClose: false,
                permissions: ['se.read.page']
            });
            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.manager.toolbar',
                type: 'TEMPLATE',
                nameI18nKey: 'personalization.toolbar.library.name',
                priority: 8,
                section: 'left',
                include: 'personalizationsmarteditCustomizationManagMenuTemplate.html',
                keepAliveOnClose: false,
                permissions: ['se.edit.page']
            });
            featureService.register({
                key: 'personalizationsmartedit.context.service',
                nameI18nKey: 'personalization.context.service.name',
                descriptionI18nKey: 'personalization.context.service.description',
                enablingCallback: function() {
                    var personalization = personalizationsmarteditContextService.getPersonalization();
                    personalization.enabled = true;
                    personalizationsmarteditContextService.setPersonalization(personalization);
                },
                disablingCallback: function() {
                    var personalization = personalizationsmarteditContextService.getPersonalization();
                    personalization.enabled = false;
                    personalizationsmarteditContextService.setPersonalization(personalization);
                },
                permissions: ['se.edit.page']
            });

            perspectiveService.register({
                key: PERSONALIZATION_PERSPECTIVE_KEY,
                nameI18nKey: 'personalization.perspective.name',
                descriptionI18nKey: 'personalization.perspective.description',
                features: ['personalizationsmartedit.context.service',
                    'personalizationsmartedit.container.pagecustomizations.toolbar',
                    'personalizationsmartedit.container.manager.toolbar',
                    'personalizationsmartedit.container.combinedview.toolbar',
                    'personalizationsmarteditSharedSlot',
                    'personalizationsmarteditComponentLightUp',
                    'personalizationsmarteditCombinedViewComponentLightUp',
                    'personalizationsmartedit.context.add.action',
                    'personalizationsmartedit.context.edit.action',
                    'personalizationsmartedit.context.delete.action',
                    'personalizationsmartedit.context.info.action',
                    'personalizationsmartedit.context.component.edit.action',
                    'personalizationsmartedit.context.show.action.list',
                    'se.contextualMenu',
                    'se.emptySlotFix',
                    'externalcomponentbutton',
                    'personalizationsmarteditExternalComponentDecorator'
                ],
                perspectives: [],
                permissions: ['se.personalization.open']
            });

            var clearAllContextsAndReloadPreview = function() {
                personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(personalizationsmarteditIFrameUtils, personalizationsmarteditContextService);
            };

            var clearAllContexts = function() {
                personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCustomizeContext(personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCombinedViewContext(personalizationsmarteditContextService);
            };

            crossFrameEventService.subscribe(EVENT_PERSPECTIVE_UNLOADING, function(eventId, unloadingPerspective) {
                if (unloadingPerspective === PERSONALIZATION_PERSPECTIVE_KEY) {
                    clearAllContextsAndReloadPreview();
                }
            });

            systemEventService.registerEventHandler(EVENTS.EXPERIENCE_UPDATE, function() {
                personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCustomizeContext(personalizationsmarteditContextService);
                personalizationsmarteditContextUtils.clearCombinedViewContext(personalizationsmarteditContextService);
                return $q.when();
            });

            smartEditBootstrapGateway.subscribe("smartEditReady", function(eventId, data) {

                var oldPreviewTicketId = (personalizationsmarteditContextService.getSeData().sePreviewData || {}).previewTicketId;
                personalizationsmarteditContextService.refreshPreviewData().then(function successCallback() {
                    var newPreviewTicketId = personalizationsmarteditContextService.getSeData().sePreviewData.previewTicketId;
                    if (oldPreviewTicketId !== newPreviewTicketId) {
                        personalizationsmarteditRestService.getPreviewTicket(newPreviewTicketId).then(function successCallback(newPreview) {
                            if (newPreview.variations.length === 0) {
                                personalizationsmarteditRestService.getPreviewTicket(oldPreviewTicketId).then(function successCallback(oldPreview) {
                                    if (oldPreview.variations.length > 0 && newPreview.variations.length === 0) {
                                        clearAllContexts();
                                    }
                                }, function errorCallback() {
                                    clearAllContexts(); //old preview ticket not found
                                });
                            }
                        });
                    }
                });

                personalizationsmarteditContextService.refreshExperienceData().then(function successCallback() {
                    var experience = personalizationsmarteditContextService.getSeData().seExperienceData;
                    var activePerspective = perspectiveService.getActivePerspective() || {};
                    if (activePerspective.key === PERSONALIZATION_PERSPECTIVE_KEY && experience.pageContext.catalogVersionUuid !== experience.catalogDescriptor.catalogVersionUuid) {
                        var warningConf = {
                            message: 'personalization.warning.pagefromparent',
                            timeout: -1
                        };
                        personalizationsmarteditMessageHandler.sendWarning(warningConf);
                    }
                }).finally(function() {
                    personalizationsmarteditContextService.applySynchronization();
                });
            });

        });
