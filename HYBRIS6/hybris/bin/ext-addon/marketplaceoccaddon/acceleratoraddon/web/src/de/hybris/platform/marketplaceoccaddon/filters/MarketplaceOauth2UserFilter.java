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
package de.hybris.platform.marketplaceoccaddon.filters;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Filter to add current user into session.
 */
public class MarketplaceOauth2UserFilter implements Filter
{

	private static final String ROLE_VENDORADMINISTRATORGROUP = "ROLE_VENDORADMINISTRATORGROUP";
	private static final String ROLE_VENDORPRODUCTMANAGERGROUP = "ROLE_VENDORPRODUCTMANAGERGROUP";
	private static final String ROLE_VENDORWAREHOUSESTAFFGROUP = "ROLE_VENDORWAREHOUSESTAFFGROUP";
	private static final String ROLE_VENDORCONTENTMANAGERGROUP = "ROLE_VENDORCONTENTMANAGERGROUP";

	@Resource(name = "userService")
	private UserService userService;


	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (containsRole(auth, ROLE_VENDORADMINISTRATORGROUP) || containsRole(auth, ROLE_VENDORPRODUCTMANAGERGROUP)
				|| containsRole(auth, ROLE_VENDORWAREHOUSESTAFFGROUP) || containsRole(auth, ROLE_VENDORCONTENTMANAGERGROUP))
		{
			final UserModel userModel = getUserService().getUserForUID((String) auth.getPrincipal());
			getUserService().setCurrentUser(userModel);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void destroy()
	{
		// YTODO Auto-generated method stub
	}

	protected boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	protected UserService getUserService()
	{
		return userService;
	}

}
