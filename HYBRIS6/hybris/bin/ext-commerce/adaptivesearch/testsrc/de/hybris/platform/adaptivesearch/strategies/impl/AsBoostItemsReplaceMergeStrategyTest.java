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
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AsBoostItemsReplaceMergeStrategyTest extends AbstractAsBoostRulesMergeStrategyTest
{
	private AsBoostItemsReplaceMergeStrategy mergeStrategy;

	@Before
	public void createMergeStrategy()
	{
		mergeStrategy = new AsBoostItemsReplaceMergeStrategy();
		mergeStrategy.setAsSearchProfileResultFactory(getAsSearchProfileResultFactory());
	}

	@Test
	public void mergePromotedItems()
	{
		// given
		final AsPromotedItem promotedItem1 = new AsPromotedItem();
		promotedItem1.setItemPk(PK_1);
		promotedItem1.setUid(UID_1);

		final AsPromotedItem promotedItem2 = new AsPromotedItem();
		promotedItem2.setItemPk(PK_2);
		promotedItem2.setUid(UID_2);

		getTarget().getPromotedItems().put(promotedItem1.getItemPk(), createConfigurationHolder(promotedItem1));
		getSource().getPromotedItems().put(promotedItem2.getItemPk(), createConfigurationHolder(promotedItem2));

		// when
		mergeStrategy.mergeBoostItems(getSource(), getTarget());

		// then
		assertEquals(1, getTarget().getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = getTarget().getPromotedItems().values()
				.iterator();

		final AsConfigurationHolder<AsPromotedItem> promotedItem2Holder = promotedItemsIterator.next();
		assertSame(promotedItem2, promotedItem2Holder.getConfiguration());
		assertNull(promotedItem2Holder.getReplacedConfigurationUid());
	}

	@Test
	public void mergePromotedItemsWithDuplicates()
	{
		// given
		final AsPromotedItem promotedItem1 = new AsPromotedItem();
		promotedItem1.setItemPk(PK_1);
		promotedItem1.setUid(UID_2);

		final AsPromotedItem promotedItem2 = new AsPromotedItem();
		promotedItem2.setItemPk(PK_2);
		promotedItem2.setUid(UID_3);

		final AsExcludedItem excludedItem1 = new AsExcludedItem();
		excludedItem1.setItemPk(PK_2);
		excludedItem1.setUid(UID_4);

		final AsExcludedItem excludedItem2 = new AsExcludedItem();
		excludedItem2.setItemPk(PK_3);
		excludedItem2.setUid(UID_5);

		getTarget().getExcludedItems().put(excludedItem1.getItemPk(), createConfigurationHolder(excludedItem1));
		getTarget().getExcludedItems().put(excludedItem2.getItemPk(), createConfigurationHolder(excludedItem2));

		getSource().getPromotedItems().put(promotedItem1.getItemPk(), createConfigurationHolder(promotedItem1));
		getSource().getPromotedItems().put(promotedItem2.getItemPk(), createConfigurationHolder(promotedItem2));

		// when
		mergeStrategy.mergeBoostItems(getSource(), getTarget());

		// then
		assertEquals(2, getTarget().getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = getTarget().getPromotedItems().values()
				.iterator();

		final AsConfigurationHolder<AsPromotedItem> promotedItem1Holder = promotedItemsIterator.next();
		assertSame(promotedItem1, promotedItem1Holder.getConfiguration());
		assertNull(promotedItem1Holder.getReplacedConfigurationUid());

		final AsConfigurationHolder<AsPromotedItem> promotedItem2Holder = promotedItemsIterator.next();
		assertSame(promotedItem2, promotedItem2Holder.getConfiguration());
		assertNull(promotedItem2Holder.getReplacedConfigurationUid());

		assertEquals(0, getTarget().getExcludedItems().size());
	}

	@Test
	public void mergeExcludedItems()
	{
		// given
		final AsExcludedItem excludedItem1 = new AsExcludedItem();
		excludedItem1.setItemPk(PK_1);
		excludedItem1.setUid(UID_1);

		final AsExcludedItem excludedItem2 = new AsExcludedItem();
		excludedItem2.setItemPk(PK_2);
		excludedItem2.setUid(UID_2);

		getTarget().getExcludedItems().put(excludedItem1.getItemPk(), createConfigurationHolder(excludedItem1));
		getSource().getExcludedItems().put(excludedItem2.getItemPk(), createConfigurationHolder(excludedItem2));

		// when
		mergeStrategy.mergeBoostItems(getSource(), getTarget());

		// then
		assertEquals(1, getTarget().getExcludedItems().size());
		final Iterator<AsConfigurationHolder<AsExcludedItem>> excludedItemsIterator = getTarget().getExcludedItems().values()
				.iterator();

		final AsConfigurationHolder<AsExcludedItem> excludedItem2Holder = excludedItemsIterator.next();
		assertSame(excludedItem2, excludedItem2Holder.getConfiguration());
		assertNull(excludedItem2Holder.getReplacedConfigurationUid());
	}

	@Test
	public void mergeExcludedItemsWithDuplicates()
	{
		// given
		final AsPromotedItem promotedItem1 = new AsPromotedItem();
		promotedItem1.setItemPk(PK_1);
		promotedItem1.setUid(UID_2);

		final AsPromotedItem promotedItem2 = new AsPromotedItem();
		promotedItem2.setItemPk(PK_2);
		promotedItem2.setUid(UID_3);

		final AsExcludedItem excludedItem1 = new AsExcludedItem();
		excludedItem1.setItemPk(PK_2);
		excludedItem1.setUid(UID_4);

		final AsExcludedItem excludedItem2 = new AsExcludedItem();
		excludedItem2.setItemPk(PK_3);
		excludedItem2.setUid(UID_5);

		getTarget().getPromotedItems().put(promotedItem1.getItemPk(), createConfigurationHolder(promotedItem1));
		getTarget().getPromotedItems().put(promotedItem2.getItemPk(), createConfigurationHolder(promotedItem2));

		getSource().getExcludedItems().put(excludedItem1.getItemPk(), createConfigurationHolder(excludedItem1));
		getSource().getExcludedItems().put(excludedItem2.getItemPk(), createConfigurationHolder(excludedItem2));

		// when
		mergeStrategy.mergeBoostItems(getSource(), getTarget());

		// then
		assertEquals(0, getTarget().getPromotedItems().size());

		assertEquals(2, getTarget().getExcludedItems().size());
		final Iterator<AsConfigurationHolder<AsExcludedItem>> excludedItemsIterator = getTarget().getExcludedItems().values()
				.iterator();

		final AsConfigurationHolder<AsExcludedItem> excludedItem1Holder = excludedItemsIterator.next();
		assertSame(excludedItem1, excludedItem1Holder.getConfiguration());
		assertNull(excludedItem1Holder.getReplacedConfigurationUid());

		final AsConfigurationHolder<AsExcludedItem> excludedItem2Holder = excludedItemsIterator.next();
		assertSame(excludedItem2, excludedItem2Holder.getConfiguration());
		assertNull(excludedItem2Holder.getReplacedConfigurationUid());
	}
}