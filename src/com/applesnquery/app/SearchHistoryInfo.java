package com.applesnquery.app;

public class SearchHistoryInfo {

	private String searchSN;
	private String searchTime;
	
	public SearchHistoryInfo(String searchSN,String searchTime){
		this.searchSN=searchSN;
		this.searchTime=searchTime;
	}
	
	public String getSearchSN(){
		return searchSN;
	}
	
	public String getSearchTime(){
		return searchTime;
	}

}
