angular.module('personalizationsmarteditmodule', [
        'decoratorServiceModule',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditComponentLightUpDecorator',
        'personalizationsmarteditCombinedViewComponentLightUpDecorator',
        'personalizationsmarteditContextMenu',
        'personalizationsmarteditCommons',
        'personalizationsmarteditSharedSlotDecorator',
        'featureServiceModule',
        'personalizationsmarteditShowActionListModule',
        'yjqueryModule',
        'personalizationsmarteditContextualMenuServiceModule',
        'externalComponentDecoratorModule',
        'externalComponentButtonModule',
        'personalizationsmarteditComponentHandlerServiceModule',
        'personalizationsmarteditExternalComponentDecoratorModule'
    ])
    .directive('body', function(personalizationsmarteditContextService) {
        return {
            link: function(scope, element, attrs) {
                scope.$watch('element.class', function() {
                    var pageIdArray = element.attr('class').split(" ").filter(function(elem) {
                        return /smartedit-page-uid\-(\S+)/.test(elem);
                    });
                    if (pageIdArray.length > 0) {
                        var pageId = /smartedit-page-uid\-(\S+)/.exec(pageIdArray[0])[1];
                        personalizationsmarteditContextService.setPageId(pageId);
                        if (pageIdArray.length > 1) {
                            console.log("more than one page- class element attribute defined");
                        }
                    }
                }, true);

            }
        };
    })
    .run(
        function($q, yjQuery, decoratorService, personalizationsmarteditContextualMenuService, personalizationsmarteditContextServiceProxy, personalizationsmarteditContextModalHelper, featureService, personalizationsmarteditUtils, domain, personalizationsmarteditComponentHandlerService) {

            var loadCSS = function(href) {
                var cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
                yjQuery("head").append(cssLink);
            };
            loadCSS(domain + "/personalizationsmartedit/css/style.css");

            var PersonalizationviewContextServiceProxy = new personalizationsmarteditContextServiceProxy('PersonalizationCtxGateway'); //NOSONAR

            decoratorService.addMappings({
                '^.*Slot$': ['personalizationsmarteditSharedSlot']
            });

            decoratorService.addMappings({
                '^.*Component$': ['personalizationsmarteditComponentLightUp', 'personalizationsmarteditCombinedViewComponentLightUp']
            });

            decoratorService.addMappings({
                '^((?!Slot).)*$': ['personalizationsmarteditExternalComponentDecorator']
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditExternalComponentDecorator',
                nameI18nKey: 'personalizationsmarteditExternalComponentDecorator',
                displayCondition: function(componentType, componentId) {
                    var component = personalizationsmarteditComponentHandlerService.getOriginalComponent(componentId, componentType);
                    var container = personalizationsmarteditComponentHandlerService.getParentContainerForComponent(component);
                    if (container.length > 0 && container[0].attributes["data-smartedit-personalization-action-id"]) {
                        return $q.when(false);
                    }
                    return $q.when(personalizationsmarteditComponentHandlerService.isExternalComponent(componentId, componentType));
                }
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditComponentLightUp',
                nameI18nKey: 'personalizationsmarteditComponentLightUp'
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditCombinedViewComponentLightUp',
                nameI18nKey: 'personalizationsmarteditCombinedViewComponentLightUp'
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditSharedSlot',
                nameI18nKey: 'personalizationsmarteditSharedSlot'
            });

            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.add.action",
                i18nKey: 'personalization.context.action.add',
                nameI18nKey: 'personalization.context.action.add',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuAddItemEnabled(config);
                },
                action: {
                    callback: function(config, $event) {
                        personalizationsmarteditContextModalHelper.openAddAction(config);
                    }
                },
                displayClass: "addactionbutton",
                displayIconClass: "hyicon hyicon-addlg cmsx-ctx__icon personalization-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-addlg cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.edit.action",
                i18nKey: 'personalization.context.action.edit',
                nameI18nKey: 'personalization.context.action.edit',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuEditItemEnabled(config);
                },
                action: {
                    callback: function(config, $event) {
                        personalizationsmarteditContextModalHelper.openEditAction(config);
                    }
                },
                displayClass: "replaceactionbutton",
                displayIconClass: "hyicon hyicon-change cmsx-ctx__icon personalization-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-change cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.delete.action",
                i18nKey: 'personalization.context.action.delete',
                nameI18nKey: 'personalization.context.action.delete',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuDeleteItemEnabled(config);
                },
                action: {
                    callback: function(config, $event) {
                        personalizationsmarteditContextModalHelper.openDeleteAction(config);
                    }
                },
                displayClass: "removeactionbutton",
                displayIconClass: "hyicon hyicon-removelg cmsx-ctx__icon personalization-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-removelg cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.info.action",
                i18nKey: 'personalization.context.action.info',
                nameI18nKey: 'personalization.context.action.info',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuInfoItemEnabled(config.element);
                },
                action: {
                    template: '<div class="perso-ctx__button-template" data-translate="personalization.error.nocustomizationvariationselected"></div>'
                },
                displayClass: "infoactionbutton",
                displayIconClass: "hyicon hyicon-warning cmsx-ctx__icon personalization-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-warning cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.component.edit.action",
                i18nKey: 'personalization.context.component.action.edit',
                nameI18nKey: 'personalization.context.component.action.edit',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuEditComponentItemEnabled(config);
                },
                action: {
                    callback: function(config, $event) {
                        personalizationsmarteditContextModalHelper.openEditComponentAction(config);
                    }
                },
                displayClass: "editbutton",
                displayIconClass: "hyicon hyicon-edit cmsx-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-edit cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.show.action.list",
                i18nKey: 'personalization.context.action.list.show',
                nameI18nKey: 'personalization.context.action.list.show',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextualMenuService.isContextualMenuShowActionListEnabled(config);
                },
                action: {
                    template: '<personalizationsmartedit-show-action-list data-component="componentDetails"></personalizationsmartedit-show-action-list>'
                },
                displayClass: "showactionlistbutton",
                displayIconClass: "hyicon hyicon-combinedview cmsx-ctx__icon personalization-ctx__icon",
                displaySmallIconClass: "hyicon hyicon-combinedview cmsx-ctx__icon--small",
                permissions: ['se.edit.page']
            });

        });
