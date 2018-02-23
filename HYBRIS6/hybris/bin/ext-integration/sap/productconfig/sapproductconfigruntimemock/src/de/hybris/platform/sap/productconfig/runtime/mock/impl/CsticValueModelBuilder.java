package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;


/**
 * helper class to build cstic values for Mock implemntations<br>
 * <b>create a new instance for every model object you want to build</b>
 */
@SuppressWarnings("javadoc")
public class CsticValueModelBuilder
{

	private final CsticValueModel csticValue;

	public CsticValueModelBuilder()
	{
		csticValue = new CsticValueModelImpl();
		csticValue.setDomainValue(true);
	}


	public CsticValueModel build()
	{
		return csticValue;
	}


	public CsticValueModelBuilder withName(final String csticName, final String langDependentName)
	{
		csticValue.setName(csticName);
		csticValue.setLanguageDependentName(langDependentName);

		return this;
	}
}
