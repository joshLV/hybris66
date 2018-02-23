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
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2lib.model.components.FlashComponentModel;
import de.hybris.platform.cmsfacades.util.builder.FlashComponentModelBuilder;
import de.hybris.platform.cmsfacades.util.dao.impl.FlashComponentDao;

import org.springframework.beans.factory.annotation.Required;


public class FlashComponentModelMother extends AbstractModelMother<FlashComponentModel>
{
	public static final String UID_HEADER = "uid-test-component-header";
	public static final String NAME_HEADER = "name-test-component-header";
	public static final String URL_LINK_HEADER = "url-link-test-component-header";

	private FlashComponentDao flashComponentDao;

	public FlashComponentModel createHeaderFlashComponentModel(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getFlashComponentDao().getByUidAndCatalogVersion(UID_HEADER, catalogVersion), //
				() -> FlashComponentModelBuilder.aModel() //
						.withUid(UID_HEADER) //
						.withCatalogVersion(catalogVersion) //
						.isVisible(Boolean.TRUE) //
						.withUrlLink(URL_LINK_HEADER)
						.withExternal(false)
						.withName(NAME_HEADER) //
						.build());
	}

	protected FlashComponentDao getFlashComponentDao()
	{
		return flashComponentDao;
	}

	@Required
	public void setFlashComponentDao(final FlashComponentDao flashComponentDao)
	{
		this.flashComponentDao = flashComponentDao;
	}

}
