package com.sky.ddtsp.entity;

import java.util.Date;

public class AmazonAuth {
    private Long id;

    private String merchantId;

    private String shopName;

    private Long customerId;

    private String refreshToken;

    private String nwsAuthToken;

    private Integer endPointArea;

    private Date orderTimeAfter;

    private Date orderTimeBefore;

    private String remark;

    private Integer status;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String orderNextToken;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    public String getNwsAuthToken() {
        return nwsAuthToken;
    }

    public void setNwsAuthToken(String nwsAuthToken) {
        this.nwsAuthToken = nwsAuthToken == null ? null : nwsAuthToken.trim();
    }

    public Integer getEndPointArea() {
        return endPointArea;
    }

    public void setEndPointArea(Integer endPointArea) {
        this.endPointArea = endPointArea;
    }

    public Date getOrderTimeAfter() {
        return orderTimeAfter;
    }

    public void setOrderTimeAfter(Date orderTimeAfter) {
        this.orderTimeAfter = orderTimeAfter;
    }

    public Date getOrderTimeBefore() {
        return orderTimeBefore;
    }

    public void setOrderTimeBefore(Date orderTimeBefore) {
        this.orderTimeBefore = orderTimeBefore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderNextToken() {
        return orderNextToken;
    }

    public void setOrderNextToken(String orderNextToken) {
        this.orderNextToken = orderNextToken == null ? null : orderNextToken.trim();
    }
}