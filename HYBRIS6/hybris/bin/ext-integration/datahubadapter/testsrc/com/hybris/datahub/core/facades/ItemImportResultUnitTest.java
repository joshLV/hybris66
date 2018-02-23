/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.datahub.core.facades;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

/**
 * A unit test for <code>ItemImportResult</code>
 */
@UnitTest
@SuppressWarnings("javadoc")
public class ItemImportResultUnitTest
{
	private ItemImportResult importResult;

	@Before
	public void setUp()
	{
		importResult = new ItemImportResult();
	}

	@Test
	public void testSuccesfulByDefault()
	{
		assertThat(importResult.isSuccessful()).isTrue();
	}

	@Test
	public void testDoesNotContainImportErrorsBeforeTheyAdded()
	{
		final Collection<ImportError> rejected = importResult.getErrors();
		assertThat(rejected).isNotNull();
		assertThat(rejected).isEmpty();
	}

	@Test
	public void testAllAddedErrorsCanBeReadBack()
	{
		final ImportError err1 = ImportTestUtils.error("Missing attribute");
		final ImportError err2 = ImportTestUtils.error("Unresolved attribute");
		importResult.addErrors(Arrays.asList(err1, err2));

		final Collection<ImportError> errors = importResult.getErrors();

		assertThat(errors).hasSize(2)
						  .contains(err1, err2);
	}

	@Test
	public void testSameResultIsReturnedAfterAddingAnErrorToIt()
	{
		final ItemImportResult orig = new ItemImportResult();
		final ItemImportResult returned = orig.addErrors(ImportTestUtils.errors("some error"));

		assertThat(returned).isEqualTo(orig);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddErrorsDoesNotExpectNullBePassedForTheErrorCollection()
	{
		new ItemImportResult().addErrors(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testImportErrorCollectionCannotBeModifiedFromOutside()
	{
		final ImportError errorBeingAddedAroundItemImportResult = ImportTestUtils.error("Item 1 is rejected");
		importResult.getErrors().add(errorBeingAddedAroundItemImportResult);
	}

	@Test
	public void testResultIsUnsuccessfulWhenAtLeastOneImportErrorWasAdded()
	{
		importResult.addErrors(ImportTestUtils.errors("Some problem"));

		assertThat(importResult.isSuccessful()).isFalse();
	}

	@Test
	public void testReportedExceptionCanBeReadBack()
	{
		final Exception ex = new Exception("Import exception has occurred");

		final ItemImportResult res = new ItemImportResult(ex);
		assertThat(res.getExceptionMessage()).isEqualTo(ex.getMessage());
	}

	@Test
	public void testReportedExceptionWithoutMessageCanBeReadBack()
	{
		final Exception ex = new Exception();

		final ItemImportResult res = new ItemImportResult(ex);
		assertThat(res.getExceptionMessage()).isEqualTo(ex.getClass().getCanonicalName());
	}

	@Test
	public void toStringContainsSUCCESS_whenResultIsSuccessful()
	{
		final ItemImportResult res = new ItemImportResult();
		assert res.isSuccessful() : "Result with no errors or exception should be successful";

		assertThat(res.toString()).contains("SUCCESS");
	}

	@Test
	public void toStringContainsERROR_whenResultIsNotSuccessful()
	{
		final ItemImportResult res = new ItemImportResult().addErrors(ImportTestUtils.errors("an error"));
		assert !res.isSuccessful() : "Result with errors should be unsuccessful";

		assertThat(res.toString()).contains("ERROR");
	}

	@Test
	public void toStringPrintsOutExceptionWhenItIsPresentInTheResult()
	{
		final String res = new ItemImportResult((new IOException("cannot read file"))).toString();

		assertThat(res).contains("ERROR")
					   .contains("cannot read file");
	}
}
