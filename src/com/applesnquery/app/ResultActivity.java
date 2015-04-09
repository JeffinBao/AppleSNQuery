package com.applesnquery.app;


import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	
	private TextView snText;
	private List<ResultInfo> resultList;
	private List<String> errorReasonList=new ArrayList<String>();
	
	/**
	 * 
	 * @param context
	 * @param serialNumber
	 * best way to start an activity
	 */
	public static void actionStart(Context context,String serialNumber){
		Intent intent=new Intent(context,ResultActivity.class);
		intent.putExtra("sn_data", serialNumber);
		context.startActivity(intent);
	}
	
	/**
	 * 
	 * @param jsonData
	 * @return
	 * analyze jsonData get from juhe api
	 */
	private List<ResultInfo> parseJSONWithJSONObject(String jsonData){
		List<ResultInfo> resultInfoList=new ArrayList<ResultInfo>();
		try{
				JSONObject jsonObject=new JSONObject(jsonData);
				String resultcode=jsonObject.getString("resultcode");
				String reason=jsonObject.getString("reason");
				String result=jsonObject.getString("result");
				int resultcodeInt=Integer.parseInt(resultcode);	
				switch(resultcodeInt){
				case 200:
					JSONObject jsonObjectResult=new JSONObject(result);
					
					String phone_model=jsonObjectResult.getString("phone_model");
					Log.d("ResultActivity",phone_model);
					ResultInfo phoneModel=new ResultInfo("设备型号",phone_model);
					resultInfoList.add(phoneModel);
					
					String active=jsonObjectResult.getString("active");
					Log.d("ResultActivity",active);
					ResultInfo acTive=new ResultInfo("激活状态",active);
					resultInfoList.add(acTive);
					
					String warranty_status=jsonObjectResult.getString("warranty_status");
					String warranty=jsonObjectResult.getString("warranty");
					Log.d("ResultActivity",warranty_status);
					Log.d("ResultActivity",warranty);
					ResultInfo warrantyStatus=new ResultInfo("保修期限",warranty_status+" ("+warranty+")");
					resultInfoList.add(warrantyStatus);
					
					String tele_support=jsonObjectResult.getString("tele_support");
					String tele_support_status=jsonObjectResult.getString("tele_support_status");
					Log.d("ResultActivity",tele_support);
					Log.d("ResultActivity",tele_support_status);
					ResultInfo teleSupport=new ResultInfo("电话支持",tele_support_status+" ("+tele_support+")");
					resultInfoList.add(teleSupport);
					
					String start_date=jsonObjectResult.getString("start_date");
					String end_date=jsonObjectResult.getString("end_date");	
					Log.d("ResultActivity",start_date);
					Log.d("ResultActivity",end_date);
					ResultInfo manufactureDate=new ResultInfo("生产日期",start_date+" -- "+end_date);
					resultInfoList.add(manufactureDate);
					
					String made_area=jsonObjectResult.getString("made_area");
					Log.d("ResultActivity",made_area);
					ResultInfo madeArea=new ResultInfo("生产地点",made_area);
					resultInfoList.add(madeArea);
					break;
				case 201:
                    errorReasonList.add("序列号不正确");
					break;
				case 202:
                    errorReasonList.add(reason);
					break;
				case 203:
					errorReasonList.add(reason);
					break;
				case 204:
					errorReasonList.add(reason);
					break;
				case 205:
					errorReasonList.add(reason);
					break;
			    default:
			    	break;
						
					}	
				
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultInfoList;
	}
	
	
	public void onBackPressed(){
		Intent intent=new Intent(ResultActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.result_activity);
		
		String sn=getIntent().getStringExtra("sn_data");
		snText=(TextView)findViewById(R.id.sn_text);
		snText.setText(sn);
		
		final ProgressDialog progressDialog=new ProgressDialog(ResultActivity.this);
		progressDialog.setMessage("结果加载中，请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.show();
		
		Parameters params=new Parameters();
		params.add("sn",sn);
		params.add("dtype","json");
		
		JuheData.executeWithAPI(37,"http://apis.juhe.cn/appleinfo/index",JuheData.GET,params,new DataCallBack(){
			
			@Override
			public void resultLoaded(int err,String reason,String result){
				if(err==0){
					Log.d("ResultActivity",result);
					resultList=parseJSONWithJSONObject(result);
					if(!resultList.isEmpty()){
						ListView listView=(ListView)findViewById(R.id.result_listview);
						ResultAdapter adapter=new ResultAdapter(ResultActivity.this,R.layout.result_info_display,resultList);
						listView.setAdapter(adapter);
						progressDialog.dismiss();//close progress dialog once the result display
					}else{
						String errorInfo=errorReasonList.get(0);//error infomation from errorReasonList
						Toast.makeText(getApplicationContext(), errorInfo, Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(ResultActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
	
				}else{
					Toast.makeText(getApplicationContext(), "请打开网络", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(ResultActivity.this,MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
    }

}
