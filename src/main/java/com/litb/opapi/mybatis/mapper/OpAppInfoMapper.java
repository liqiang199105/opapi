package com.litb.opapi.mybatis.mapper;

import java.util.List;

import com.litb.opapi.dao.entity.OpAppInfo;
import com.litb.opapi.mybatis.MyBatisBaseMapper;

public interface OpAppInfoMapper extends MyBatisBaseMapper<OpAppInfo>{
	
	public List<OpAppInfo> getAll();

}
