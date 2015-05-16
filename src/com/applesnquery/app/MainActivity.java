package com.applesnquery.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private Button searchButton;
	private EditText SnEditText;
	private SwipeMenuListView searchHistoryListview;
	private Button clearHistoryButton;
	private List<SearchHistoryInfo> searchHistoryList;
	private MyDatabaseHelper dbHelper;
	
    private long exitTime=0;
	
	private void exitApp(){
		if(System.currentTimeMillis()-exitTime>2000){
			Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
			exitTime=System.currentTimeMillis();
		}else{
			finish();
		}
	}
	
	/**
	 * exit application with an confirmation dialog
	 */
	public void onBackPressed(){
		exitApp();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        dbHelper=new MyDatabaseHelper(this,"search_history.db",null,1);
        final SQLiteDatabase db=dbHelper.getWritableDatabase();  
        searchHistoryList=new ArrayList<SearchHistoryInfo>();
        
        SnEditText=(EditText)findViewById(R.id.edit_text);
        
        searchButton=(Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String serialNumber=SnEditText.getText().toString().toUpperCase();
								
				if(serialNumber.length()==12){	
					
					InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							            
					ResultActivity.actionStart(MainActivity.this, serialNumber);
						
					finish();//kill MainActivity process
					
				}else if(serialNumber.length()==0){
					Toast.makeText(MyApplication.getContext(), "序列号不能为空", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MyApplication.getContext(), "请输入正确位数的序列号", Toast.LENGTH_SHORT).show();
				}
						
		   }
		});
        
        Cursor cursor=db.query("search_history",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
        	do{
         		String serialNum=cursor.getString(cursor.getColumnIndex("sn"));
        		String timeNow=cursor.getString(cursor.getColumnIndex("time"));
         		SearchHistoryInfo searchHistoryInfo=new SearchHistoryInfo(serialNum,timeNow);
                searchHistoryList.add(searchHistoryInfo);
           	  }while(cursor.moveToNext());
          }
          cursor.close();   
            
		  SearchHistoryAdapter adapter=new SearchHistoryAdapter(MainActivity.this,R.layout.search_history_display,searchHistoryList);
		  searchHistoryListview=(SwipeMenuListView)findViewById(R.id.search_history_listview);
		  clearHistoryButton=(Button)findViewById(R.id.clear_history_button);
		  /**
		   * whether there's any data in db,if so,searchHistoryListview and clearHistoryButton is VISIBLE.
		   * otherwise,searchHistoryListview and clearHistoryButton is INVISIBLE.
		   */
		  if(cursor.getCount()>0){
			  searchHistoryListview.setVisibility(View.VISIBLE);
			  clearHistoryButton.setVisibility(View.VISIBLE);
			  searchHistoryListview.setAdapter(adapter); 
			  searchHistoryListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					TextView tv=(TextView)view.findViewById(R.id.search_history_sn);
					String snString=tv.getText().toString();
					ContentValues values=new ContentValues();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentTime=sdf.format(new Date(System.currentTimeMillis()));
		            values.put("time", currentTime);
		            db.update("search_history", values, "sn=?", new String[]{snString});//update time of specific serial number
					ResultActivity.actionStart(MainActivity.this, snString);
					finish();
				}
			  });
			  
			  InitSwipeMenuListView();
			  
			  searchHistoryListview.setOnMenuItemClickListener(new OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						// TODO Auto-generated method stub
						switch(index){
						case 0:
							List<SearchHistoryInfo> searchHistoryListTemp=new ArrayList<SearchHistoryInfo>();
					        Cursor cursor=db.query("search_history",null,null,null,null,null,null);
					        if(cursor.moveToFirst()){
					        	do{
					         		String serialNum=cursor.getString(cursor.getColumnIndex("sn"));
					        		String timeNow=cursor.getString(cursor.getColumnIndex("time"));
					         		SearchHistoryInfo searchHistoryInfo=new SearchHistoryInfo(serialNum,timeNow);
					                searchHistoryListTemp.add(searchHistoryInfo);
					           	  }while(cursor.moveToNext());
					          }
					          cursor.close(); 
					          
							SearchHistoryInfo searchHistoryInfoSN=SearchHistoryAdapter.getItem(position,searchHistoryListTemp);
							
							//TextView tv=(TextView)findViewById(R.id.search_history_sn);
							String snString=searchHistoryInfoSN.getSearchSN();
							db.delete("search_history", "sn=?", new String[]{snString});
							searchHistoryListTemp.clear();
							  
							Cursor cursorAfterDelete=db.query("search_history",null,null,null,null,null,null);
					        if(cursorAfterDelete.moveToFirst()){
					        	do{
					         		String serialNum=cursorAfterDelete.getString(cursorAfterDelete.getColumnIndex("sn"));
					        		String timeNow=cursorAfterDelete.getString(cursorAfterDelete.getColumnIndex("time"));
					         		SearchHistoryInfo searchHistoryInfo=new SearchHistoryInfo(serialNum,timeNow);
					                searchHistoryListTemp.add(searchHistoryInfo);
					           	  }while(cursorAfterDelete.moveToNext());
					          }
					          cursorAfterDelete.close();
					          
					        if(cursorAfterDelete.getCount()>0){
					        	SearchHistoryAdapter adapter=new SearchHistoryAdapter(MainActivity.this,R.layout.search_history_display,searchHistoryListTemp);
					            searchHistoryListview.setAdapter(adapter);
					        }else{
					        	searchHistoryListview.setVisibility(View.INVISIBLE);
								clearHistoryButton.setVisibility(View.INVISIBLE);
					        }
					          
							break;

						}
						return false;
					}
					  
				  });
			  
		      clearHistoryButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/**
					 * alert dialog indication before delete all the search history data in db
					 */
					AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
					builder.setTitle("系统提示");
					builder.setMessage("确认删除全部查询记录？");
					builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
							
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							db.delete("search_history", null, null);
							searchHistoryList.clear();

							clearHistoryButton.setVisibility(View.INVISIBLE);
							searchHistoryListview.setVisibility(View.INVISIBLE);
							}
						});
					builder.setNegativeButton("取消",null);
						
					builder.show();
				}
			}); 
		        
		  }else{
			  searchHistoryListview.setVisibility(View.INVISIBLE);
			  clearHistoryButton.setVisibility(View.INVISIBLE);
		  }
                    
    }
    
    private void InitSwipeMenuListView(){
		  
  	  searchHistoryListview=(SwipeMenuListView)findViewById(R.id.search_history_listview);
		  SwipeMenuCreator creator=new SwipeMenuCreator(){
			  public void create(SwipeMenu menu){
				  SwipeMenuItem deleteItem=new SwipeMenuItem(getApplicationContext());
				  deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ff3030")));
				  deleteItem.setIcon(R.drawable.ic_action_discard);
				  deleteItem.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
							getBaseContext().getResources().getDisplayMetrics()));
				  
				  menu.addMenuItem(deleteItem);
				  				  
			  }
		  };
		  searchHistoryListview.setMenuCreator(creator);
		  
     }

}
