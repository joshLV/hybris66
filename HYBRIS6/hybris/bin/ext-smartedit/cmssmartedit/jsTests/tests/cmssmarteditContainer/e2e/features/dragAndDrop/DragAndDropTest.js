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
describe('Drag and Drop - Background: GIVEN I am in a perspective in which drag and drop is enabled -', function() {

    var dragAndDrop = e2e.pageObjects.DragAndDrop;
    var perspective = e2e.componentObjects.modeSelector;
    var slots = dragAndDrop.structure.slots;
    var components = dragAndDrop.structure.components;
    var editorModal = e2e.componentObjects.editorModal;
    var storefront = e2e.componentObjects.storefront;

    // see layouts/sf-layout-default.js
    var BOTTOM_HEADER_TOTAL_COMPONENTS = 21;

    beforeEach(function() {
        browser.bootstrap(__dirname);
    });

    beforeEach(function(done) {
        browser.waitForWholeAppToBeReady();
        perspective.selectBasicPerspective().then(function() {
            done();
        });
    });

    describe('within frame', function() {
        it('WHEN I grab a component from one slot AND hover over another slot in which its type is not allowed THEN I should see the slot highlighted to indicate that drop is not allowed',
            function() {
                // Arrange
                dragAndDrop.actions.moveToStoreFront();

                // Act
                dragAndDrop.actions.grabComponent(slots.TOP_HEADER_SLOT, components.COMPONENT1);
                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
                var bottomSlot = dragAndDrop.elements.getSlotById(slots.BOTTOM_HEADER_SLOT);
                dragAndDrop.actions.hoverOverSlot(bottomSlot);

                // Assert
                expect(dragAndDrop.actions.isSlotEnabled(slots.BOTTOM_HEADER_SLOT)).toBe(false, 'Expected slot not to display that it allows the component');
                expect(dragAndDrop.actions.isSlotDisabled(slots.BOTTOM_HEADER_SLOT)).toBe(true, 'Expected slot to display that the component is forbidden');
            });


        it('WHEN I grab a component from one slot AND hover over another slot in which its type is allowed THEN I should see the slot highlighted to indicate that drop is allowed',
            function() {
                // Arrange
                dragAndDrop.actions.moveToStoreFront();

                // Act
                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
                dragAndDrop.actions.grabComponent(slots.BOTTOM_HEADER_SLOT, components.COMPONENT4);
                storefront.actions.moveToComponent(storefront.constants.SEARCH_BOX_SLOT);
                var searchBoxSlot = dragAndDrop.elements.getSlotById(slots.SEARCH_BOX_SLOT);
                dragAndDrop.actions.hoverOverSlot(searchBoxSlot);

                //Assert
                expect(dragAndDrop.actions.isSlotEnabled(slots.SEARCH_BOX_SLOT)).toBe(true, 'Expected slot to display that it allows the component');
                expect(dragAndDrop.actions.isSlotDisabled(slots.SEARCH_BOX_SLOT)).toBe(false, 'Expected slot not to display that the component is forbidden');
            });
    });

    describe('across frames', function() {
        it('WHEN I grab a component from the component menu AND hover over a slot in which its type is not allowed THEN I should see the slot highlighted to indicate that drop is not allowed',
            function() {
                // Act
                dragAndDrop.actions.grabComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.SEARCH_BOX_SLOT);
                var searchBoxSlot = dragAndDrop.elements.getSlotById(slots.SEARCH_BOX_SLOT);
                dragAndDrop.actions.hoverOverSlot(searchBoxSlot);

                // Assert
                expect(dragAndDrop.actions.isSlotEnabled(slots.SEARCH_BOX_SLOT)).toBe(false, 'Expected slot not to display that it allows the component');
                expect(dragAndDrop.actions.isSlotDisabled(slots.SEARCH_BOX_SLOT)).toBe(true, 'Expected slot to display that the component is forbidden');
            });

        it('WHEN I grab a component from the component menu AND hover over a slot in which its type is allowed THEN I should see the slot highlighted to indicate that drop is allowed',
            function() {
                // Act
                dragAndDrop.actions.grabComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
                var bottomHeaderSlot = dragAndDrop.elements.getSlotById(slots.BOTTOM_HEADER_SLOT);
                dragAndDrop.actions.hoverOverSlot(bottomHeaderSlot);

                // Assert
                expect(dragAndDrop.actions.isSlotEnabled(slots.BOTTOM_HEADER_SLOT)).toBe(true, 'Expected slot to display that it allows the component');
                expect(dragAndDrop.actions.isSlotDisabled(slots.BOTTOM_HEADER_SLOT)).toBe(false, 'Expected slot not to display that the component is forbidden');

            });

        it('WHEN I grab a component from the component menu AND hover over an empty slot in which its type is allowed THEN I should be able to drop the component',
            function() {
                // Arrange
                dragAndDrop.actions.moveToStoreFront();

                // Making slot empty.
                storefront.actions.moveToComponent(storefront.constants.FOOTER_SLOT_ID);
                dragAndDrop.actions.grabComponent(slots.FOOTER_SLOT, components.COMPONENT5);
                storefront.actions.moveToComponent(storefront.constants.SEARCH_BOX_SLOT);
                var searchBoxSlot = dragAndDrop.elements.getSlotById(slots.SEARCH_BOX_SLOT);
                dragAndDrop.actions.hoverOverSlot(searchBoxSlot);
                dragAndDrop.actions.dropComponent(components.COMPONENT5, slots.SEARCH_BOX_SLOT);
                dragAndDrop.actions.moveToParent();
                // Act
                dragAndDrop.actions.grabCustomizedComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.FOOTER_SLOT_ID);
                var footerSlot = dragAndDrop.elements.getSlotById(slots.FOOTER_SLOT);
                dragAndDrop.actions.hoverOverSlot(footerSlot);
                dragAndDrop.actions.dropComponentFromComponentMenu(footerSlot);
                // Assert
                expect(dragAndDrop.actions.isComponentInPosition(slots.FOOTER_SLOT, components.COMPONENT5, 0, 1)).toBe(true, 'Expected new component to be at the first position of the footer slot');
            });

        it('WHEN I grab a component from the component menu AND drop it in a slot THEN the component is moved to the new position',
            function() {
                dragAndDrop.actions.grabCustomizedComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);

                dragAndDrop.actions.addComponentToSlotInPosition(slots.BOTTOM_HEADER_SLOT, 10, BOTTOM_HEADER_TOTAL_COMPONENTS);

                // Assert
                expect(dragAndDrop.actions.isComponentInPosition(slots.BOTTOM_HEADER_SLOT, components.COMPONENT10, 9, BOTTOM_HEADER_TOTAL_COMPONENTS + 1)).toBe(true, 'Expected new component to move to the 10nth position of the bottom header slot');
            });

        it('GIVEN I am in a perspective in which drag and drop is enabled WHEN I enable "clone on drop" and  grab a component from the component menu AND hover over a slot in which its type is allowed and drop it THEN I should see an editor modal open with the name pre fixed and saving will close the modal',
            function() {

                // Arrange
                perspective.selectBasicPerspective();

                // Act
                dragAndDrop.actions.enableCloneOnDropAndGrabCustomizedComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();

                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
                dragAndDrop.actions.addComponentToSlotInPosition(slots.BOTTOM_HEADER_SLOT, 5, BOTTOM_HEADER_TOTAL_COMPONENTS);

                // Assert
                expect(editorModal.elements.getAttributeValueByName('name')).toContain('Clone of');
                expect(editorModal.elements.getSaveButton().isEnabled()).toBe(true);

                editorModal.actions.modalDialogClickSave();
                editorModal.assertions.assertModalIsNotPresent();
            });
    });

    describe('scrolling ', function() {
        it('WHEN I grab a component AND I position the mouse in the lower hint THEN the page scrolls down',
            function() {
                dragAndDrop.actions.grabComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                dragAndDrop.actions.getPageVerticalScroll().then(function(oldScroll) {

                    // Act
                    dragAndDrop.actions.hoverOverBottomHint();
                    browser.sleep(1000);

                    // Assert
                    dragAndDrop.actions.getPageVerticalScroll().then(function(newScroll) {
                        expect(newScroll > oldScroll).toBe(true, 'Expected page to scroll down');
                    });
                });
            });

        it('WHEN I grab a component AND I position the mouse in the top hint THEN the page scrolls down',
            function() {
                dragAndDrop.actions.grabComponentInComponentMenu();
                dragAndDrop.actions.moveToStoreFront();
                dragAndDrop.actions.scrollToBottom();
                dragAndDrop.actions.getPageVerticalScroll().then(function(oldScroll) {

                    // Act
                    dragAndDrop.actions.hoverOverTopHint();
                    browser.sleep(1000);

                    // Assert
                    dragAndDrop.actions.getPageVerticalScroll().then(function(newScroll) {
                        expect(newScroll < oldScroll).toBe(true, 'Expected page to scroll up');
                    });
                });
            });

        it('WHEN I grab a component from the component menu AND I position the mouse in the lower hint THEN the page scrolls down',
            function() {
                // Arrange
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
                dragAndDrop.actions.grabComponent(slots.BOTTOM_HEADER_SLOT, components.COMPONENT4);
                dragAndDrop.actions.getPageVerticalScroll().then(function(oldScroll) {

                    // Act
                    dragAndDrop.actions.hoverOverBottomHint();
                    browser.sleep(1000);

                    // Assert
                    dragAndDrop.actions.getPageVerticalScroll().then(function(newScroll) {
                        expect(newScroll > oldScroll).toBe(true, 'Expected page to scroll down');
                    });
                });
            });

        it('and the page is at the bottom WHEN I grab a component from the component menu AND I position the mouse in the upper hint THEN the page scrolls up',
            function() {
                // Arrange
                dragAndDrop.actions.moveToStoreFront();
                storefront.actions.moveToComponent(storefront.constants.STATIC_SLOT_ID);
                dragAndDrop.actions.grabComponent(storefront.constants.STATIC_SLOT_ID, storefront.constants.STATIC_COMPONENT_NAME);
                dragAndDrop.actions.getPageVerticalScroll().then(function(oldScroll) {

                    // Act
                    dragAndDrop.actions.hoverOverTopHint();
                    browser.sleep(1000);

                    // Assert
                    dragAndDrop.actions.getPageVerticalScroll().then(function(newScroll) {
                        expect(newScroll < oldScroll).toBe(true, 'Expected page to scroll up');
                    });
                });
            });
    });

});
