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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.12 at 07:19:30 PM EDT 
//



package org.cxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subcontractingStockInTransferQuantity",
    "unrestrictedUseQuantity",
    "blockedQuantity",
    "qualityInspectionQuantity",
    "promotionQuantity",
    "stockInTransferQuantity",
    "incrementQuantity",
    "requiredMinimumQuantity",
    "requiredMaximumQuantity",
    "stockOnHandQuantity",
    "workInProcessQuantity",
    "intransitQuantity",
    "scrapQuantity",
    "orderQuantity",
    "daysOfSupply"
})
@XmlRootElement(name = "Inventory")
public class Inventory {

    @XmlElement(name = "SubcontractingStockInTransferQuantity")
    protected SubcontractingStockInTransferQuantity subcontractingStockInTransferQuantity;
    @XmlElement(name = "UnrestrictedUseQuantity")
    protected UnrestrictedUseQuantity unrestrictedUseQuantity;
    @XmlElement(name = "BlockedQuantity")
    protected BlockedQuantity blockedQuantity;
    @XmlElement(name = "QualityInspectionQuantity")
    protected QualityInspectionQuantity qualityInspectionQuantity;
    @XmlElement(name = "PromotionQuantity")
    protected PromotionQuantity promotionQuantity;
    @XmlElement(name = "StockInTransferQuantity")
    protected StockInTransferQuantity stockInTransferQuantity;
    @XmlElement(name = "IncrementQuantity")
    protected IncrementQuantity incrementQuantity;
    @XmlElement(name = "RequiredMinimumQuantity")
    protected RequiredMinimumQuantity requiredMinimumQuantity;
    @XmlElement(name = "RequiredMaximumQuantity")
    protected RequiredMaximumQuantity requiredMaximumQuantity;
    @XmlElement(name = "StockOnHandQuantity")
    protected StockOnHandQuantity stockOnHandQuantity;
    @XmlElement(name = "WorkInProcessQuantity")
    protected WorkInProcessQuantity workInProcessQuantity;
    @XmlElement(name = "IntransitQuantity")
    protected IntransitQuantity intransitQuantity;
    @XmlElement(name = "ScrapQuantity")
    protected ScrapQuantity scrapQuantity;
    @XmlElement(name = "OrderQuantity")
    protected OrderQuantity orderQuantity;
    @XmlElement(name = "DaysOfSupply")
    protected DaysOfSupply daysOfSupply;

    /**
     * Gets the value of the subcontractingStockInTransferQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link SubcontractingStockInTransferQuantity }
     *     
     */
    public SubcontractingStockInTransferQuantity getSubcontractingStockInTransferQuantity() {
        return subcontractingStockInTransferQuantity;
    }

    /**
     * Sets the value of the subcontractingStockInTransferQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubcontractingStockInTransferQuantity }
     *     
     */
    public void setSubcontractingStockInTransferQuantity(SubcontractingStockInTransferQuantity value) {
        this.subcontractingStockInTransferQuantity = value;
    }

    /**
     * Gets the value of the unrestrictedUseQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link UnrestrictedUseQuantity }
     *     
     */
    public UnrestrictedUseQuantity getUnrestrictedUseQuantity() {
        return unrestrictedUseQuantity;
    }

    /**
     * Sets the value of the unrestrictedUseQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnrestrictedUseQuantity }
     *     
     */
    public void setUnrestrictedUseQuantity(UnrestrictedUseQuantity value) {
        this.unrestrictedUseQuantity = value;
    }

    /**
     * Gets the value of the blockedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BlockedQuantity }
     *     
     */
    public BlockedQuantity getBlockedQuantity() {
        return blockedQuantity;
    }

    /**
     * Sets the value of the blockedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BlockedQuantity }
     *     
     */
    public void setBlockedQuantity(BlockedQuantity value) {
        this.blockedQuantity = value;
    }

    /**
     * Gets the value of the qualityInspectionQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link QualityInspectionQuantity }
     *     
     */
    public QualityInspectionQuantity getQualityInspectionQuantity() {
        return qualityInspectionQuantity;
    }

    /**
     * Sets the value of the qualityInspectionQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualityInspectionQuantity }
     *     
     */
    public void setQualityInspectionQuantity(QualityInspectionQuantity value) {
        this.qualityInspectionQuantity = value;
    }

    /**
     * Gets the value of the promotionQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PromotionQuantity }
     *     
     */
    public PromotionQuantity getPromotionQuantity() {
        return promotionQuantity;
    }

