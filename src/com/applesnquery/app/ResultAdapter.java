package com.applesnquery.app;

/**
 * @author jeffinbao
 * ResultAdapter is an adapter used for supporting result display
 */

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<ResultInfo> {
	private int resourceId;
	
	public ResultAdapter(Context context,int textViewResourceId,List<ResultInfo> objects){
		super(context,textViewResourceId,objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		ResultInfo resultInfo=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder=new ViewHolder();
			viewHolder.resultInfoItem=(TextView)view.findViewById(R.id.result_item);
			viewHolder.resultInfoContent=(TextView)view.findViewById(R.id.result_content);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		
		viewHolder.resultInfoItem.setText(resultInfo.getItem());
		viewHolder.resultInfoContent.setText(resultInfo.getInfo());

		view.setBackgroundColor(Color.parseColor("#c8e6c9"));//convert color mode from hex to int
		
		return view;
	}
	
	class ViewHolder{
		TextView resultInfoItem;
		TextView resultInfoContent;
	}

}
