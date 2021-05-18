package com.sky.ddtsp.job;

import com.alibaba.fastjson.JSON;
import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentialsProvider;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.*;
import com.sky.ddtsp.dao.custom.CustomAmazonAuthMapper;
import com.sky.ddtsp.dao.custom.CustomAmazonOrderItemMapper;
import com.sky.ddtsp.dao.custom.CustomAmazonOrderMapper;
import com.sky.ddtsp.dao.custom.CustomAmazonSyncInfoMapper;
import com.sky.ddtsp.dto.amazonAuth.AmazonConfig;
import com.sky.ddtsp.entity.*;
import com.sky.ddtsp.enums.AmazonSyncInfoTypeEnum;
import com.sky.ddtsp.enums.YesOrNoEnum;
import com.sky.ddtsp.service.IAmazonSyncInfoService;
import com.sky.ddtsp.util.DateUtil;
import com.sky.ddtsp.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OrdersJob {
    @Autowired
    CustomAmazonAuthMapper customAmazonAuthMapper;
    @Autowired
    CustomAmazonOrderMapper customAmazonOrderMapper;
    @Autowired
    IAmazonSyncInfoService amazonSyncInfoService;
    @Autowired
    CustomAmazonSyncInfoMapper customAmazonSyncInfoMapper;
    @Autowired
    CustomAmazonOrderItemMapper customAmazonOrderItemMapper;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void scheduled() {
        List<AmazonAuth> amazonAuthList = listAmazonAuth();
        //获取店铺信息 遍历店铺信息
        amazonAuthList.forEach(amazonAuth -> {
            if (StringUtils.isEmpty(amazonAuth.getMarketplaceId())) {
                return;
            }

            syncOrderInfo(amazonAuth);
        });
        //获取获取订单信息
        log.info("{}，获取订单信息", DateUtil.getFormatDateStr(new Date()));
    }

    private void syncOrderInfo(AmazonAuth amazonAuth) {
        OrdersV0Api ordersV0Api = getOrdersV0Api(amazonAuth);
        AmazonSyncInfo amazonSyncInfo = amazonSyncInfoService.getAmazonSyncInfo(AmazonSyncInfoTypeEnum.ORDER, amazonAuth.getMerchantId());
        GetOrdersResponse response = getOrders(amazonAuth, ordersV0Api, amazonSyncInfo);
        if (response == null) {
            return;
        }
        dealOrderInfo(ordersV0Api, amazonAuth, response.getPayload(), amazonSyncInfo);
    }

    private GetOrdersResponse getOrders(AmazonAuth amazonAuth, OrdersV0Api ordersV0Api, AmazonSyncInfo amazonSyncInfo) {
        GetOrdersResponse response = null;

        List<String> marketplaceIds = new ArrayList<>();
        marketplaceIds.add(amazonAuth.getMarketplaceId());
        String createdAfter = null;
        String createdBefore = null;
        String lastUpdatedAfter = null;
        String nextToken = null;
        if (StringUtils.isEmpty(amazonSyncInfo.getNextToken())) {
            lastUpdatedAfter = DateUtil.dateTimeToUtczStr(amazonAuth.getLastUpdatedAfter());
        } else {
            nextToken = amazonSyncInfo.getNextToken();
        }
        String lastUpdatedBefore = null;
        List<String> orderStatuses = null;
        List<String> fulfillmentChannels = null;
        List<String> paymentMethods = null;
        String buyerEmail = null;
        String sellerOrderId = null;
        Integer maxResultsPerPage = null;
        List<String> easyShipShipmentStatuses = null;
        List<String> amazonOrderIds = null;
        try {
            response = ordersV0Api.getOrders(marketplaceIds, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, orderStatuses, fulfillmentChannels, paymentMethods, buyerEmail, sellerOrderId, maxResultsPerPage, easyShipShipmentStatuses, nextToken, amazonOrderIds);
        } catch (ApiException e) {
            log.error("syncOrderInfo fail,amazonAuth:{},erro:{}", JSON.toJSONString(amazonAuth), e.getMessage());
            log.error("syncOrderInfo fail,amazonAuth:{},erro:{}", JSON.toJSONString(amazonAuth), e.getCode());
            e.printStackTrace();
            return null;
        }
        return response;
    }

    GetOrdersResponse getOrdersByLastUpdatedAfter(AmazonAuth amazonAuth, OrdersV0Api ordersV0Api) {
        GetOrdersResponse response = null;

        List<String> marketplaceIds = new ArrayList<>();
        marketplaceIds.add(amazonAuth.getMarketplaceId());
        String createdAfter = null;
        String createdBefore = null;
        String lastUpdatedAfter = "2018-01-01T00:00:00Z";
        if (!StringUtils.isEmpty(amazonAuth.getLastUpdatedAfter())) {
            lastUpdatedAfter = DateUtil.dateTimeToUtczStr(amazonAuth.getLastUpdatedAfter());
        }
        String lastUpdatedBefore = null;
        List<String> orderStatuses = null;
        List<String> fulfillmentChannels = null;
        List<String> paymentMethods = null;
        String buyerEmail = null;
        String sellerOrderId = null;
        Integer maxResultsPerPage = null;
        List<String> easyShipShipmentStatuses = null;
        String nextToken = null;
        List<String> amazonOrderIds = null;
        try {
            response = ordersV0Api.getOrders(marketplaceIds, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, orderStatuses, fulfillmentChannels, paymentMethods, buyerEmail, sellerOrderId, maxResultsPerPage, easyShipShipmentStatuses, nextToken, amazonOrderIds);
        } catch (ApiException e) {
            log.error("syncOrderInfo fail,amazonAuth:{},erro:{}", JSON.toJSONString(amazonAuth), e.getMessage());
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private OrdersV0Api getOrdersV0Api(AmazonAuth amazonAuth) {
        AWSAuthenticationCredentials awsAuthenticationCredentials = AWSAuthenticationCredentials.builder()
                //IAM user的accessKeyId
                .accessKeyId(AmazonConfig.INSTANCE.getAmazonIamUserAccessKeyId())
                //IAM user的secretKey
                .secretKey(AmazonConfig.INSTANCE.getAmazonIamUserSecretKey())
                //这里按照amazon对不同region的分区填写，例子是北美地区的
                .region("us-east-1")
                .build();
        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider = AWSAuthenticationCredentialsProvider.builder()
                //IAM role，特别注意：最好用IAM role当做IAM ARN去申请app
                // 而且IAM user需要添加内联策略STS关联上IAM role，具体操作看：https://www.spapi.org.cn/cn/model2/_2_console.html
                .roleArn(AmazonConfig.INSTANCE.getAmazonIamRoleArn())
                .roleSessionName(AmazonConfig.INSTANCE.getAmazonIamRoleSessionName())
                .build();
        LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
                //申请app后LWA中的clientId
                .clientId(AmazonConfig.INSTANCE.getAmazonAppClientId())
                //申请app后LWA中的clientSecret
                .clientSecret(AmazonConfig.INSTANCE.getAmazonAppClientSecret())
                //店铺授权时产生的refreshToken或者app自授权生成的
                .refreshToken(amazonAuth.getRefreshToken())
                .endpoint("https://api.amazon.com/auth/o2/token")
                .build();
        return new OrdersV0Api.Builder()
                .awsAuthenticationCredentials(awsAuthenticationCredentials)
                .lwaAuthorizationCredentials(lwaAuthorizationCredentials)
                .awsAuthenticationCredentialsProvider(awsAuthenticationCredentialsProvider)
                .endpoint("https://sellingpartnerapi-na.amazon.com")
                .build();
    }

    private void dealOrderInfo(OrdersV0Api ordersV0Api, AmazonAuth amazonAuth, OrdersList ordersList, AmazonSyncInfo amazonSyncInfo) {
        if (CollectionUtils.isEmpty(ordersList.getOrders())) {
            return;
        }
        log.info("dealTime:{},dealOrderInfo orderSize:{},nextToken:{}", DateUtil.getFormatSSS(new Date()), ordersList.getOrders().size(), ordersList.getNextToken());
        ordersList.getOrders().forEach(order -> {
            AmazonOrder amazonOrder = getAmazonOrderByAmazonOrderId(order.getAmazonOrderId());
            if (amazonOrder == null) {
                amazonOrder = new AmazonOrder();
                amazonOrder.setMerchantId(amazonAuth.getMerchantId());
                setAmazonOrderInfo(amazonOrder, order);
                amazonOrder.setCreateTime(new Date());
                amazonOrder.setUpdateTime(new Date());
                customAmazonOrderMapper.insertSelective(amazonOrder);
            } else {
                setAmazonOrderInfo(amazonOrder, order);
                amazonOrder.setUpdateTime(new Date());
                customAmazonOrderMapper.updateByPrimaryKeySelective(amazonOrder);
            }
            try {
                dealOrderItemsInfo(ordersV0Api, order.getAmazonOrderId(), null);
            } catch (ApiException e) {
                e.printStackTrace();
                log.info("dealOrderItemsInfo fail,orderId:{},message:{}，code:{}", order.getAmazonOrderId(), e.getMessage(), e.getCode());
                return;
            }
        });
        if (StringUtils.isEmpty(ordersList.getNextToken())) {
            //没有nextToken表示同步完成
            amazonSyncInfo.setLastUpdateAfter(DateUtil.utczStrToDateTime(ordersList.getLastUpdatedBefore()));
            amazonSyncInfo.setUpdateTime(new Date());
            customAmazonSyncInfoMapper.updateByPrimaryKey(amazonSyncInfo);
        } else {
            amazonSyncInfo.setNextToken(ordersList.getNextToken());
            amazonSyncInfo.setUpdateTime(new Date());
            customAmazonSyncInfoMapper.updateByPrimaryKey(amazonSyncInfo);
            //获取下页
            GetOrdersResponse response = getOrders(amazonAuth, ordersV0Api, amazonSyncInfo);
            if (response == null) {
                return;
            }
            dealOrderInfo(ordersV0Api, amazonAuth, response.getPayload(), amazonSyncInfo);
        }
    }

    private void dealOrderItemsInfo(OrdersV0Api ordersV0Api, String amazonOrderId, String nextToken) throws ApiException {
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
        dealOrderItemsInfo(ordersV0Api, amazonOrderId, nextToken);
    }

    private void setAmazonOrderItem(OrderItem orderItem, AmazonOrderItem amazonOrderItem) {
        BeanUtils.copyProperties(orderItem,amazonOrderItem);
        amazonOrderItem.setSellerSku(orderItem.getSellerSKU());
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

    private void setAmazonOrderInfo(AmazonOrder amazonOrder, Order order) {
        amazonOrder.setMarketplaceId(order.getMarketplaceId());
        amazonOrder.setAmazonOrderId(order.getAmazonOrderId());
        amazonOrder.setSellerOrderId(order.getSellerOrderId());
        amazonOrder.setPurchaseDate(DateUtil.utcStrToDateTime(order.getPurchaseDate()));
        amazonOrder.setLastUpdateDate(DateUtil.utcStrToDateTime(order.getLastUpdateDate()));
        amazonOrder.setOrderDate(DateUtil.dateTimeToDate(amazonOrder.getPurchaseDate()));
        if (order.getOrderStatus() != null) {
            amazonOrder.setOrderStatus(order.getOrderStatus().getValue());
        }
        if (order.getOrderType() != null) {
            amazonOrder.setOrderType(order.getOrderType().getValue());
        }
        if (order.getFulfillmentChannel() != null) {
            amazonOrder.setFulfillmentChannel(order.getFulfillmentChannel().getValue());
        }
        amazonOrder.setSalesChannel(order.getSalesChannel());
        amazonOrder.setOrderChannel(order.getOrderChannel());
        amazonOrder.setShipServiceLevel(order.getShipServiceLevel());
        amazonOrder.setShipmentServiceLevelCategory(order.getShipmentServiceLevelCategory());
        if (order.getOrderTotal() != null) {
            amazonOrder.setCurrency(order.getOrderTotal().getCurrencyCode());
            amazonOrder.setTotalAmount(MathUtil.strToBigDecimal(order.getOrderTotal().getAmount()));
        }
        amazonOrder.setNumberOfItemsShipped(order.getNumberOfItemsShipped());
        amazonOrder.setNumberOfItemsUnshipped(order.getNumberOfItemsUnshipped());
        amazonOrder.setPaymentExecutionDetail(JSON.toJSONString(order.getPaymentExecutionDetail()));
        if (order.getPaymentMethod() != null) {
            amazonOrder.setPaymentMethod(order.getPaymentMethod().getValue());
        }
        amazonOrder.setEasyShipShipmentStatus(order.getEasyShipShipmentStatus());
        amazonOrder.setEarliestShipDate(DateUtil.utcStrToDateTime(order.getEarliestShipDate()));
        amazonOrder.setLatestShipDate(DateUtil.utcStrToDateTime(order.getLatestShipDate()));
        amazonOrder.setEarlyestDeliveryDate(DateUtil.utcStrToDateTime(order.getEarliestDeliveryDate()));
        amazonOrder.setLatestDeliveryDate(DateUtil.utcStrToDateTime(order.getLatestDeliveryDate()));
        amazonOrder.setBusinessOrder(order.isIsBusinessOrder());
        amazonOrder.setPrime(order.isIsPrime());
        amazonOrder.setPremiumOrder(order.isIsPremiumOrder());
        amazonOrder.setGlobalExpressEnabled(order.isIsGlobalExpressEnabled());
        amazonOrder.setReplacementOrder(order.isIsReplacementOrder());
        amazonOrder.setReplacedOrderId(order.getReplacedOrderId());
        amazonOrder.setPromiseResponseDueDate(order.getPromiseResponseDueDate());
        amazonOrder.setEstimatedShipDateSet(order.isIsEstimatedShipDateSet());
        amazonOrder.setSoldByAb(order.isIsSoldByAB());
        if (order.getAssignedShipFromLocationAddress() != null) {
            amazonOrder.setAssignedShipFromLocationAddress(JSON.toJSONString(order.getAssignedShipFromLocationAddress()));
            amazonOrder.setBuyerEmail(order.getAssignedShipFromLocationAddress().getName());
            amazonOrder.setStateOrRegion(order.getAssignedShipFromLocationAddress().getStateOrRegion());
            amazonOrder.setCountryCode(order.getAssignedShipFromLocationAddress().getCountryCode());
            amazonOrder.setPostalCode(order.getAssignedShipFromLocationAddress().getPostalCode());
            amazonOrder.setCity(order.getAssignedShipFromLocationAddress().getCity());
            amazonOrder.setCounty(order.getAssignedShipFromLocationAddress().getCounty());
            amazonOrder.setAddressLine1(order.getAssignedShipFromLocationAddress().getAddressLine1());
        }
    }

    private AmazonOrder getAmazonOrderByAmazonOrderId(String amazonOrderId) {
        AmazonOrderExample amazonOrderExample = new AmazonOrderExample();
        amazonOrderExample.createCriteria().andAmazonOrderIdEqualTo(amazonOrderId);
        List<AmazonOrder> list = customAmazonOrderMapper.selectByExample(amazonOrderExample);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    private GetOrdersResponse getOrdersByNextToken(AmazonAuth amazonAuth, OrdersV0Api ordersV0Api, OrdersList ordersList) {
        GetOrdersResponse response = null;

        List<String> marketplaceIds = new ArrayList<>();
        marketplaceIds.add(amazonAuth.getMarketplaceId());
        String createdAfter = null;
        String createdBefore = null;
        String lastUpdatedAfter = null;
        String lastUpdatedBefore = null;
        List<String> orderStatuses = null;
        List<String> fulfillmentChannels = null;
        List<String> paymentMethods = null;
        String buyerEmail = null;
        String sellerOrderId = null;
        Integer maxResultsPerPage = null;
        List<String> easyShipShipmentStatuses = null;
        String nextToken = ordersList.getNextToken();
        List<String> amazonOrderIds = null;
        try {
            response = ordersV0Api.getOrders(marketplaceIds, createdAfter, createdBefore, lastUpdatedAfter, lastUpdatedBefore, orderStatuses, fulfillmentChannels, paymentMethods, buyerEmail, sellerOrderId, maxResultsPerPage, easyShipShipmentStatuses, nextToken, amazonOrderIds);
        } catch (ApiException e) {
            log.error("syncOrderInfo fail,amazonAuth:{},erro:{}", JSON.toJSONString(amazonAuth), e.getMessage());
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private List<AmazonAuth> listAmazonAuth() {
        AmazonAuthExample amazonAuthExample = new AmazonAuthExample();
        amazonAuthExample.createCriteria().andStatusEqualTo(YesOrNoEnum.YES.getValue());
        return customAmazonAuthMapper.selectByExample(amazonAuthExample);
    }
}
