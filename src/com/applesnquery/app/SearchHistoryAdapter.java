package com.applesnquery.app;
/**
 * SearchHistoryAdapter is used for displaying SN search history in MainActivity
 */
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchHistoryAdapter extends ArrayAdapter<SearchHistoryInfo> {
	
	private int resourceId;
	
	public SearchHistoryAdapter(Context context,int textViewResourceId,List<SearchHistoryInfo> objects){
		super(context,textViewResourceId,objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		SearchHistoryInfo searchHistory=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder=new ViewHolder();
			viewHolder.searchSN=(TextView)view.findViewById(R.id.search_history_sn);
			viewHolder.searchTime=(TextView)view.findViewById(R.id.search_history_time);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		
		viewHolder.searchSN.setText(searchHistory.getSearchSN());
		viewHolder.searchTime.setText(searchHistory.getSearchTime());
		
		return view;
	}
	
	class ViewHolder{
		TextView searchSN;
		TextView searchTime;
	}

}
