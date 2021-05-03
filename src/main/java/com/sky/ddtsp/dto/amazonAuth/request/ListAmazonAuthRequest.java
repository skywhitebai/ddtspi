package com.sky.ddtsp.dto.amazonAuth.request;

import com.sky.ddtsp.dto.request.PageRequest;
import lombok.Data;

/**
 * @author baixueping
 * @description 列表查询亚马逊授权入参
 * @date 2021/4/28 16:29
 */
@Data
public class ListAmazonAuthRequest extends PageRequest {
    String merchantId;
    String shopName;
    Integer status;
}
