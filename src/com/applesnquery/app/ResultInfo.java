package com.applesnquery.app;

/**
 * 
 * @author jeffinbao
 * ResultInfo class is used for storing both item and search result
 */
public class ResultInfo {
	
	private String item;
	private String info;
	
	public ResultInfo(String item,String info){
		this.item=item;
		this.info=info;
	}
	
	public String getItem(){
		return item;
	}
	
	public String getInfo(){
		return info;
	}

}
