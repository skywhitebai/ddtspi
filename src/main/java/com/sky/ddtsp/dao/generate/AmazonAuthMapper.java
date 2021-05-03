package com.sky.ddtsp.dao.generate;

import com.sky.ddtsp.entity.AmazonAuth;
import com.sky.ddtsp.entity.AmazonAuthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AmazonAuthMapper {
    long countByExample(AmazonAuthExample example);

    int deleteByExample(AmazonAuthExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AmazonAuth record);

    int insertSelective(AmazonAuth record);

    List<AmazonAuth> selectByExampleWithBLOBs(AmazonAuthExample example);

    List<AmazonAuth> selectByExample(AmazonAuthExample example);

    AmazonAuth selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AmazonAuth record, @Param("example") AmazonAuthExample example);

    int updateByExampleWithBLOBs(@Param("record") AmazonAuth record, @Param("example") AmazonAuthExample example);

    int updateByExample(@Param("record") AmazonAuth record, @Param("example") AmazonAuthExample example);

    int updateByPrimaryKeySelective(AmazonAuth record);

    int updateByPrimaryKeyWithBLOBs(AmazonAuth record);

    int updateByPrimaryKey(AmazonAuth record);
}