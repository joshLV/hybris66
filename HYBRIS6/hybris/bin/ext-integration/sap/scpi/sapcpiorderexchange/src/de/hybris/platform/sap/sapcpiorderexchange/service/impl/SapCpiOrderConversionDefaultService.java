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
package de.hybris.platform.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.*;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import de.hybris.platform.sap.sapcpiadapter.data.*;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderConversionService;
import de.hybris.platform.sap.sapmodel.model.SAPLogicalSystemModel;
import de.hybris.platform.sap.sapmodel.services.SapPlantLogSysOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SapCpiOrderConversionDefaultService implements SapCpiOrderConversionService {

    private static final Logger LOG = LoggerFactory.getLogger(SapCpiOrderConversionDefaultService.class);

    private RawItemContributor<OrderModel> sapOrderContributor;
    private RawItemContributor<OrderModel> sapPaymentContributor;
    private RawItemContributor<OrderModel> sapPartnerContributor;
    private RawItemContributor<OrderModel> sapOrderEntryContributor;
    private RawItemContributor<OrderModel> sapSalesConditionsContributor;
    private SapPlantLogSysOrgService sapPlantLogSysOrgService;

    @Override
    public SapCpiOrder convertHybrisOrderToSapCpiOrder(OrderModel orderModel) {

        SapCpiOrder sapCpiOrder = new SapCpiOrder();

        sapOrderContributor.createRows(orderModel).stream().findFirst().ifPresent(row -> {

            sapCpiOrder.setSapCpiConfig(mapOrderConfigInfo(orderModel));

            sapCpiOrder.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrder.setBaseStoreUid(mapAttribute(OrderCsvColumns.BASE_STORE, row));
            sapCpiOrder.setCreationDate(mapDateAttribute(OrderCsvColumns.DATE, row));
            sapCpiOrder.setCurrencyIsoCode(mapAttribute(OrderCsvColumns.ORDER_CURRENCY_ISO_CODE, row));
            sapCpiOrder.setPaymentMode(mapAttribute(OrderCsvColumns.PAYMENT_MODE, row));
            sapCpiOrder.setReceiverName(mapAttribute(OrderCsvColumns.LOGICAL_SYSTEM, row));
            sapCpiOrder.setDeliveryMode(mapAttribute(OrderCsvColumns.DELIVERY_MODE, row));
            sapCpiOrder.setSalesOrganization(mapAttribute(OrderCsvColumns.SALES_ORGANIZATION, row));
            sapCpiOrder.setDistributionChannel(mapAttribute(OrderCsvColumns.DISTRIBUTION_CHANNEL, row));
            sapCpiOrder.setDivision(mapAttribute(OrderCsvColumns.DIVISION, row));
            sapCpiOrder.setChannel(mapAttribute(OrderCsvColumns.CHANNEL, row));
            sapCpiOrder.setPurchaseOrderNumber(mapAttribute(OrderCsvColumns.PURCHASE_ORDER_NUMBER, row));
            sapCpiOrder.setTransactionType(orderModel.getStore().getSAPConfiguration().getSapcommon_transactionType());

            orderModel.getStore()
                    .getSAPConfiguration()
                    .getSapDeliveryModes()
                    .stream()
                    .filter(entry -> entry.getDeliveryMode().getCode().contentEquals(orderModel.getDeliveryMode().getCode()))
                    .findFirst()
                    .ifPresent(entry -> sapCpiOrder.setShippingCondition(entry.getDeliveryValue()));

            sapCpiOrder.setSapCpiOrderItems(mapOrderItems(orderModel));
            sapCpiOrder.setSapCpiPartnerRoles(mapOrderPartners(orderModel));
            sapCpiOrder.setSapCpiOrderAddresses(mapOrderAddresses(orderModel));
            sapCpiOrder.setSapCpiOrderPriceComponents(mapOrderPrices(orderModel));
            sapCpiOrder.setSapCpiCreditCardPayments(mapCreditCards(orderModel));

        });

        return sapCpiOrder;

    }

    protected SapCpiConfig mapOrderConfigInfo(OrderModel orderModel) {

        String plantCode = orderModel.getConsignments().stream().findFirst().get().getWarehouse().getCode();
        SAPLogicalSystemModel sapLogicalSystem = sapPlantLogSysOrgService.getSapLogicalSystemForPlant(orderModel.getStore(), plantCode);

        SapCpiTargetSystem sapCpiTargetSystem = new SapCpiTargetSystem();

        sapCpiTargetSystem.setSenderName(sapLogicalSystem.getSenderName());
        sapCpiTargetSystem.setSenderPort(sapLogicalSystem.getSenderPort());

        sapCpiTargetSystem.setReceiverName(sapLogicalSystem.getSapLogicalSystemName());
        sapCpiTargetSystem.setReceiverPort(sapLogicalSystem.getSapLogicalSystemName());


        if (sapLogicalSystem.getSapHTTPDestination() != null) {

            String targetUrl = sapLogicalSystem.getSapHTTPDestination().getTargetURL();

            sapCpiTargetSystem.setUrl(targetUrl);
            sapCpiTargetSystem.setClient(targetUrl.split("sap-client=")[1].substring(0, 3));
            sapCpiTargetSystem.setUsername(sapLogicalSystem.getSapHTTPDestination().getUserid());

        } else {

            String msg = String.format("Error occurs while reading the target system information for the order [%s]!", orderModel.getCode());
            LOG.error(msg);
            throw new RuntimeException(msg);
        }

        SapCpiConfig sapCpiConfig = new SapCpiConfig();
        sapCpiConfig.setSapCpiTargetSystem(sapCpiTargetSystem);

        return sapCpiConfig;

    }

    protected List<SapCpiOrderItem> mapOrderItems(OrderModel orderModel) {


        final List<SapCpiOrderItem> sapCpiOrderItems = new ArrayList<>();

        sapOrderEntryContributor.createRows(orderModel).forEach(row -> {

            final SapCpiOrderItem sapCpiOrderItem = new SapCpiOrderItem();

            sapCpiOrderItem.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderItem.setEntryNumber(mapAttribute(OrderEntryCsvColumns.ENTRY_NUMBER, row));
            sapCpiOrderItem.setQuantity(mapAttribute(OrderEntryCsvColumns.QUANTITY, row));
            sapCpiOrderItem.setProductCode(mapAttribute(OrderEntryCsvColumns.PRODUCT_CODE, row));
            sapCpiOrderItem.setPlant(mapAttribute(OrderEntryCsvColumns.WAREHOUSE, row));
            sapCpiOrderItem.setNamedDeliveryDate(mapDateAttribute(OrderEntryCsvColumns.EXPECTED_SHIPPING_DATE, row));
            sapCpiOrderItem.setUnit(mapAttribute(OrderEntryCsvColumns.ENTRY_UNIT_CODE, row));
            sapCpiOrderItem.setProductName(mapAttribute(OrderEntryCsvColumns.PRODUCT_NAME, row));
            sapCpiOrderItem.setItemCategory(mapAttribute(OrderEntryCsvColumns.ITEM_CATEGORY, row));

            sapCpiOrderItems.add(sapCpiOrderItem);

        });

        return sapCpiOrderItems;

    }

    private List<SapCpiPartnerRole> mapOrderPartners(OrderModel orderModel) {

        final List<SapCpiPartnerRole> sapCpiPartnerRoles = new ArrayList<>();

        sapPartnerContributor.createRows(orderModel).forEach(row -> {

            SapCpiPartnerRole sapCpiPartnerRole = new SapCpiPartnerRole();

            sapCpiPartnerRole.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiPartnerRole.setDocumentAddressId(mapAttribute(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, row));
            sapCpiPartnerRole.setEntryNumber(mapAttribute(OrderEntryCsvColumns.ENTRY_NUMBER, row));
            sapCpiPartnerRole.setPartnerId(mapAttribute(PartnerCsvColumns.PARTNER_CODE, row));
            sapCpiPartnerRole.setPartnerRoleCode(mapAttribute(PartnerCsvColumns.PARTNER_ROLE_CODE, row));

            sapCpiPartnerRoles.add(sapCpiPartnerRole);

        });

        return sapCpiPartnerRoles;
    }

    private List<SapCpiOrderAddress> mapOrderAddresses(OrderModel orderModel) {

        final List<SapCpiOrderAddress> sapCpiOrderAddresses = new ArrayList<>();

        sapPartnerContributor.createRows(orderModel).forEach(row -> {

            SapCpiOrderAddress sapCpiOrderAddress = new SapCpiOrderAddress();

            sapCpiOrderAddress.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderAddress.setAppartment(mapAttribute(PartnerCsvColumns.APPARTMENT, row));
            sapCpiOrderAddress.setBuilding(mapAttribute(PartnerCsvColumns.BUILDING, row));
            sapCpiOrderAddress.setCity(mapAttribute(PartnerCsvColumns.CITY, row));
            sapCpiOrderAddress.setCountryIsoCode(mapAttribute(PartnerCsvColumns.COUNTRY_ISO_CODE, row));
            sapCpiOrderAddress.setDistrict(mapAttribute(PartnerCsvColumns.DISTRICT, row));
            sapCpiOrderAddress.setDocumentAddressId(mapAttribute(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, row));
            sapCpiOrderAddress.setEmail(mapAttribute(PartnerCsvColumns.EMAIL, row));
            sapCpiOrderAddress.setFaxNumber(mapAttribute(PartnerCsvColumns.FAX, row));
            sapCpiOrderAddress.setFirstName(mapAttribute(PartnerCsvColumns.FIRST_NAME, row));
            sapCpiOrderAddress.setHouseNumber(mapAttribute(PartnerCsvColumns.HOUSE_NUMBER, row));
            sapCpiOrderAddress.setLanguageIsoCode(mapAttribute(PartnerCsvColumns.LANGUAGE_ISO_CODE, row));
            sapCpiOrderAddress.setLastName(mapAttribute(PartnerCsvColumns.LAST_NAME, row));
            sapCpiOrderAddress.setMiddleName(mapAttribute(PartnerCsvColumns.MIDDLE_NAME, row));
            sapCpiOrderAddress.setMiddleName2(mapAttribute(PartnerCsvColumns.MIDDLE_NAME2, row));
            sapCpiOrderAddress.setPobox(mapAttribute(PartnerCsvColumns.POBOX, row));
            sapCpiOrderAddress.setPostalCode(mapAttribute(PartnerCsvColumns.POSTAL_CODE, row));
            sapCpiOrderAddress.setRegionIsoCode(mapAttribute(PartnerCsvColumns.REGION_ISO_CODE, row));
            sapCpiOrderAddress.setStreet(mapAttribute(PartnerCsvColumns.STREET, row));
            sapCpiOrderAddress.setTelNumber(mapAttribute(PartnerCsvColumns.TEL_NUMBER, row));
            sapCpiOrderAddress.setTitleCode(mapAttribute(PartnerCsvColumns.TITLE, row));

            if (sapCpiOrderAddress.getDocumentAddressId() != null && !sapCpiOrderAddress.getDocumentAddressId().isEmpty())
                sapCpiOrderAddresses.add(sapCpiOrderAddress);
        });

        return sapCpiOrderAddresses;
    }


    protected List<SapCpiCreditCardPayment> mapCreditCards(OrderModel orderModel) {

        final List<SapCpiCreditCardPayment> sapCpiCreditCardPayments = new ArrayList<>();

        try {

            sapPaymentContributor.createRows(orderModel).forEach(row -> {

                SapCpiCreditCardPayment sapCpiCreditCardPayment = new SapCpiCreditCardPayment();

                sapCpiCreditCardPayment.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
                sapCpiCreditCardPayment.setCcOwner(mapAttribute(PaymentCsvColumns.CC_OWNER, row));
                sapCpiCreditCardPayment.setPaymentProvider(mapAttribute(PaymentCsvColumns.PAYMENT_PROVIDER, row));
                sapCpiCreditCardPayment.setSubscriptionId(mapAttribute(PaymentCsvColumns.SUBSCRIPTION_ID, row));

                String requestId = mapAttribute(PaymentCsvColumns.REQUEST_ID, row);
                requestId = Pattern.compile("^\\d+$").matcher(requestId).matches() ? BigInteger.valueOf(Long.parseLong(requestId)).toString(32).toUpperCase() : requestId;
                sapCpiCreditCardPayment.setRequestId(requestId);

                String month = mapAttribute(PaymentCsvColumns.VALID_TO_MONTH, row);
                sapCpiCreditCardPayment.setValidToMonth(month);

                String year = mapAttribute(PaymentCsvColumns.VALID_TO_YEAR, row);
                YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
                int days = yearMonth.lengthOfMonth();
                year = new StringBuilder().append(year).append(month).append(days).toString();
                sapCpiCreditCardPayment.setValidToYear(year);

                sapCpiCreditCardPayments.add(sapCpiCreditCardPayment);

            });


        } catch (RuntimeException ex) {

            String msg = String.format("Error occurs while setting the payment information for the order [%s]!", orderModel.getCode());
            LOG.error(msg.concat(ex.getMessage()));
            throw new RuntimeException(msg);

        }

        return sapCpiCreditCardPayments;

    }


    protected List<SapCpiOrderPriceComponent> mapOrderPrices(OrderModel orderModel) {

        final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents = new ArrayList<>();

        sapSalesConditionsContributor.createRows(orderModel).forEach(row -> {

            SapCpiOrderPriceComponent sapCpiOrderPriceComponent = new SapCpiOrderPriceComponent();

            sapCpiOrderPriceComponent.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderPriceComponent.setEntryNumber(mapAttribute(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, row));
            sapCpiOrderPriceComponent.setConditionCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CODE, row));
            sapCpiOrderPriceComponent.setConditionCounter(mapAttribute(SalesConditionCsvColumns.CONDITION_COUNTER, row));
            sapCpiOrderPriceComponent.setCurrencyIsoCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, row));
            sapCpiOrderPriceComponent.setPriceQuantity(mapAttribute(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, row));
            sapCpiOrderPriceComponent.setUnit(mapAttribute(SalesConditionCsvColumns.CONDITION_UNIT_CODE, row));
            sapCpiOrderPriceComponent.setValue(mapAttribute(SalesConditionCsvColumns.CONDITION_VALUE, row));
            sapCpiOrderPriceComponent.setAbsolute(mapAttribute(SalesConditionCsvColumns.ABSOLUTE, row));

            sapCpiOrderPriceComponents.add(sapCpiOrderPriceComponent);

        });

        return sapCpiOrderPriceComponents;

    }

    protected String mapAttribute(String attribute, Map<String, Object> row) {
        return row.get(attribute) != null ? row.get(attribute).toString() : null;
    }

    protected String mapDateAttribute(String attribute, Map<String, Object> row) {

        if (row.get(attribute) != null && row.get(attribute) instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format((Date) row.get(attribute));
        }

        return null;
    }

    protected RawItemContributor<OrderModel> getSapOrderContributor() {
        return sapOrderContributor;
    }

    @Required
    public void setSapOrderContributor(RawItemContributor<OrderModel> sapOrderContributor) {
        this.sapOrderContributor = sapOrderContributor;
    }

    protected RawItemContributor<OrderModel> getSapPaymentContributor() {
        return sapPaymentContributor;
    }

    @Required
    public void setSapPaymentContributor(RawItemContributor<OrderModel> sapPaymentContributor) {
        this.sapPaymentContributor = sapPaymentContributor;
    }

    protected RawItemContributor<OrderModel> getSapPartnerContributor() {
        return sapPartnerContributor;
    }

    @Required
    public void setSapPartnerContributor(RawItemContributor<OrderModel> sapPartnerContributor) {
        this.sapPartnerContributor = sapPartnerContributor;
    }

    protected RawItemContributor<OrderModel> getSapOrderEntryContributor() {
        return sapOrderEntryContributor;
    }

    @Required
    public void setSapOrderEntryContributor(RawItemContributor<OrderModel> sapOrderEntryContributor) {
        this.sapOrderEntryContributor = sapOrderEntryContributor;
    }

    protected RawItemContributor<OrderModel> getSapSalesConditionsContributor() {
        return sapSalesConditionsContributor;
    }

    @Required
    public void setSapSalesConditionsContributor(RawItemContributor<OrderModel> sapSalesConditionsContributor) {
        this.sapSalesConditionsContributor = sapSalesConditionsContributor;
    }

    protected SapPlantLogSysOrgService getSapPlantLogSysOrgService() {
        return sapPlantLogSysOrgService;
    }

    @Required
    public void setSapPlantLogSysOrgService(SapPlantLogSysOrgService sapPlantLogSysOrgService) {
        this.sapPlantLogSysOrgService = sapPlantLogSysOrgService;
    }

}