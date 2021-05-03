package com.sky.ddtsp.dto.amazonAuth.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 保存亚马逊token
 */
@Data
public class SaveAmazonAuthRequest {
    @NotEmpty(message = "sellingPartnerId不能为空")
    String sellingPartnerId;
    @NotEmpty(message = "spapiOauthCode不能为空")
    String spapiOauthCode;
}
