package com.litb.opapi.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.mybatis.MyBatisBaseMapper;

public interface OpAppTokenMapper extends MyBatisBaseMapper<OpAppToken>{
	
	public OpAppToken findByExample(@Param("OpAppToken") OpAppToken appToken);
	
}
