package com.sky.ddtspi.dao.generate;

import com.sky.ddtspi.entity.SysRight;
import com.sky.ddtspi.entity.SysRightExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysRightMapper {
    long countByExample(SysRightExample example);

    int deleteByExample(SysRightExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysRight record);

    int insertSelective(SysRight record);

    List<SysRight> selectByExample(SysRightExample example);

    SysRight selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysRight record, @Param("example") SysRightExample example);

    int updateByExample(@Param("record") SysRight record, @Param("example") SysRightExample example);

    int updateByPrimaryKeySelective(SysRight record);

    int updateByPrimaryKey(SysRight record);
}