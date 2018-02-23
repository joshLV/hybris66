/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.backoffice.cockpitng.dnd;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Div;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.validation.BackofficeValidationService;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dnd.DefaultDragAndDropContext;
import com.hybris.cockpitng.dnd.DragAndDropContext;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.dnd.DropHandler;
import com.hybris.cockpitng.dnd.DropOperationData;
import com.hybris.cockpitng.dnd.DropOperationValidationData;
import com.hybris.cockpitng.dnd.SelectionSupplier;
import com.hybris.cockpitng.mouse.MouseKeys;
import com.hybris.cockpitng.validation.ValidationContext;


public class DefaultDragAndDropStrategyTest
{

	public static final String STRING_TYPE = "string";
	public static final String TARGET_OBJECT = "target";
	public static final String DRAGGED_OBJECT = "dragged";

	@Mock
	private TypeFacade typeFacade;
	@InjectMocks
	private DefaultDragAndDropStrategy strategy;
	@Mock
	private BackofficeValidationService validationService;
	@Mock
	private ObjectFacade objectFacade;

	@Before
	public void init() throws TypeNotFoundException
	{
		MockitoAnnotations.initMocks(this);

		when(typeFacade.load("A")).thenReturn(
				new DataType.Builder("A").subtype("B").subtype("C").type(DataType.Type.COMPOUND).build());
		when(typeFacade.load("B")).thenReturn(new DataType.Builder("B").supertype("A").type(DataType.Type.COMPOUND).build());
		when(typeFacade.load("C")).thenReturn(
				new DataType.Builder("C").supertype("A").subtype("D").subtype("E").type(DataType.Type.COMPOUND).abstractType(true)
						.build());
		when(typeFacade.load("D")).thenReturn(new DataType.Builder("D").supertype("C").type(DataType.Type.COMPOUND).build());
		when(typeFacade.load("E")).thenReturn(new DataType.Builder("E").supertype("C").type(DataType.Type.COMPOUND).build());
	}

	@Test
	public void testResolveDroppablesExactType()
	{
		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A!"));

		final String droppables = strategy.resolveDroppables(handler);

		assertThat(droppables).isEqualTo("A");
	}

	@Test
	public void testResolveDroppablesHierarchy()
	{
		final DropHandler handler = mock(DropHandler.class);

		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A"));

		final String droppables = strategy.resolveDroppables(handler);

		assertThat(droppables).isEqualTo("A,B,D,E");


		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("C"));

		final String droppablesC = strategy.resolveDroppables(handler);

		assertThat(droppablesC).isEqualTo("D,E");


		when(handler.findSupportedTypes()).thenReturn(Arrays.asList("D", "E"));

		final String droppablesDE = strategy.resolveDroppables(handler);

		assertThat(droppablesDE).isEqualTo("D,E");


		when(handler.findSupportedTypes()).thenReturn(Arrays.asList("B", "C"));

		final String droppablesBC = strategy.resolveDroppables(handler);

