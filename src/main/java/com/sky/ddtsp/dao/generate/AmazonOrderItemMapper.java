package com.sky.ddtsp.dao.generate;

import com.sky.ddtsp.entity.AmazonOrderItem;
import com.sky.ddtsp.entity.AmazonOrderItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AmazonOrderItemMapper {
    long countByExample(AmazonOrderItemExample example);

    int deleteByExample(AmazonOrderItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AmazonOrderItem record);

    int insertSelective(AmazonOrderItem record);

    List<AmazonOrderItem> selectByExample(AmazonOrderItemExample example);

    AmazonOrderItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AmazonOrderItem record, @Param("example") AmazonOrderItemExample example);

    int updateByExample(@Param("record") AmazonOrderItem record, @Param("example") AmazonOrderItemExample example);

    int updateByPrimaryKeySelective(AmazonOrderItem record);

    int updateByPrimaryKey(AmazonOrderItem record);
}