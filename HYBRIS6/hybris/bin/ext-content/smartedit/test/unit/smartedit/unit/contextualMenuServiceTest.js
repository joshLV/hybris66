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
describe('test contextualMenuServiceModule', function() {

    var contextualMenuService, $rootScope;

    beforeEach(module('contextualMenuServiceModule'));
    beforeEach(inject(function(_contextualMenuService_, _$rootScope_) {
        contextualMenuService = _contextualMenuService_;
        $rootScope = _$rootScope_;
    }));

    function getItem(key) {
        return {
            key: key,
            action: {
                template: "dummyTemplate string"
            }
        };
    }

    it('addItems WILL throw an error when item doesnt contain a valid key', function() {
        expect(function() {
            contextualMenuService.addItems({
                'type1': [{
                    action: {
                        template: "dummyTemplate string"
                    },
                    notKey: 'contextualMenuItem1'
                }, {
                    action: {
                        template: "dummyTemplate string"
                    },
                    key: 'contextualMenuItem2'
                }]
            });
        }).toThrow(new Error("addItems() - Cannot add items. Error: Item doesn't have key."));
    });

    it('getContextualMenuByType will return an unique array of contextual menu items when componenttype is given', function() {

        contextualMenuService.addItems({
            'type1': [getItem('contextualMenuItem1'), getItem('contextualMenuItem2')]
        });

        contextualMenuService.addItems({
            'type1': [getItem('contextualMenuItem3')],
            'type2': [getItem('contextualMenuItem3'), getItem('contextualMenuItem4')]
        });

        expect(contextualMenuService.getContextualMenuByType('type1')).toEqualData(
            [getItem('contextualMenuItem1'), getItem('contextualMenuItem2'), getItem('contextualMenuItem3')]);
        expect(contextualMenuService.getContextualMenuByType('type2')).toEqualData(
            [getItem('contextualMenuItem3'), getItem('contextualMenuItem4')]);
    });

    it('getContextualMenuByType will return an unique array of contextual menu items when it matches the regexps', function() {

        contextualMenuService.addItems({
            '*Suffix': [getItem('element1'), getItem('element2')],
            '.*Suffix': [getItem('element2'), getItem('element3')],
            'TypeSuffix': [getItem('element3'), getItem('element4')],
            '^((?!Middle).)*$': [getItem('element4'), getItem('element5')],
            'PrefixType': [getItem('element5'), getItem('element6')]
        });

        expect(contextualMenuService.getContextualMenuByType('TypeSuffix')).toEqualData(
            [getItem('element1'), getItem('element2'), getItem('element3'), getItem('element4'), getItem('element5')]);

        expect(contextualMenuService.getContextualMenuByType('TypeSuffixes')).toEqualData(
            [getItem('element2'), getItem('element3'), getItem('element4'), getItem('element5')]);

        expect(contextualMenuService.getContextualMenuByType('MiddleTypeSuffix')).toEqualData(
            [getItem('element1'), getItem('element2'), getItem('element3')]);
    });

    describe('getContextualMenuItems will return an array-of-array of contextual menu items based on condition', function() {

        it('will return those menu items which satisfy the condition or those that have no condition set (default condition to be true)', function() {

            //GIVEN
            contextualMenuService.addItems({
                'ComponentType1': [{
                    key: 'key1',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key2',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON2',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    key: 'key3',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON3',
                    condition: function() {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    key: 'key4',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON4',
                    condition: function() {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    key: 'key5',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON5',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    key: 'key6',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON6',
                    condition: function(configuration) {
                        return configuration.componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

            //WHEN
            var promise = contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3
            });

            promise.then(function(result) {
                //THEN
                expect(result).toEqualData({
                    leftMenuItems: [{
                        key: 'key1',
                        action: {
                            template: "dummy template string"
                        },
                        i18nKey: 'ICON1',
                        icon: 'icon1.png'
                    }, {
                        key: 'key3',
                        action: {
                            template: "dummy template string"
                        },
                        i18nKey: 'ICON3',
                        condition: Function,
                        icon: 'icon3.png'
                    }, {
                        key: 'key5',
                        action: {
                            template: "dummy template string"
                        },
                        i18nKey: 'ICON5',
                        condition: Function,
                        icon: 'icon5.png'
                    }],
                    moreMenuItems: [{
                        key: 'key6',
                        action: {
                            template: "dummy template string"
                        },
                        i18nKey: 'ICON6',
                        condition: Function,
                        icon: 'icon6.png'
                    }]
                });
            });
            $rootScope.$digest();
        });

        it('for iLeftBtns= 3, will set a maximum of 3 menu items to the left (1st element in the array) and the rest to the right (2nd element in the array)', function() {

            //GIVEN
            contextualMenuService.addItems({
                'ComponentType1': [{
                    key: 'key1',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key2',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON2',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    key: 'key3',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON3',
                    condition: function() {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    key: 'key4',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON4',
                    condition: function() {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    key: 'key5',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON5',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    key: 'key6',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON6',
                    condition: function(configuration) {
                        return configuration.componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

            //WHEN
            var promise = contextualMenuService.getContextualMenuItems({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                iLeftBtns: 3
            });

            promise.then(function(result) {
                //THEN
                expect(result.leftMenuItems).toEqualData([{
                    key: 'key1',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    key: 'key3',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON3',
                    condition: function() {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    key: 'key5',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON5',
                    condition: function(configuration) {
                        return configuration.componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }]);

                //THEN
                expect(result.moreMenuItems).toEqualData([{
                    key: 'key6',
                    action: {
                        template: "dummy template string"
                    },
                    i18nKey: 'ICON6',
                    condition: function(configuration) {
                        return configuration.componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]);
            });
        });
    });

    it('getContextualMenuItems will provide the dom element', function() {

        //GIVEN
        var element = angular.element('<div></div>');
        var contextualItemMock = jasmine.createSpyObj('contextualItemMock', ['condition']);
        contextualItemMock.key = 'key1';
        contextualItemMock.i18nKey = 'ICON1';
        contextualItemMock.condition.and.returnValue(true);
        contextualItemMock.icon = 'icon1.png';
        contextualItemMock.action = {
            template: "dummy template string"
        };

        var obj = {
            'ComponentType1': [contextualItemMock]
        };
        contextualMenuService.addItems(obj);

        //WHEN
        var promise = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 1,
            element: element
        });

        promise.then(function(result) {
            //THEN
            expect(result.leftMenuItems).toEqualData(obj.ComponentType1);

            //THEN
            expect(contextualItemMock.condition).toHaveBeenCalledWith({
                componentId: 'ComponentId1',
                componentType: 'ComponentType1',
                element: element
            });
        });
    });

    it('removeItemByKey will remove all the items with the provided key when the condition is called', function() {

        //GIVEN
        contextualMenuService.addItems({
            'ComponentType1': [{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key3',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }],
            'ComponentType2': [{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key5',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]
        });

        //WHEN
        contextualMenuService.removeItemByKey('key2');
        var promise = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 3
        });

        promise.then(function(result) {

            //THEN
            expect(result.leftMenuItems).toEqualData([{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key3',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }]);

            //THEN
            expect(result.leftMenuItems).toEqualData([{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key5',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]);
        });



    });

    it('removeItemByKey will not do anything when the provided key does not match an item', function() {

        //GIVEN
        contextualMenuService.addItems({
            'ComponentType1': [{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key3',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }],
            'ComponentType2': [{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key5',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]
        });
        contextualMenuService.removeItemByKey('key10');

        //WHEN
        var promise = contextualMenuService.getContextualMenuItems({
            componentId: 'ComponentId1',
            componentType: 'ComponentType1',
            iLeftBtns: 3
        });

        promise.then(function(result) {

            //THEN
            expect(result.leftMenuItems).toEqualData([{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key3',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON3',
                icon: 'icon3.png'
            }]);

            //THEN
            expect(result.leftMenuItems).toEqualData([{
                key: 'key1',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                key: 'key2',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON2',
                icon: 'icon2.png'
            }, {
                key: 'key5',
                action: {
                    template: "dummy template string"
                },
                i18nKey: 'ICON5',
                icon: 'icon5.png'
            }]);
        });
    });
});
