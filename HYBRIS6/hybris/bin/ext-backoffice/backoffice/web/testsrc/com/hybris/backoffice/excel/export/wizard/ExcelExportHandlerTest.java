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
package com.hybris.backoffice.excel.export.wizard;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.ExcelConstants;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.exporting.ExcelExportService;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class ExcelExportHandlerTest
{
	public static final String TEST_FILE_NAME = "TestFileName";
	@Mock
	ExcelExportService excelExportService;
	@InjectMocks
	@Spy
	private ExcelExportHandler handler;
	@Mock
	private FlowActionHandlerAdapter adapter;
	private ExcelExportWizardForm form;
	@Mock
	private Workbook exportedWorkBook;

	@Before
	public void setUp()
	{
		final WidgetInstanceManager wim = CockpitTestUtil.mockWidgetInstanceManager();
		form = new ExcelExportWizardForm();
		form.setTypeCode(ProductModel._TYPECODE);
		wim.getModel().put(ExcelConstants.EXCEL_FORM_PROPERTY, form);
		when(adapter.getWidgetInstanceManager()).thenReturn(wim);
		doNothing().when(handler).triggerDownloading(any(Workbook.class), anyString());
		doReturn(TEST_FILE_NAME).when(handler).getFilename(form);
		when(excelExportService.exportData(anyString(), anyList())).thenReturn(exportedWorkBook);
		when(excelExportService.exportData(anyList(), anyList())).thenReturn(exportedWorkBook);
	}

	@Test
	public void shouldTriggerTypeExportWhenPageableEmpty()
	{
		final List<SelectedAttribute> attributesToExport = mockSelectedAttributes(ProductModel.CODE, ProductModel.NAME);
		final Pageable pageable = mockPageable(null, ProductModel._TYPECODE);
		form.setPageable(pageable);
		form.setTypeCode(ProductModel._TYPECODE);
		form.setAttributes(attributesToExport);

		handler.perform(new CustomType(), adapter, Collections.emptyMap());

		verify(excelExportService).exportData(ProductModel._TYPECODE, attributesToExport);
		verify(handler).triggerDownloading(exportedWorkBook, TEST_FILE_NAME);
		verify(adapter).done();
	}

	@Test
	public void shouldTriggerExportWhenPageableIsNotEmpty()
	{
		final List<SelectedAttribute> attributesToExport = mockSelectedAttributes(ProductModel.CODE, ProductModel.NAME);
		final Pageable pageable = mockPageable(Lists.newArrayList(1, 2), ProductModel._TYPECODE);
		form.setPageable(pageable);
		form.setTypeCode(ProductModel._TYPECODE);
		form.setAttributes(attributesToExport);

		handler.perform(new CustomType(), adapter, Collections.emptyMap());

		verify(excelExportService).exportData(pageable.getAllResults(), attributesToExport);
		verify(handler).triggerDownloading(exportedWorkBook, TEST_FILE_NAME);
		verify(adapter).done();
	}

	@Test
	public void shouldNotTriggerDownloadWhenExportedWorkbookIsEmpty()
	{
		final List<SelectedAttribute> attributesToExport = mockSelectedAttributes(ProductModel.CODE, ProductModel.NAME);
		final Pageable pageable = mockPageable(Lists.newArrayList(1, 2), ProductModel._TYPECODE);
		form.setPageable(pageable);
		form.setTypeCode(ProductModel._TYPECODE);
		form.setAttributes(attributesToExport);
		when(excelExportService.exportData(anyList(), anyList())).thenReturn(null);

		handler.perform(new CustomType(), adapter, Collections.emptyMap());

		verify(excelExportService).exportData(pageable.getAllResults(), attributesToExport);
		verify(handler,never()).triggerDownloading(exportedWorkBook, TEST_FILE_NAME);
		verify(adapter,never()).done();
	}

	private Pageable mockPageable(final List<Object> items, final String typeCode)
	{
		final Pageable pageable = mock(Pageable.class);
		when(pageable.getAllResults()).thenReturn(items);
		when(pageable.getTypeCode()).thenReturn(typeCode);
		when(pageable.getTotalCount()).thenReturn(items != null ? items.size() : 0);
		return pageable;
	}

	private List<SelectedAttribute> mockSelectedAttributes(final String... qualifiers)
	{
		final List<SelectedAttribute> attributes = new ArrayList<>();
		for (final String qualifier : qualifiers)
		{

			final AttributeDescriptorModel ad = mock(AttributeDescriptorModel.class);
			when(ad.getQualifier()).thenReturn(qualifier);
			final SelectedAttribute sa = new SelectedAttribute(ad);
			attributes.add(sa);

		}
		return attributes;
	}
}
