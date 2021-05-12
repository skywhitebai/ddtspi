package com.sky.ddtsp.enums;

import lombok.Getter;

/**
 * @author baixueping
 * @description 亚马逊同步信息类型
 * @date 2021/5/11 14:45
 */
@Getter
public enum AmazonSyncInfoTypeEnum {
    ORDER("order");

    String type;

    AmazonSyncInfoTypeEnum(String type) {
        this.type = type;
    }
}
