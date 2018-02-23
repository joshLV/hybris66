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
package de.hybris.platform.b2b.hmc;

import de.hybris.platform.hmc.AbstractEditorMenuChip;
import de.hybris.platform.hmc.AbstractExplorerMenuTreeNodeChip;
import de.hybris.platform.hmc.EditorTabChip;
import de.hybris.platform.hmc.extension.HMCExtension;
import de.hybris.platform.hmc.extension.MenuEntrySlotEntry;
import de.hybris.platform.hmc.generic.ClipChip;
import de.hybris.platform.hmc.generic.ToolbarActionChip;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;
import de.hybris.platform.jalo.Item;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 * Provides necessary meta information about the b2bcommerce hmc extension.
 * 
 * 
 * @version ExtGen v4.1
 */
public class B2BCommerceHMCExtension extends HMCExtension
{
	/** Edit the local|project.properties to change logging behavior (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(B2BCommerceHMCExtension.class.getName());

	/** Path to the resource bundles. */
	public final static String RESOURCE_PATH = "de.hybris.platform.b2b.hmc.b2bcommerce_locales";


	/**
	 * @see HMCExtension#getTreeNodeChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<AbstractExplorerMenuTreeNodeChip> getTreeNodeChips(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getMenuEntrySlotEntries(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<MenuEntrySlotEntry> getMenuEntrySlotEntries(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getSectionChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.generic.ClipChip)
	 */
	@Override
	public List<ClipChip> getSectionChips(final DisplayState displayState, final ClipChip parent)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<EditorTabChip> getEditorTabChips(final DisplayState displayState, final AbstractEditorMenuChip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getToolbarActionChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<ToolbarActionChip> getToolbarActionChips(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public ResourceBundle getLocalizeResourceBundle(final Locale locale)
	{
		return null;
	}

	@Override
	public String getResourcePath()
	{
		return RESOURCE_PATH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.hmc.extension.HMCExtension#beforeSave(de.hybris.platform.jalo.Item,
	 * de.hybris.platform.hmc.webchips.DisplayState, java.util.Map, java.util.Map)
	 */
	@Override
	//FIXME: validate that only admin can modify root unit relation
	public ActionResult beforeSave(final Item item, final DisplayState displayState, final Map currentValues,
			final Map initialValues)
	{
		/*
		 * final ActionResult defaultActionResult = super.beforeSave(item, displayState, currentValues, initialValues);
		 * try {
		 * 
		 * 
		 * if (item instanceof B2BUnit && item.getAttribute(B2BUnit.B2BUNIT) == null && currentValues.get(B2BUnit.B2BUNIT)
		 * != null && !JaloSession.getCurrentSession().getUser().isAdmin()) { currentValues.remove(B2BUnit.B2BUNIT);
		 * 
		 * // this is a root b2bunit node. Check if the parent attribute has been set. final String codeMinSizeDialogError
		 * = Localization.getLocalizedString("action.b2bunitchangerootparent.cannotchange"); return new
		 * ActionResult(ActionResult.FAILED, codeMinSizeDialogError, true);
		 * 
		 * }
		 * 
		 * 
		 * 
		 * } catch (final JaloInvalidParameterException e) { LOG.error(e.getMessage(), e); return new
		 * ActionResult(ActionResult.FAILED, Localization.getLocalizedString("default.save.error"), false); }
		 * 
		 * return defaultActionResult;
		 */
		return super.beforeSave(item, displayState, currentValues, initialValues);
	}
}
