package com.litb.opapi.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.litb.opapi.dao.entity.OpAppInfo;
import com.litb.opapi.dao.entity.OpAppToken;
import com.litb.opapi.dao.intf.IOpAppTokenDao;
import com.litb.opapi.mybatis.mapper.OpAppInfoMapper;
import com.litb.opapi.mybatis.mapper.OpAppTokenMapper;

@Repository
public class OpAppTokenDao implements IOpAppTokenDao {
	private static Logger logger = LoggerFactory.getLogger(OpAppTokenDao.class);
	
	@Autowired private OpAppTokenMapper opAppTokenMapper;
	@Autowired private OpAppInfoMapper opAppInfoMapper;

	@Override
	@Transactional
	public OpAppToken insert(OpAppToken opAppToken) {
		// TODO Auto-generated method stub
		this.opAppTokenMapper.createEntity(opAppToken);
		return opAppToken;
	}

	@Override
	@Transactional
	@CacheEvict(value="SecretCache", key="#opAppToken.getAppToken()")
	public void delete(OpAppToken opAppToken) {
		// TODO Auto-generated method stub
		this.opAppTokenMapper.deleteEntityPhysically(opAppToken);
	}

	@Override
	@Transactional
	@CacheEvict(value="SecretCache", key="#oldToken")
	public void update(OpAppToken opAppToken, String oldToken) {
		// TODO Auto-generated method stub
		this.opAppTokenMapper.updateEntity(opAppToken);
	}

	@Override
	public OpAppToken findAppToken(String appName, String appUserKey) {
		// TODO Auto-generated method stub
		OpAppToken oppToken = new OpAppToken();
		oppToken.setAppName(appName);
		oppToken.setAppUserKey(appUserKey);
		
		return this.opAppTokenMapper.findByExample(oppToken);
	}

	@Override
	@Transactional
	public OpAppInfo insert(OpAppInfo opAppInfo) {
		// TODO Auto-generated method stub
		this.opAppInfoMapper.createEntity(opAppInfo);
		return opAppInfo;
	}

	@Override
	public List<OpAppInfo> getAllAppInfo() {
		List<OpAppInfo> list = this.opAppInfoMapper.getAll();
		return list;
	}

	@Override
	@Cacheable(value="SecretCache")
	public OpAppToken findAppTokenByToken(String token) {
		// TODO Auto-generated method stub
		logger.info("try to query token from database by token string.");
		OpAppToken oppToken = new OpAppToken();
		oppToken.setAppToken(token);
		return this.opAppTokenMapper.findByExample(oppToken);
	}

}
