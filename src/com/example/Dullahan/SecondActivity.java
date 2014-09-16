package com.example.Dullahan;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2014-09-10.
 */
public class SecondActivity extends Activity {

    LinearLayout layout2;
    TextView moremes;
    String clientuser=null;
    Menu mMenu=null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mene, menu);
        return true;
    }
    private void hiddenEditMenu(){
        if(null != mMenu){
//          for (int i = 0; i < mMenu.size(); i++){
//              mMenu.getItem(i).setVisible(false);
//              mMenu.getItem(i).setEnabled(false);
//          }
            mMenu.findItem(R.id.menu_add1).setVisible(false);
        }
    }

    private void showEditMenu(){
        if(null != mMenu){
//          for (int i = 0; i < mMenu.size(); i++){
//              mMenu.getItem(i).setVisible(true);
//              mMenu.getItem(i).setEnabled(true);
//          }

            mMenu.findItem(R.id.menu_add1).setVisible(true);
       }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add1:
                //Toast.makeText(MainActivity.this, "Menu Discard cliked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(SecondActivity.this, PostView.class);
                System.out.println("后"+clientuser);
                intent.putExtra("Welcome",clientuser);
                SecondActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏 这句需要在setContentView之前使用才能生效。

//        getActionBar().setDisplayShowHomeEnabled(false);
//        去actionbar 图标

        setContentView(R.layout.second);
        layout2=(LinearLayout)findViewById(R.id.listlayout2);
        final LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2.setOrientation(LinearLayout.VERTICAL);
        moremes=(TextView)findViewById(R.id.moremes);
        moremes.setGravity(Gravity.CENTER);

        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        if(info!=null) {
            new ReadMessage().execute();
        }else{
            Toast.makeText(SecondActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
        }

//        TextView secondView=(TextView)findViewById(R.id.SecondView);
        Intent intent=getIntent();
        clientuser=intent.getStringExtra("Welcome");
        System.out.println("先"+clientuser);
//        secondView.setText(value);


    }
    class ReadMore implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=cm.getActiveNetworkInfo();
            if(info!=null) {
                new ReadMessage().execute();
            }else{
                Toast.makeText(SecondActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(SecondActivity.this,"有效果",Toast.LENGTH_SHORT).show();


        }
    }




//    AysncTask
private int count=0;
    class ReadMessage extends AsyncTask<Void,Integer,Boolean>{

        String strResult=null;
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String uriAPI = "http://192.168.1.101:8080/Atest2?count="+count;  //声明网址字符串
                HttpGet httpRequest = new HttpGet(uriAPI);   //建立HTTP POST联机
//                        httpRequest.setHeader("Content-type","application/x-java-serialized-object");
//                        HttpGet httpRequest=new HttpGet("http://192.168.1.101:8080/Atest");

                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);   //取得http响应

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());   //获取字符串
                }
            }catch (Exception e){

            }
            return null;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            try {
                JSONArray jsonArray=new JSONArray(strResult);
//                System.out.println("aaaaaaa");
//                System.out.println(jsonArray);

                if(jsonArray.length()==6){
                    for(int i=0;i<5;i++){
                        count++;
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    System.out.println(SecondActivity.class.getName()+jsonObject.getString("username"));
                        TextView textView=new TextView(SecondActivity.this);
                        textView.setText(count+"\t"+jsonObject.getString("username")+"\n"+jsonObject.getString("content"));
//            textView.setBackgroundColor(Color.RED);
                        layout2.addView(textView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        TextView textView2=new TextView(SecondActivity.this);
                        textView2.setBackgroundColor(Color.LTGRAY);
                        layout2.addView(textView2,LinearLayout.LayoutParams.MATCH_PARENT,2);
                    }

//                TextView textView=new TextView(SecondActivity.this);
//                textView.setTextColor(Color.RED);


                    moremes.setText("加载更多");
//                    textView.setText("共" + jsonArray.length() + "条留言");
//                    layout2.addView(textView,LinearLayout.LayoutParams.MATCH_PARENT,100);
                    moremes.setOnClickListener(new ReadMore());
                }else{
                    for(int i=0;i<jsonArray.length();i++){
                        count++;
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    System.out.println(SecondActivity.class.getName()+jsonObject.getString("username"));
                        TextView textView=new TextView(SecondActivity.this);
                        textView.setText(count+"\t"+jsonObject.getString("username")+"\n"+jsonObject.getString("content"));
//            textView.setBackgroundColor(Color.RED);
                        layout2.addView(textView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        TextView textView2=new TextView(SecondActivity.this);
                        textView2.setBackgroundColor(Color.LTGRAY);
                        layout2.addView(textView2,LinearLayout.LayoutParams.MATCH_PARENT,2);
                    }

//                TextView textView=new TextView(SecondActivity.this);
//                textView.setTextColor(Color.RED);

//                    TextView textView=new TextView(SecondActivity.this);
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setText("加载更多");
                    moremes.setText("共" + count + "条留言");
//                    layout2.addView(textView,LinearLayout.LayoutParams.MATCH_PARENT,100);
                    moremes.setOnClickListener(new ReadMore());
                }





            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(SecondActivity.this,strResult,Toast.LENGTH_SHORT).show();


        }
    }






}
