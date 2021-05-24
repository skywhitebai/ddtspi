package com.sky.ddtspi.entity;

import java.util.Date;

public class AmazonFbaInventory {
    private Long id;

    private String merchantId;

    private String asin;

    private String fnSku;

    private String sellerSku;

    private String condition;

    private Integer fulfillableQuantity;

    private Integer inboundWorkingQuantity;

    private Integer inboundReceivingQuantity;

    private Integer reservedQuantity;

    private Integer researchingQuantity;

    private Integer unfulfillableQuantity;

    private Date lastUpdatedTime;

    private String productName;

    private Integer totalQuantity;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getFnSku() {
        return fnSku;
    }

    public void setFnSku(String fnSku) {
        this.fnSku = fnSku == null ? null : fnSku.trim();
    }

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku == null ? null : sellerSku.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public Integer getFulfillableQuantity() {
        return fulfillableQuantity;
    }

    public void setFulfillableQuantity(Integer fulfillableQuantity) {
        this.fulfillableQuantity = fulfillableQuantity;
    }

    public Integer getInboundWorkingQuantity() {
        return inboundWorkingQuantity;
    }

    public void setInboundWorkingQuantity(Integer inboundWorkingQuantity) {
        this.inboundWorkingQuantity = inboundWorkingQuantity;
    }

    public Integer getInboundReceivingQuantity() {
        return inboundReceivingQuantity;
    }

    public void setInboundReceivingQuantity(Integer inboundReceivingQuantity) {
        this.inboundReceivingQuantity = inboundReceivingQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getResearchingQuantity() {
        return researchingQuantity;
    }

    public void setResearchingQuantity(Integer researchingQuantity) {
        this.researchingQuantity = researchingQuantity;
    }

    public Integer getUnfulfillableQuantity() {
        return unfulfillableQuantity;
    }

    public void setUnfulfillableQuantity(Integer unfulfillableQuantity) {
        this.unfulfillableQuantity = unfulfillableQuantity;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}