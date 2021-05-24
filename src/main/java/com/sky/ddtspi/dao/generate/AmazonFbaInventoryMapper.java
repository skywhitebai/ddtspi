package com.sky.ddtspi.dao.generate;

import com.sky.ddtspi.entity.AmazonFbaInventory;
import com.sky.ddtspi.entity.AmazonFbaInventoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AmazonFbaInventoryMapper {
    long countByExample(AmazonFbaInventoryExample example);

    int deleteByExample(AmazonFbaInventoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AmazonFbaInventory record);

    int insertSelective(AmazonFbaInventory record);

    List<AmazonFbaInventory> selectByExample(AmazonFbaInventoryExample example);

    AmazonFbaInventory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AmazonFbaInventory record, @Param("example") AmazonFbaInventoryExample example);

    int updateByExample(@Param("record") AmazonFbaInventory record, @Param("example") AmazonFbaInventoryExample example);

    int updateByPrimaryKeySelective(AmazonFbaInventory record);

    int updateByPrimaryKey(AmazonFbaInventory record);
}