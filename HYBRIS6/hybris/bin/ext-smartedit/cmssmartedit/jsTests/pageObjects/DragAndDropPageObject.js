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
/* jshint unused:false, undef:false */
(function() {

    var dragAndDropPageObject = {};

    var contextualMenu = e2e.componentObjects.componentContextualMenu;
    var componentMenu = e2e.componentObjects.componentMenu;

    dragAndDropPageObject.structure = {
        slots: {
            TOP_HEADER_SLOT: 'topHeaderSlot',
            SEARCH_BOX_SLOT: 'searchBoxSlot',
            BOTTOM_HEADER_SLOT: 'bottomHeaderSlot',
            FOOTER_SLOT: 'footerSlot'
        },
        components: {
            COMPONENT1: 'component1',
            COMPONENT2: 'component2',
            COMPONENT3: 'component3',
            COMPONENT4: 'component4',
            COMPONENT5: 'component5',
            COMPONENT10: 'component10'
        }
    };

    dragAndDropPageObject.elements = {
        getSlotById: function(slotId) {
            return element(by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + slotId + "']"));
        },
        getComponentById: function(componentId) {
            return element(by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + componentId + "']"));
        },
        getComponentsInSlot: function(slotId, expectedElementsInSlotCount) {
            return browser.wait(function() {
                var deferred = protractor.promise.defer();
                element.all(by.css(".smartEditComponent[data-smartedit-component-id='" + slotId + "'] .smartEditComponent")).count().then(function(count) {
                    deferred.fulfill(count === expectedElementsInSlotCount);
                });
                return deferred.promise;
            }, 10000).then(function() {
                return element.all(by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + slotId + "'] .smartEditComponentX"));
            });
        },
        getComponentHelpers: function(slotId, position) {
            position++; // The nth-child selector doesn't start at 0, but at 1.
            browser.waitForVisibility(by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + slotId + "']"));
            var hintElement = by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + slotId + "'] .smartEditComponentX:nth-child(" + position + "n) .overlayDropzone");
            browser.waitForVisibility(hintElement);
            return element.all(hintElement);
        },
        getComponentMenuButton: function() {
            return element(by.css('[data-item-key="se.cms.componentMenuTemplate"] > button'));
        },
        getCustomizedComponentsTab: function() {
            return element(by.css('.se-component-menu--tabs .nav-tabs [data-tab-id=componentsTab]'));
        },
        getComponentInComponentMenu: function() {
            return element(by.css(".smartEditComponent[data-smartedit-component-type='CMSParagraphComponent'] img"));
        },
        getComponentInCustomizedComponentMenu: function() {
            return element(by.css("y-infinite-scrolling .smartEditComponent[data-smartedit-component-type='AbstractCMSComponent'][data-smartedit-component-id='Component10'] img"));
        },
        getTopScrollHint: function() {
            return element(by.id('top_scroll_page'));
        },
        getBottomScrollHint: function() {
            return element(by.id('bottom_scroll_page'));
        },
        getCloneOnDropAction: function() {
            return element(by.css(".se-component-menu--clone-on-drop"));
        }
    };

    dragAndDropPageObject.actions = {
        moveToStoreFront: function() {
            browser.switchToIFrame();
        },
        moveToParent: function() {
            browser.switchToParent();
        },
        moveComponentToSlot: function(sourceSlotId, componentId, targetSlotId) {
            _grabComponent(sourceSlotId, componentId);

            var targetSlot = dragAndDropPageObject.elements.getSlotById(targetSlotId);

            _hoverOverSlot(targetSlot);
            _dropComponent(componentId, targetSlotId);
        },
        moveComponentToPositionInSlot: function(sourceSlotId, componentId, targetSlotId, position, expectedElementsInSlotCount) {
            dragAndDropPageObject.elements.getComponentsInSlot(targetSlotId, expectedElementsInSlotCount).then(function(elements) {
                _grabComponent(sourceSlotId, componentId).then(function() {
                    _hoverOverSlot(dragAndDropPageObject.elements.getSlotById(targetSlotId)).then(function() {
                        var helperPosition = (position < elements.length) ? position + 1 : position;
                        dragAndDropPageObject.elements.getComponentHelpers(targetSlotId, helperPosition).then(function(helpers) {
                            var helperElement = (position < elements.length) ? helpers[0] : helpers[1];
                            _hoverOverHint(helperElement).then(function() {
                                this.dropAtDroppableArea(componentId, helperElement);
                            }.bind(this));
                        }.bind(this));

                    }.bind(this));
                }.bind(this));

            }.bind(this));
        },
        addComponentToSlotInPosition: function(targetSlotId, position, expectedElementsInSlotCount) {
            dragAndDropPageObject.elements.getComponentsInSlot(targetSlotId, expectedElementsInSlotCount).then(function(elements) {
                var elementCount = elements.length;
                var targetSlot = dragAndDropPageObject.elements.getSlotById(targetSlotId);

                _hoverOverSlot(targetSlot).then(function() {
                    if (position < elementCount) {
                        dragAndDropPageObject.elements.getComponentHelpers(targetSlotId, position).then(function(helpers) {
                            _hoverOverHint(helpers[0]).then(function() {
                                this.dropComponentFromComponentMenu(helpers[0]);
                            }.bind(this));
                        }.bind(this));
                    } else {
                        dragAndDropPageObject.elements.getComponentHelpers(targetSlotId, position - 1).then(function(helpers) {
                            _hoverOverHint(helpers[1]).then(function() {
                                this.dropComponentFromComponentMenu(helpers[1]);
                            }.bind(this));
                        }.bind(this));
                    }
                }.bind(this));
            }.bind(this));
        },
        moveToElement: function(element) {
            browser.actions()
                .mouseMove(element)
                .perform();
        },
        grabComponent: function(slotId, componentId) {
            _grabComponent(slotId, componentId);
        },
        grabComponentInComponentMenu: function() {
            _grabComponentInComponentMenu();
        },
        enableCloneOnDropAndGrabCustomizedComponentInComponentMenu: function() {
            _enableCloneOnDropAndGrabCustomizedComponentInComponentMenu();
        },
        grabCustomizedComponentInComponentMenu: function() {
            _grabCustomizedComponentInComponentMenu();
        },
        hoverOverSlot: function(slot) {
            return _hoverOverSlot(slot);
        },
        dropComponent: function(componentId, slotId) {
            _dropComponent(componentId, slotId);
        },
        dropComponentFromComponentMenu: function(element) {
            _dropComponentFromComponentMenu(element);
        },
        dropAtDroppableArea: function(componentId, droppableArea) {
            browser.executeScript(simulateDragAndDropOperation, droppableArea.getWebElement(), EVENT_TYPES.DROP).then(function() {
                contextualMenu.elements.getMoveButtonForComponentId(componentId).then(function(elementToDrag) {
                    browser.executeScript(simulateDragAndDropOperation, elementToDrag, EVENT_TYPES.DRAG_END);
                });
            }.bind(this));
        },
        isComponentInPosition: function(slot, component, position, expectedElementsInSlotCount) {
            return _isComponentInPosition(slot, component, position, expectedElementsInSlotCount);
        },
        isSlotEnabled: function(slotId) {
            var slot = dragAndDropPageObject.elements.getSlotById(slotId);
            return hasClass(slot, 'over-slot-enabled');
        },
        isSlotDisabled: function(slotId) {
            var slot = dragAndDropPageObject.elements.getSlotById(slotId);
            return hasClass(slot, 'over-slot-disabled');
        },
        hoverOverTopHint: function() {
            var hint = dragAndDropPageObject.elements.getTopScrollHint();
            this.moveToElement(hint);
            browser.executeScript(simulateDragAndDropOperation, hint.getWebElement(), EVENT_TYPES.DRAG_ENTER);
        },
        hoverOverBottomHint: function() {
            var hint = dragAndDropPageObject.elements.getBottomScrollHint();
            this.moveToElement(hint);
            browser.executeScript(simulateDragAndDropOperation, hint.getWebElement(), EVENT_TYPES.DRAG_ENTER);
        },
        scrollToBottom: function() {
            _scrollToBottom();
        },
        getPageVerticalScroll: function() {
            return _getCurrentPageVerticalScroll();
        }
    };

    var EVENT_TYPES = {
        DRAG_START: 'dragstart',
        DRAG_END: 'dragend',
        DROP: 'drop',
        DRAG_ENTER: 'dragenter',
        DRAG_OVER: 'dragover'
    };

    function _enableCloneOnDropForCustomizedComponents() {
        browser.click(dragAndDropPageObject.elements.getCloneOnDropAction());
    }

    function _grabComponent(slotId, componentId) {
        browser.switchToIFrame();

        return contextualMenu.elements.getMoveButtonForComponentId(componentId).then(function(moveButton) {
            browser.actions()
                .mouseMove(dragAndDropPageObject.elements.getSlotById(slotId))
                .mouseMove(dragAndDropPageObject.elements.getComponentById(componentId))
                .mouseMove(moveButton)
                .mouseDown()
                .perform();

            browser.executeScript(simulateDragAndDropOperation, moveButton.getWebElement(), EVENT_TYPES.DRAG_START);
            return moveButton;
        });
    }

    function _grabComponentInComponentMenu() {
        browser.click(dragAndDropPageObject.elements.getComponentMenuButton());

        var elementToDrag = dragAndDropPageObject.elements.getComponentInComponentMenu();
        browser.actions()
            .mouseMove(elementToDrag)
            .perform();

        browser.executeScript(simulateDragAndDropOperation, elementToDrag.getWebElement(), EVENT_TYPES.DRAG_START);
    }

    function _enableCloneOnDropAndGrabCustomizedComponentInComponentMenu() {
        browser.click(dragAndDropPageObject.elements.getComponentMenuButton());
        browser.click(dragAndDropPageObject.elements.getCustomizedComponentsTab());

        _enableCloneOnDropForCustomizedComponents();

        componentMenu.actions.searchComponents(dragAndDropPageObject.structure.components.COMPONENT10);

        var elementToDrag = dragAndDropPageObject.elements.getComponentInCustomizedComponentMenu();
        browser.actions()
            .mouseMove(elementToDrag)
            .perform();

        browser.executeScript(simulateDragAndDropOperation, elementToDrag.getWebElement(), EVENT_TYPES.DRAG_START);
    }

    function _grabCustomizedComponentInComponentMenu() {
        browser.click(dragAndDropPageObject.elements.getComponentMenuButton());
        browser.click(dragAndDropPageObject.elements.getCustomizedComponentsTab());

        componentMenu.actions.searchComponents(dragAndDropPageObject.structure.components.COMPONENT10);

        var elementToDrag = dragAndDropPageObject.elements.getComponentInCustomizedComponentMenu();
        browser.actions()
            .mouseMove(elementToDrag)
            .perform();

        browser.executeScript(simulateDragAndDropOperation, elementToDrag.getWebElement(), EVENT_TYPES.DRAG_START);
    }

    // this method needs to be retired at some point when all of its usages are replaced by dropComponentFromComponentMenu or dropAtDroppableArea
    function _dropComponent(componentId, slotId) {
        var slot = dragAndDropPageObject.elements.getSlotById(slotId);
        browser.executeScript(simulateDragAndDropOperation, slot.getWebElement(), EVENT_TYPES.DROP).then(function() {
            contextualMenu.elements.getMoveButtonForComponentId(componentId).then(function(elementToDrag) {
                browser.executeScript(simulateDragAndDropOperation, elementToDrag.getWebElement(), EVENT_TYPES.DRAG_END);
            });
        });
    }

    function _dropComponentFromComponentMenu(dropabbleArea) {
        browser.executeScript(simulateDragAndDropOperation, dropabbleArea.getWebElement(), EVENT_TYPES.DROP).then(function() {
            browser.switchToParent().then(function() {
                var elementToDrag = dragAndDropPageObject.elements.getComponentInCustomizedComponentMenu();
                browser.executeScript(simulateDragAndDropOperation, elementToDrag.getWebElement(), EVENT_TYPES.DRAG_END);
                browser.switchToIFrame();
            });
        });
    }

    function _hoverOverSlot(slot) {
        return browser.actions()
            .mouseMove(slot)
            .perform()
            .then(function() {
                return browser.executeScript(simulateDragAndDropOperation, slot.getWebElement(), EVENT_TYPES.DRAG_ENTER).then(function() {
                    return browser.executeScript(simulateDragAndDropOperation, slot.getWebElement(), EVENT_TYPES.DRAG_OVER);
                });
            });
    }

    function _hoverOverHint(hint) {
        return browser.actions()
            .mouseMove(hint)
            .perform()
            .then(function() {
                return browser.executeScript(simulateDragAndDropOperation, hint.getWebElement(), EVENT_TYPES.DRAG_OVER);
            });
    }

    /*
     Unfortunately, Protractor's Chrome Driver is not compatible with HTML 5 drag and drop. If it's simulated (hover
     over component, mouse down, and then mouse move), nothing happens; the events are never triggered. Thus, we
     have to simulate the events.
     */
    function simulateDragAndDropOperation(element, operationType) {

        function createCustomEvent(type) {
            var event = document.createEvent("CustomEvent");
            event.initCustomEvent(type, true, true, null);
            event.dataTransfer = {
                data: {},
                setData: function(type, val) {
                    this.data[type] = val;
                },
                getData: function(type) {
                    return this.data[type];
                }
            };

            var rects = element.getBoundingClientRect();
            event.pageX = rects.left + Math.ceil((rects.right - rects.left) / 2);
            event.pageY = window.scrollY + rects.top + Math.ceil((rects.bottom - rects.top) / 2);

            return event;
        }

        function dispatchEvent(node, type, event) {
            if (node.dispatchEvent) {
                return node.dispatchEvent(event);
            }
            if (node.fireEvent) {
                return node.fireEvent("on" + type, event);
            }
        }

        var event = createCustomEvent(operationType);
        dispatchEvent(element, operationType, event);
    }

    // Helpers
    function _scrollToBottom() {
        return browser.executeScript('window.scrollTo(0,document.scrollingElement.scrollHeight);');
    }

    function _getCurrentPageVerticalScroll() {
        return browser.executeScript('return document.scrollingElement.scrollTop;');
    }

    function _isComponentInPosition(slotId, componentId, position, expectedElementsInSlotCount) {
        return dragAndDropPageObject.elements.getComponentsInSlot(slotId, expectedElementsInSlotCount)
            .then(function(components) {
                if (components.length > position) {
                    return browser.wait(function() {
                        var component = components[position];
                        return component.getAttribute('data-smartedit-component-id');
                    }, 10000).then(function(attr) {
                        return (attr === componentId);
                    });
                } else {
                    return false;
                }
            });
    }

    function hasClass(element, className) {
        return element.getAttribute('class').then(function(classes) {
            return classes.split(' ').indexOf(className) !== -1;
        });
    }

    module.exports = dragAndDropPageObject;

}());
