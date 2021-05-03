package com.sky.ddtsp.job;

import com.alibaba.fastjson.JSON;
import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentials;
import com.amazon.spapi.SellingPartnerAPIAA.AWSAuthenticationCredentialsProvider;
import com.amazon.spapi.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.amazon.spapi.model.orders.OrderList;
import com.amazon.spapi.model.orders.OrdersList;
import com.sky.ddtsp.dao.custom.CustomAmazonAuthMapper;
import com.sky.ddtsp.dto.amazonAuth.AmazonConfig;
import com.sky.ddtsp.entity.AmazonAuth;
import com.sky.ddtsp.entity.AmazonAuthExample;
import com.sky.ddtsp.enums.YesOrNoEnum;
import com.sky.ddtsp.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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

    @Scheduled(cron = "0/5 * * * * *")
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
        log.info("=====>>>>>使用cron  {}", System.currentTimeMillis());
    }

    private void syncOrderInfo(AmazonAuth amazonAuth) {
        OrdersV0Api ordersV0Api = getOrdersV0Api(amazonAuth);
        GetOrdersResponse response = getOrdersByLastUpdatedAfter(amazonAuth, ordersV0Api);
        if (response == null) {
            return;
        }
        dealOrderInfo(ordersV0Api, amazonAuth, response.getPayload());
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
                .accessKeyId(AmazonConfig.getInstance().getAmazonIamUserAccessKeyId())
                //IAM user的secretKey
                .secretKey(AmazonConfig.getInstance().getAmazonIamUserSecretKey())
                //这里按照amazon对不同region的分区填写，例子是北美地区的
                .region("us-east-1")
                .build();
        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider = AWSAuthenticationCredentialsProvider.builder()
                //IAM role，特别注意：最好用IAM role当做IAM ARN去申请app
                // 而且IAM user需要添加内联策略STS关联上IAM role，具体操作看：https://www.spapi.org.cn/cn/model2/_2_console.html
                .roleArn(AmazonConfig.getInstance().getAmazonIamRoleArn())
                .roleSessionName(AmazonConfig.getInstance().getAmazonIamRoleSessionName())
                .build();
        LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
                //申请app后LWA中的clientId
                .clientId(AmazonConfig.getInstance().getAmazonAppClientId())
                //申请app后LWA中的clientSecret
                .clientSecret(AmazonConfig.getInstance().getAmazonAppClientSecret())
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

    private void dealOrderInfo(OrdersV0Api ordersV0Api, AmazonAuth amazonAuth, OrdersList ordersList) {
        if (CollectionUtils.isEmpty(ordersList.getOrders())) {
            return;
        }
        log.info("dealTime:{},dealOrderInfo orderSize:{},nextToken:{}", DateUtil.getFormatSSS(new Date()), ordersList.getOrders().size(), ordersList.getNextToken());
        ordersList.getOrders().forEach(order -> {

        });
        if (StringUtils.isEmpty(ordersList.getNextToken())) {
            AmazonAuth amazonAuthUpdate = new AmazonAuth();
            amazonAuthUpdate.setId(amazonAuth.getId());
            amazonAuthUpdate.setLastUpdatedAfter(DateUtil.utczStrToDateTime(ordersList.getLastUpdatedBefore()));
            customAmazonAuthMapper.updateByPrimaryKeySelective(amazonAuthUpdate);
        } else {
            //获取下页
            GetOrdersResponse response = getOrdersByNextToken(amazonAuth, ordersV0Api, ordersList);
            if (response == null) {
                return;
            }
            dealOrderInfo(ordersV0Api, amazonAuth, response.getPayload());
        }
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
