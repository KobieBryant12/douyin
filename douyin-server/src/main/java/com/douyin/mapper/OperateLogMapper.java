package com.douyin.mapper;

import com.douyin.entity.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper {
    @Insert("insert into douyin.operate_log (user_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{userId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);
}
