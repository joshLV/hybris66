package de.hybris.platform.assistedservicewebservices.controllers;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customer.CustomerListFacade;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import javax.annotation.Resource;


public abstract class AbstractAssistedServiceWebServiceController
{
	@Resource(name = "customerListFacade")
	private CustomerListFacade customerListFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	protected <T> SearchPageData<T> createSearchPageData(final List<T> entries, final PaginationData paginationData)
	{
		final SearchPageData<T> customerSearchPageData = new SearchPageData<>();
		customerSearchPageData.setResults(entries);
		customerSearchPageData.setPagination(paginationData);
		return customerSearchPageData;
	}

	public CustomerListFacade getCustomerListFacade()
	{
		return customerListFacade;
	}

	public CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}
}
