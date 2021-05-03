package com.sky.ddtsp.dao.generate;

import com.sky.ddtsp.entity.SysRoleRight;
import com.sky.ddtsp.entity.SysRoleRightExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysRoleRightMapper {
    long countByExample(SysRoleRightExample example);

    int deleteByExample(SysRoleRightExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleRight record);

    int insertSelective(SysRoleRight record);

    List<SysRoleRight> selectByExample(SysRoleRightExample example);

    SysRoleRight selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysRoleRight record, @Param("example") SysRoleRightExample example);

    int updateByExample(@Param("record") SysRoleRight record, @Param("example") SysRoleRightExample example);

    int updateByPrimaryKeySelective(SysRoleRight record);

    int updateByPrimaryKey(SysRoleRight record);
}