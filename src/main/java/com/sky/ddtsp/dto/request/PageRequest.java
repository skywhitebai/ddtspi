package com.sky.ddtsp.dto.request;

import lombok.Data;

/**
 * @author baixueping
 * @description 分页入参
 * @date 2021/4/28 16:40
 */
@Data
public class PageRequest {
    Integer pageSize = 15;
    Integer pageNum = 1;

    public Integer getPageSize() {
        if(pageSize==null||pageSize<=0){
            return 15;
        }
        return pageSize;
    }

    public Integer getPageNum() {
        if(pageNum==null||pageNum<=1){
            return 1;
        }
        return pageNum;
    }
}
