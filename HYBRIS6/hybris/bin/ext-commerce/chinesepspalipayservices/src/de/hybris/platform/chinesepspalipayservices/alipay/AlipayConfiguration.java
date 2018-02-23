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
package de.hybris.platform.chinesepspalipayservices.alipay;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.site.BaseSiteService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.util.UriComponentsBuilder;


public class AlipayConfiguration
{

	private static final String PRICE_FORMAT = "%.2f";

	private String webPartner;
	private String webKey;
	private String webSellerEmail;
	private String webGateway;
	private String wapPartner;
	private String wapKey;
	private String wapSeller;
	private String wapRsaPrivate;
	private String wapRsaAlipayPublic;
	private String wapGateway;

	private String requestTimeout;

	private String requestSubject;

	private String httpsVerifyUrl;
	private String refundBatchNoTimezone;
	private String alipayTimezone;

	private String signType;

	private String wapAuthServiceApiName;
	private String wapTradeDirectApiName;
	private String directPayServiceApiName;
	private String directayPaymethodName;
	private String expressPaymethodName;
	private String closeTradeServiceApiName;
	private String checkTradeServiceApiName;
	private String refundServiceApiName;

	private String webSellerId;
	private String refundReason;

	private BaseSiteService baseSiteService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;


	public String getRequestPrice(final double orderPrice)
	{
		validateParameterNotNull(Double.valueOf(orderPrice), "The given orderPrice is null!");

		return String.format(PRICE_FORMAT, Double.valueOf(orderPrice));
	}

	public String getRefundPrice(final double orderPrice)
	{
		validateParameterNotNull(Double.valueOf(orderPrice), "The given orderPrice is null!");

		return String.format(PRICE_FORMAT, Double.valueOf(orderPrice));
	}


	/**
	 * @return the webPartner
	 */
	public String getWebPartner()
	{
		return webPartner;
	}

	/**
	 * @param webPartner
	 *           the webPartner to set
	 */
	public void setWebPartner(final String webPartner)
	{
		this.webPartner = webPartner;
	}

	/**
	 * @return the webKey
	 */
	public String getWebKey()
	{
		return webKey;
	}

	/**
	 * @param webKey
	 *           the webKey to set
	 */
	public void setWebKey(final String webKey)
	{
		this.webKey = webKey;
	}

	/**
	 * @return the webSellerEmail
	 */
	public String getWebSellerEmail()
	{
		return webSellerEmail;
	}

	/**
	 * @param webSellerEmail
	 *           the webSellerEmail to set
	 */
	public void setWebSellerEmail(final String webSellerEmail)
	{
		this.webSellerEmail = webSellerEmail;
	}

	/**
	 * @return the webGateway
	 */
	public String getWebGateway()
	{
		if (webGateway.startsWith("/"))
		{
			final String siteBaseUrl = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(
					getBaseSiteService().getCurrentBaseSite(), true, "/");
			return String.valueOf(UriComponentsBuilder.fromHttpUrl(siteBaseUrl).replacePath(webGateway).build().toUri()) + "?";
		}
		return webGateway;
	}

	protected String getConfiguredWebGateway()
	{
		return webGateway;
	}

	/**
	 * @param webGateway
	 *           the webGateway to set
	 */
	public void setWebGateway(final String webGateway)
	{
		this.webGateway = webGateway;
	}

	/**
	 * @return the wapPartner
	 */
	public String getWapPartner()
	{
		return wapPartner;
	}

	/**
	 * @param wapPartner
	 *           the wapPartner to set
	 */
	public void setWapPartner(final String wapPartner)
	{
		this.wapPartner = wapPartner;
	}

	/**
	 * @return the wapKey
	 */
	public String getWapKey()
	{
		return wapKey;
	}

	/**
	 * @param wapKey
	 *           the wapKey to set
	 */
	public void setWapKey(final String wapKey)
	{
		this.wapKey = wapKey;
	}

	/**
	 * @return the wapSeller
	 */
	public String getWapSeller()
	{
		return wapSeller;
	}

	/**
	 * @param wapSeller
	 *           the wapSeller to set
	 */
	public void setWapSeller(final String wapSeller)
	{
		this.wapSeller = wapSeller;
	}

	/**
	 * @return the wapRsaPrivate
	 */
	public String getWapRsaPrivate()
	{
		return wapRsaPrivate;
	}

	/**
	 * @param wapRsaPrivate
	 *           the wapRsaPrivate to set
	 */
	public void setWapRsaPrivate(final String wapRsaPrivate)
	{
		this.wapRsaPrivate = wapRsaPrivate;
	}

	/**
	 * @return the wapRsaAlipayPublic
	 */
	public String getWapRsaAlipayPublic()
	{
		return wapRsaAlipayPublic;
	}

	/**
	 * @param wapRsaAlipayPublic
	 *           the wapRsaAlipayPublic to set
	 */
	public void setWapRsaAlipayPublic(final String wapRsaAlipayPublic)
	{
		this.wapRsaAlipayPublic = wapRsaAlipayPublic;
	}

	/**
	 * @return the wapGateway
	 */
	public String getWapGateway()
	{
		return wapGateway;
	}

	/**
	 * @param wapGateway
	 *           the wapGateway to set
	 */
	public void setWapGateway(final String wapGateway)
	{
		this.wapGateway = wapGateway;
	}

	/**
	 * @return the requestTimeout
	 */
	public String getRequestTimeout()
	{
		return requestTimeout;
	}

	/**
	 * @param requestTimeout
	 *           the requestTimeout to set
	 */
	public void setRequestTimeout(final String requestTimeout)
	{
		this.requestTimeout = requestTimeout;
	}

	/**
	 * @return the requestSubject
	 */
	public String getRequestSubject()
	{
		return requestSubject;
	}

