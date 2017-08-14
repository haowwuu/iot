package com.cetiti.ddapv2.process.service.support;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cetiti.ddapv2.process.dao.AccountDao;
import com.cetiti.ddapv2.process.model.Account;
import com.cetiti.ddapv2.process.service.AccountService;
import com.cetiti.ddapv2.process.util.MessageUtil;
import com.cetiti.ddapv2.process.util.EncryptUtil;
import com.cetiti.ddapv2.process.util.MessageContext;

/**
 * @Description TODO
 * @author Wuwuhao
 * @date 2017年8月1日
 * 
 */
@Service
public class AccountServiceImpl implements AccountService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Resource
	private AccountDao accountDao;
	@Resource
	private MessageUtil msgUtil;
	
	@Override
	public Account getAccount(String account) {
		if(!StringUtils.hasText(account)){
			return null;
		}
		try{
			return accountDao.selectAccount(account);
		}catch (Exception e) {
			MessageContext.setMsg(msgUtil.get("db.exception"));
			LOGGER.error("getAccount [{}]", e.getMessage());
			return null;
		}
	}

	@Override
	public boolean addAccount(Account account) {
		if(null==account){
			return false;
		}
		if(!StringUtils.hasText(account.getAccount())){
			MessageContext.setMsg(msgUtil.get("account.null"));
			return false;
		}
		if(!StringUtils.hasText(account.getPassword())){
			MessageContext.setMsg(msgUtil.get("password.null"));
			return false;
		}
		if(null!=getAccount(account.getAccount())){
			MessageContext.setMsg(msgUtil.get("account.exist"));
			return false;
		}
		try{
			accountDao.insertAccount(account);
			return true;
		}catch (Exception e) {
			MessageContext.setMsg(msgUtil.get("db.exception"));
			LOGGER.error("addAccount [{}]", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteAccount(String account) {
		if(!StringUtils.hasText(account)){
			return true;
		}
		try{
			accountDao.deleteAccount(account);
			return true;
		}catch (Exception e) {
			MessageContext.setMsg(msgUtil.get("db.exception"));
			LOGGER.error("deleteAccount [{}]", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateAccount(Account account) {
		if(null==account||!StringUtils.hasText(account.getAccount())){
			MessageContext.setMsg("account.null");
			return false;
		}
		try{
			accountDao.updateAccount(account);
			return true;
		}catch (Exception e) {
			MessageContext.setMsg(msgUtil.get("db.exception"));
			LOGGER.error("updateAccount [{}]", e.getMessage());
			return false;
		}
	}

	@Override
	public boolean login(Account account) {
		if(null==account||!StringUtils.hasText(account.getAccount())
				||!StringUtils.hasText(account.getPassword())){
			MessageContext.setMsg(msgUtil.get("password.incorrect"));
			return false;
		}
		Account dbaccount = getAccount(account.getAccount());
		if(null==dbaccount){
			MessageContext.setMsg(msgUtil.get("account.null"));
			return false;
		}
		if(!account.getPassword().equals(EncryptUtil.md5(dbaccount.getPassword()+account.getToken()))){
			MessageContext.setMsg(msgUtil.get("password.incorrect"));
			return false;
		}
		
		return true;
	}

	@Override
	public List<Account> getAccountList() {
		try{
			return accountDao.selectAccountList();
		}catch (Exception e) {
			MessageContext.setMsg(msgUtil.get("db.exception"));
			LOGGER.error("getAccountList [{}]", e.getMessage());
			return new ArrayList<>();
		}
	}

}