package com.plm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModuleVo {
	
	private String product;
	private String module;
	private String version;
	private String type;
	private String mac;
	private String startDate;
	private String endDate;
	private String cipher;
	private String users;
	
	
	public ModuleVo() {
	}

	
	public ModuleVo(String startDate, String endDate) {
		this.startDate=startDate;
		this.endDate=endDate;
	}


	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		
		int len = mac.length();
		for (int i = len; i < 16; ++i) {
			mac = mac + "0";
		}
		
		this.mac = mac;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = conversionDate(startDate);
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = conversionDate(endDate);
	}

	public String getCipher() {
		return cipher;
	}

	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}
	
	
	private String conversionDate(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = "";
		try {
			Date date = sdf.parse(str);
			long times = date.getTime()/1000L;
			dateTime = Long.toHexString(times).toUpperCase();
		} catch (ParseException e) {
			System.out.println("传入的时间有误，请检查。");
		}
		return dateTime;
	}
	
}