	/**
	 * @param requestSubject
	 *           the requestSubject to set
	 */
	public void setRequestSubject(final String requestSubject)
	{
		this.requestSubject = requestSubject;
	}

	/**
	 * @return the httpsVerifyUrl
	 */
	public String getHttpsVerifyUrl()
	{
		if (httpsVerifyUrl.startsWith("/"))
		{
			final String siteBaseUrl = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(
					getBaseSiteService().getCurrentBaseSite(), true, "/");
			return String.valueOf(UriComponentsBuilder.fromHttpUrl(siteBaseUrl).replacePath(httpsVerifyUrl).build().toUri()) + "?";
		}
		return httpsVerifyUrl;
	}

	protected String getConfiguredVerifyUrl()
	{
		return httpsVerifyUrl;
	}

	/**
	 * @param httpsVerifyUrl
	 *           the httpsVerifyUrl to set
	 */
	public void setHttpsVerifyUrl(final String httpsVerifyUrl)
	{
		this.httpsVerifyUrl = httpsVerifyUrl;
	}

	/**
	 * @return the refundBatchNoTimezone
	 */
	public String getRefundBatchNoTimezone()
	{
		return refundBatchNoTimezone;
	}

	/**
	 * @param refundBatchNoTimezone
	 *           the refundBatchNoTimezone to set
	 */
	public void setRefundBatchNoTimezone(final String refundBatchNoTimezone)
	{
		this.refundBatchNoTimezone = refundBatchNoTimezone;
	}

	/**
	 * @return the alipayTimezone
	 */
	public String getAlipayTimezone()
	{
		return alipayTimezone;
	}

	/**
	 * @param alipayTimezone
	 *           the alipayTimezone to set
	 */
	public void setAlipayTimezone(final String alipayTimezone)
	{
		this.alipayTimezone = alipayTimezone;
	}

	/**
	 * @return the signType
	 */
	public String getSignType()
	{
		return signType;
	}

	/**
	 * @param signType
	 *           the signType to set
	 */
	public void setSignType(final String signType)
	{
		this.signType = signType;
	}

	/**
	 * @return the wapAuthServiceApiName
	 */
	public String getWapAuthServiceApiName()
	{
		return wapAuthServiceApiName;
	}

	/**
	 * @param wapAuthServiceApiName
	 *           the wapAuthServiceApiName to set
	 */
	public void setWapAuthServiceApiName(final String wapAuthServiceApiName)
	{
		this.wapAuthServiceApiName = wapAuthServiceApiName;
	}

	/**
	 * @return the wapTradeDirectApiName
	 */
	public String getWapTradeDirectApiName()
	{
		return wapTradeDirectApiName;
	}

	/**
	 * @param wapTradeDirectApiName
	 *           the wapTradeDirectApiName to set
	 */
	public void setWapTradeDirectApiName(final String wapTradeDirectApiName)
	{
		this.wapTradeDirectApiName = wapTradeDirectApiName;
	}

	/**
	 * @return the directPayServiceApiName
	 */
	public String getDirectPayServiceApiName()
	{
		return directPayServiceApiName;
	}

	/**
	 * @param directPayServiceApiName
	 *           the directPayServiceApiName to set
	 */
	public void setDirectPayServiceApiName(final String directPayServiceApiName)
	{
		this.directPayServiceApiName = directPayServiceApiName;
	}

	/**
	 * @return the directayPaymethodName
	 */
	public String getDirectayPaymethodName()
	{
		return directayPaymethodName;
	}

	/**
	 * @param directayPaymethodName
	 *           the directayPaymethodName to set
	 */
	public void setDirectayPaymethodName(final String directayPaymethodName)
	{
		this.directayPaymethodName = directayPaymethodName;
	}

	/**
	 * @return the expressPaymethodName
	 */
	public String getExpressPaymethodName()
	{
		return expressPaymethodName;
	}

	/**
	 * @param expressPaymethodName
	 *           the expressPaymethodName to set
	 */
	public void setExpressPaymethodName(final String expressPaymethodName)
	{
		this.expressPaymethodName = expressPaymethodName;
	}

	/**
	 * @return the closeTradeServiceApiName
	 */
	public String getCloseTradeServiceApiName()
	{
		return closeTradeServiceApiName;
	}

	/**
	 * @param closeTradeServiceApiName
	 *           the closeTradeServiceApiName to set
	 */
	public void setCloseTradeServiceApiName(final String closeTradeServiceApiName)
	{
		this.closeTradeServiceApiName = closeTradeServiceApiName;
	}

	/**
	 * @return the checkTradeServiceApiName
	 */
	public String getCheckTradeServiceApiName()
	{
		return checkTradeServiceApiName;
	}

	/**
	 * @param checkTradeServiceApiName
	 *           the checkTradeServiceApiName to set
	 */
	public void setCheckTradeServiceApiName(final String checkTradeServiceApiName)
	{
		this.checkTradeServiceApiName = checkTradeServiceApiName;
	}

	/**
	 * @return the refundServiceApiName
	 */
	public String getRefundServiceApiName()
	{
		return refundServiceApiName;
	}

	/**
	 * @param refundServiceApiName
	 *           the refundServiceApiName to set
	 */
	public void setRefundServiceApiName(final String refundServiceApiName)
	{
		this.refundServiceApiName = refundServiceApiName;
	}

	public String getWebSellerId()
	{
		return webSellerId;
	}

	public void setWebSellerId(final String webSellerId)
	{
		this.webSellerId = webSellerId;
	}

	public String getRefundReason()
	{
		return refundReason;
	}

	public void setRefundReason(final String refundReason)
	{
		this.refundReason = refundReason;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

}