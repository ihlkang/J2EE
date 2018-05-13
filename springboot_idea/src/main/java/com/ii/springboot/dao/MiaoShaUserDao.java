package com.ii.springboot.dao;

import com.ii.springboot.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoShaUserDao {
    @Select("select * from user where id = #{id}")
    public MiaoShaUser getById(@Param("id")long id);
    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(MiaoShaUser toBeUpdate);

}
