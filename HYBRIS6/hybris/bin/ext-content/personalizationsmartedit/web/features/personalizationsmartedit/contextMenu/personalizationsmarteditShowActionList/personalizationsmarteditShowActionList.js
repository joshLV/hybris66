angular.module('personalizationsmarteditShowActionListModule', [
        'personalizationsmarteditCommons',
        'personalizationsmarteditContextServiceModule'
    ])
    .controller('personalizationsmarteditShowActionListController', function(personalizationsmarteditContextService, personalizationsmarteditUtils) {
        var self = this;

        //Methods
        this.initItem = function(item) {
            item.visible = false;
            (item.variation.actions || []).forEach(function(elem) {
                if (elem.containerId && elem.containerId === self.component.containerId) {
                    item.visible = true;
                }
            });
        };

        this.isCustomizationFromCurrentCatalog = function(customization) {
            return personalizationsmarteditUtils.isItemFromCurrentCatalog(customization, personalizationsmarteditContextService.getSeData());
        };

        //Lifecycle methods
        this.$onInit = function() {
            self.selectedItems = personalizationsmarteditContextService.getCombinedView().selectedItems;

            self.getClassForElement = personalizationsmarteditUtils.getClassForElement;
            self.getLetterForElement = personalizationsmarteditUtils.getLetterForElement;
        };
    })
    .component('personalizationsmarteditShowActionList', {
        templateUrl: 'personalizationsmarteditShowActionListTemplate.html',
        controller: 'personalizationsmarteditShowActionListController',
        controllerAs: 'ctrl',
        transclude: false,
        bindings: {
            component: '<'
        }
    });