		assertThat(droppablesBC).isEqualTo("B,D,E");
	}

	@Test
	public void testResolveDroppablesHierarchyLimited()
	{
		final DropHandler handler = mock(DropHandler.class);

		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A"));
		strategy.setSubtypeLimit(3);

		final String droppables = strategy.resolveDroppables(handler);

		assertThat(droppables).isEqualTo("A,B,D");
	}

	@Test
	public void testResolveDroppablesGarbageIn()
	{
		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.emptyList());

		final String droppablesEmpty = strategy.resolveDroppables(handler);

		assertThat(droppablesEmpty).isEqualTo("true");


		when(handler.findSupportedTypes()).thenReturn(null);

		final String droppablesNull = strategy.resolveDroppables(handler);

		assertThat(droppablesNull).isEqualTo("true");


		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("X"));

		final String droppablesX = strategy.resolveDroppables(handler);

		assertThat(droppablesX).isEqualTo("true");
	}

	@Test
	public void testHandleDropSingleSelection()
	{
		final DropEvent dropEvent = createDropEvent(null, 0);

		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A"));

		final DragAndDropContext context = new DefaultDragAndDropContext.Builder().build();

		strategy.handleDrop(dropEvent, handler, context);

		verify(handler).handleDrop(anyListOf(DRAGGED_OBJECT.getClass()), eq(TARGET_OBJECT), any(DragAndDropContext.class));
	}

	@Test
	public void testHandleDropMultiSelection()
	{
		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A"));

		final DragAndDropContext context = new DefaultDragAndDropContext.Builder().build();

		final List<String> multiselection = Arrays.asList("dragged1", "dragged2");

		final DropEvent dropEvent = createDropEvent(multiselection, 0);

		strategy.handleDrop(dropEvent, handler, context);

		verify(handler).handleDrop(anyListOf(DRAGGED_OBJECT.getClass()), eq(TARGET_OBJECT), any(DragAndDropContext.class));
	}

	private DropEvent createDropEvent(final List multiselection, final int keys)
	{
		final Div draggedDiv = mock(Div.class);
		when(draggedDiv.getAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, true)).thenReturn(DRAGGED_OBJECT);

		final Div targetDiv = mock(Div.class);
		when(targetDiv.getAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, true)).thenReturn(TARGET_OBJECT);

		if (multiselection != null)
		{
			when(draggedDiv.getAttribute(DragAndDropStrategy.ATTRIBUTE_DND_SELECTION_SUPPLIER, true)).thenReturn(
					(SelectionSupplier) () -> multiselection);
		}

		return new DropEvent("event", targetDiv, draggedDiv, 0, 0, 0, 0, keys);
	}

	@Test
	public void testMakeDroppable()
	{
		when(typeFacade.getType(TARGET_OBJECT)).thenReturn(STRING_TYPE);

		final Div targetDiv = mock(Div.class);
		when(targetDiv.getAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, true)).thenReturn(TARGET_OBJECT);

		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A!"));

		final Map<String, DropHandler> map = new HashMap<>();
		map.put(STRING_TYPE, handler);
		strategy.setHandlerMap(map);

		strategy.makeDroppable(targetDiv, TARGET_OBJECT, new DefaultCockpitContext());

		verify(targetDiv).setAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, TARGET_OBJECT);
		verify(handler).findSupportedTypes();
		verify(targetDiv).addEventListener(eq(Events.ON_DROP), any());
		verify(targetDiv).setDroppable("A");
		verifyNoMoreInteractions(targetDiv);
	}

	@Test
	public void testDoNotMakeDroppableIfHandlersListIsEmpty()
	{
		when(typeFacade.getType(TARGET_OBJECT)).thenReturn(STRING_TYPE);

		final Div targetDiv = mock(Div.class);
		when(targetDiv.getAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, true)).thenReturn(TARGET_OBJECT);

		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A!"));

		strategy.setHandlerMap(Collections.emptyMap());

		strategy.makeDroppable(targetDiv, TARGET_OBJECT, new DefaultCockpitContext());

		verify(targetDiv,never()).setAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, TARGET_OBJECT);
		verify(handler,never()).findSupportedTypes();
		verify(targetDiv,never()).addEventListener(eq(Events.ON_DROP), any());
		verify(targetDiv,never()).setDroppable("A");
		verifyNoMoreInteractions(targetDiv);
	}




	@Test
	public void testMakeDraggable()
	{
		when(typeFacade.getType(DRAGGED_OBJECT)).thenReturn(STRING_TYPE);

		final Div draggedDiv = mock(Div.class);

		final DefaultCockpitContext dragContext = new DefaultCockpitContext();
		final SelectionSupplier selectionSupplier = Collections::emptyList;
		strategy.makeDraggable(draggedDiv, DRAGGED_OBJECT, dragContext, selectionSupplier);

		verify(draggedDiv).setAttribute(DragAndDropStrategy.ATTRIBUTE_DND_DATA, DRAGGED_OBJECT);
		verify(draggedDiv).setAttribute(DragAndDropStrategy.ATTRIBUTE_DND_SELECTION_SUPPLIER, selectionSupplier);
		verify(draggedDiv).setAttribute(DefaultDragAndDropStrategy.ATTRIBUTE_DND_DRAG_CONTEXT, dragContext);
		verify(draggedDiv).setDraggable(STRING_TYPE);
		verifyNoMoreInteractions(draggedDiv);
	}

	@Test
	public void testResolveHandledSubtypes()
	{
		final DropHandler handler = mock(DropHandler.class);

		Map<String, DropHandler> map = new HashMap<>();
		map.put("A", handler);
		strategy.setHandlerMap(map);

		Map<String, DropHandler> resolvedMap = strategy.resolveHandledSubtypes();

		assertThat(resolvedMap.get("A")).isEqualTo(handler);
		assertThat(resolvedMap.get("B")).isEqualTo(handler);
		assertThat(resolvedMap.get("D")).isEqualTo(handler);
		assertThat(resolvedMap.get("E")).isEqualTo(handler);

		map = new HashMap<>();
		map.put("A!", handler);
		strategy.setHandlerMap(map);

		resolvedMap = strategy.resolveHandledSubtypes();

		assertThat(resolvedMap.get("A")).isEqualTo(handler);
		assertThat(resolvedMap.containsKey("B")).isFalse();
		assertThat(resolvedMap.containsKey("D")).isFalse();
		assertThat(resolvedMap.containsKey("E")).isFalse();
	}

	@Test
	public void testContextParamsPassed()
	{
		final DropEvent dropEvent = createDropEvent(null, MouseEvent.ALT_KEY | MouseEvent.LEFT_CLICK);

		final DropHandler handler = mock(DropHandler.class);
		when(handler.findSupportedTypes()).thenReturn(Collections.singletonList("A"));

		final DragAndDropContext context = new DefaultDragAndDropContext.Builder().build();
		context.setParameter("x", "y");

		strategy.handleDrop(dropEvent, handler, context);

		final ArgumentCaptor<DragAndDropContext> captor = ArgumentCaptor.forClass(DragAndDropContext.class);
		verify(handler).handleDrop(anyList(), eq(TARGET_OBJECT), captor.capture());

		assertThat(captor.getValue().getTargetContext().getParameter("x")).isEqualTo("y");
		assertThat(captor.getValue().getKeys()).contains(MouseKeys.ALT_KEY, MouseKeys.LEFT_CLICK);
	}

	@Test
	public void testValidation() throws Exception
	{
		// given
		final DropEvent dropEvent = createDropEvent(null, MouseEvent.ALT_KEY | MouseEvent.LEFT_CLICK);
		final DropHandler handler = mock(DropHandler.class);

		final List<DropOperationData> operationsDatas = Arrays.asList(new DropOperationData(DRAGGED_OBJECT, TARGET_OBJECT,
				DRAGGED_OBJECT, new DefaultDragAndDropContext.Builder().build(), "success_key"));

		final ObjectFacadeOperationResult objectFacadeOperationResult = new ObjectFacadeOperationResult();
		objectFacadeOperationResult.addSuccessfulObject(DRAGGED_OBJECT);

		when(handler.handleDrop(anyList(), eq(TARGET_OBJECT), any(DragAndDropContext.class))).thenReturn(operationsDatas);
		when(objectFacade.save(anyCollection(), any(Context.class))).thenReturn(objectFacadeOperationResult);

		// when
		strategy.handleDrop(dropEvent, handler, new DefaultDragAndDropContext.Builder().build());

		// then
		verify(validationService).validate(eq(DRAGGED_OBJECT), any(ValidationContext.class));
	}

	@Test
	public void testSystemValidationNotRun() throws Exception
	{
		// given
		final DropEvent dropEvent = createDropEvent(null, MouseEvent.ALT_KEY | MouseEvent.LEFT_CLICK);
		final DropHandler handler = mock(DropHandler.class);

		final List<DropOperationData> operationsDatas = Arrays.asList(new DropOperationData(DRAGGED_OBJECT, TARGET_OBJECT,
				DRAGGED_OBJECT, new DefaultDragAndDropContext.Builder().build(), "success_key"));

		final ObjectFacadeOperationResult objectFacadeOperationResult = new ObjectFacadeOperationResult();
		objectFacadeOperationResult.addSuccessfulObject(DRAGGED_OBJECT);

		when(handler.handleDrop(anyList(), eq(TARGET_OBJECT), any(DragAndDropContext.class))).thenReturn(operationsDatas);
		when(objectFacade.save(anyCollection(), any(Context.class))).thenReturn(objectFacadeOperationResult);

		strategy.setPerformSystemValidation(false);

		// when
		strategy.handleDrop(dropEvent, handler, new DefaultDragAndDropContext.Builder().build());

		// then
		verify(validationService,never()).validate(eq(DRAGGED_OBJECT), any(ValidationContext.class));
	}

	@Test
	public void shouldFindElementsWithoutErrorsAndWarnings()
	{
		// given
		final String elementWithoutErrors = "elementWithoutErrors";
		final String elementWithError = "elementWithError";
		final String elementWithWarnings = "elementWithWarning";
		final DropOperationData elementWithoutErrorsData = prepareDropOperationData(elementWithoutErrors);
		final DropOperationData elementWithErrorData = prepareDropOperationData(elementWithError);
		final DropOperationData elementWithWarningsData = prepareDropOperationData(elementWithWarnings);
		final List<DropOperationData> allElements = Arrays.asList(elementWithoutErrorsData, elementWithErrorData,
				elementWithWarningsData);
		final List<DropOperationValidationData> validationData = Arrays.asList(
				prepareOperationValidationData(elementWithErrorData), prepareOperationValidationData(elementWithWarningsData));

		// when
		final List<DropOperationData> itemsWithoutErrorsAndWarnings = strategy.findItemsWithoutErrorsAndWarnings(allElements, validationData);

		// then
		assertThat(itemsWithoutErrorsAndWarnings).hasSize(1);
		assertThat(itemsWithoutErrorsAndWarnings).contains(elementWithoutErrorsData);
	}

	private DropOperationData prepareDropOperationData(final Object data)
	{
		return new DropOperationData(data, TARGET_OBJECT, new DefaultDragAndDropContext.Builder().build());
	}

	private DropOperationValidationData prepareOperationValidationData(final DropOperationData operationData)
	{
		return new DropOperationValidationData(operationData, new HashedMap());
	}
}