    /**
     * Sets the value of the promotionQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PromotionQuantity }
     *     
     */
    public void setPromotionQuantity(PromotionQuantity value) {
        this.promotionQuantity = value;
    }

    /**
     * Gets the value of the stockInTransferQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link StockInTransferQuantity }
     *     
     */
    public StockInTransferQuantity getStockInTransferQuantity() {
        return stockInTransferQuantity;
    }

    /**
     * Sets the value of the stockInTransferQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link StockInTransferQuantity }
     *     
     */
    public void setStockInTransferQuantity(StockInTransferQuantity value) {
        this.stockInTransferQuantity = value;
    }

    /**
     * Gets the value of the incrementQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link IncrementQuantity }
     *     
     */
    public IncrementQuantity getIncrementQuantity() {
        return incrementQuantity;
    }

    /**
     * Sets the value of the incrementQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncrementQuantity }
     *     
     */
    public void setIncrementQuantity(IncrementQuantity value) {
        this.incrementQuantity = value;
    }

    /**
     * Gets the value of the requiredMinimumQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link RequiredMinimumQuantity }
     *     
     */
    public RequiredMinimumQuantity getRequiredMinimumQuantity() {
        return requiredMinimumQuantity;
    }

    /**
     * Sets the value of the requiredMinimumQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequiredMinimumQuantity }
     *     
     */
    public void setRequiredMinimumQuantity(RequiredMinimumQuantity value) {
        this.requiredMinimumQuantity = value;
    }

    /**
     * Gets the value of the requiredMaximumQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link RequiredMaximumQuantity }
     *     
     */
    public RequiredMaximumQuantity getRequiredMaximumQuantity() {
        return requiredMaximumQuantity;
    }

    /**
     * Sets the value of the requiredMaximumQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequiredMaximumQuantity }
     *     
     */
    public void setRequiredMaximumQuantity(RequiredMaximumQuantity value) {
        this.requiredMaximumQuantity = value;
    }

    /**
     * Gets the value of the stockOnHandQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link StockOnHandQuantity }
     *     
     */
    public StockOnHandQuantity getStockOnHandQuantity() {
        return stockOnHandQuantity;
    }

    /**
     * Sets the value of the stockOnHandQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link StockOnHandQuantity }
     *     
     */
    public void setStockOnHandQuantity(StockOnHandQuantity value) {
        this.stockOnHandQuantity = value;
    }

    /**
     * Gets the value of the workInProcessQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link WorkInProcessQuantity }
     *     
     */
    public WorkInProcessQuantity getWorkInProcessQuantity() {
        return workInProcessQuantity;
    }

    /**
     * Sets the value of the workInProcessQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkInProcessQuantity }
     *     
     */
    public void setWorkInProcessQuantity(WorkInProcessQuantity value) {
        this.workInProcessQuantity = value;
    }

    /**
     * Gets the value of the intransitQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link IntransitQuantity }
     *     
     */
    public IntransitQuantity getIntransitQuantity() {
        return intransitQuantity;
    }

    /**
     * Sets the value of the intransitQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntransitQuantity }
     *     
     */
    public void setIntransitQuantity(IntransitQuantity value) {
        this.intransitQuantity = value;
    }

    /**
     * Gets the value of the scrapQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link ScrapQuantity }
     *     
     */
    public ScrapQuantity getScrapQuantity() {
        return scrapQuantity;
    }

    /**
     * Sets the value of the scrapQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScrapQuantity }
     *     
     */
    public void setScrapQuantity(ScrapQuantity value) {
        this.scrapQuantity = value;
    }

    /**
     * Gets the value of the orderQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link OrderQuantity }
     *     
     */
    public OrderQuantity getOrderQuantity() {
        return orderQuantity;
    }

    /**
     * Sets the value of the orderQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderQuantity }
     *     
     */
    public void setOrderQuantity(OrderQuantity value) {
        this.orderQuantity = value;
    }

    /**
     * Gets the value of the daysOfSupply property.
     * 
     * @return
     *     possible object is
     *     {@link DaysOfSupply }
     *     
     */
    public DaysOfSupply getDaysOfSupply() {
        return daysOfSupply;
    }

    /**
     * Sets the value of the daysOfSupply property.
     * 
     * @param value
     *     allowed object is
     *     {@link DaysOfSupply }
     *     
     */
    public void setDaysOfSupply(DaysOfSupply value) {
        this.daysOfSupply = value;
    }

}