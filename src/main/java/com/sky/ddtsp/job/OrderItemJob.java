package com.sky.ddtsp.job;

import com.alibaba.fastjson.JSON;
import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.GetOrderItemsResponse;
import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.amazon.spapi.model.orders.OrderItem;
import com.amazon.spapi.model.orders.OrderItemList;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.ddtsp.dao.custom.CustomAmazonOrderItemMapper;
import com.sky.ddtsp.dao.custom.CustomAmazonOrderMapper;
import com.sky.ddtsp.dto.amazonAuth.response.ListAmazonAuthResponse;
import com.sky.ddtsp.dto.response.BaseResponse;
import com.sky.ddtsp.entity.*;
import com.sky.ddtsp.enums.AmazonSyncInfoTypeEnum;
import com.sky.ddtsp.enums.YesOrNoEnum;
import com.sky.ddtsp.service.IAmazonAuthService;
import com.sky.ddtsp.service.IAmazonSyncInfoService;
import com.sky.ddtsp.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OrderItemJob {
    @Autowired
    IAmazonAuthService amazonAuthService;
    @Autowired
    IAmazonSyncInfoService amazonSyncInfoService;
    @Autowired
    CustomAmazonOrderMapper customAmazonOrderMapper;
    @Autowired
    CustomAmazonOrderItemMapper customAmazonOrderItemMapper;
    @Scheduled(cron = "0/5 * * * * ? ")
    public void scheduled() {
        //获取获取订单信息
        log.info("{}，获取订单商品信息", DateUtil.getFormatDateStr(new Date()));
        List<AmazonAuth> amazonAuthList = amazonAuthService.listAmazonAuth();
        //获取店铺信息 遍历店铺信息
        for (AmazonAuth amazonAuth:
                amazonAuthList) {
            if (StringUtils.isEmpty(amazonAuth.getMarketplaceId())) {
                return;
            }
            try {
                syncOrderItemInfo(amazonAuth);
            } catch (ApiException e) {
                log.info("OrderItemJob fail,e:{}", JSON.toJSONString(e));
                e.printStackTrace();
                return;
            }
        }
    }

    private void syncOrderItemInfo(AmazonAuth amazonAuth) throws ApiException {
        OrdersV0Api ordersV0Api = OrderV0ApiUtil.getOrdersV0Api(amazonAuth);
        //获取一批需要同步的订单信息
        PageInfo<AmazonOrder> amazonOrderPage=getAmazonOrderPage(amazonAuth);
        if (amazonOrderPage.getTotal()==0) {
            return;
        }
        dealOrderItemInfo(ordersV0Api, amazonAuth, amazonOrderPage);
    }

    private void dealOrderItemInfo(OrdersV0Api ordersV0Api, AmazonAuth amazonAuth, PageInfo<AmazonOrder> amazonOrderPage) throws ApiException {
        for (AmazonOrder amazonOrder :amazonOrderPage.getList()) {
            saveOrderItemsInfo(ordersV0Api,amazonOrder.getAmazonOrderId(),"");
            //修改状态为已同步
            AmazonOrder amazonOrderUpdate=new AmazonOrder();
            amazonOrderUpdate.setId(amazonOrder.getId());
            amazonOrderUpdate.setSyncOrderItemStatus(YesOrNoEnum.YES.getValue());
            customAmazonOrderMapper.updateByPrimaryKeySelective(amazonOrderUpdate);
        }
        if(amazonOrderPage.isHasNextPage()){
            dealOrderItemInfo(ordersV0Api, amazonAuth, amazonOrderPage);
        }
    }

    private void saveOrderItemsInfo(OrdersV0Api ordersV0Api, String amazonOrderId, String nextToken) throws ApiException {
        GetOrderItemsResponse response = ordersV0Api.getOrderItems(amazonOrderId, nextToken);
        OrderItemList orderItems = response.getPayload().getOrderItems();
        if (CollectionUtils.isEmpty(orderItems)) {
            return;
        }
        orderItems.forEach(orderItem -> {
            AmazonOrderItem amazonOrderItem=getAmazonOrderItem(amazonOrderId,orderItem.getSellerSKU());
            if(amazonOrderItem==null){
                amazonOrderItem=new AmazonOrderItem();
                setAmazonOrderItem(orderItem,amazonOrderItem);
                amazonOrderItem.setAmazonOrderId(amazonOrderId);
                amazonOrderItem.setCreateTime(new Date());
                customAmazonOrderItemMapper.insertSelective(amazonOrderItem);
            }else{
                setAmazonOrderItem(orderItem,amazonOrderItem);
                amazonOrderItem.setUpdateTime(new Date());
                customAmazonOrderItemMapper.updateByPrimaryKeySelective(amazonOrderItem);
            }
        });
        if (response.getPayload().getNextToken() == null) {
            return;
        }
        saveOrderItemsInfo(ordersV0Api, amazonOrderId, nextToken);
    }
    private AmazonOrderItem getAmazonOrderItem(String amazonOrderId, String sellerSKU) {
        AmazonOrderItemExample example=new AmazonOrderItemExample();
        example.createCriteria().andAmazonOrderIdEqualTo(amazonOrderId).andSellerSkuEqualTo(sellerSKU);
        List<AmazonOrderItem> orderItemList=customAmazonOrderItemMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(orderItemList)){
            return null;
        }
        return orderItemList.get(0);
    }
    private void setAmazonOrderItem(OrderItem orderItem, AmazonOrderItem amazonOrderItem) {
        BeanUtils.copyProperties(orderItem,amazonOrderItem);
        amazonOrderItem.setSellerSku(orderItem.getSellerSKU());
    }
    private PageInfo<AmazonOrder> getAmazonOrderPage(AmazonAuth amazonSyncInfo) {
        PageHelper.startPage(1, 25);
        AmazonOrderExample example=new AmazonOrderExample();
        AmazonOrderExample.Criteria criteria=example.createCriteria();
        criteria.andMerchantIdEqualTo(amazonSyncInfo.getMerchantId()).andSyncOrderItemStatusEqualTo(YesOrNoEnum.NO.getValue());
        example.setOrderByClause("sync_time asc");
        List<AmazonOrder> amazonOrderList=customAmazonOrderMapper.selectByExample(example);
        PageInfo<AmazonOrder> pageInfo = new PageInfo<>(amazonOrderList);
        return pageInfo;
    }
}
