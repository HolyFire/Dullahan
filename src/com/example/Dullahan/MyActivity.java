package com.example.Dullahan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private String username=null;
    private String password=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏 这句需要在setContentView之前使用才能生效。

        setContentView(R.layout.main);
        final RelativeLayout answer_add=(RelativeLayout)findViewById(R.id.answer_add);

//        隐藏输入法键盘
        answer_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                answer_add.setFocusable(true);
                answer_add.setFocusableInTouchMode(true);
                answer_add.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                return false;
            }
        });

        Button myButton=(Button)findViewById(R.id.button);
        myButton.setOnClickListener(new MybuttonListener());
        Button signupbutton=(Button)findViewById(R.id.button2);
        signupbutton.setOnClickListener(new SignupbuttonListener());
    }

    class SignupbuttonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MyActivity.this,SignUpView.class);
            MyActivity.this.startActivity(intent);
        }
    }

    class MybuttonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            EditText editText1=(EditText)findViewById(R.id.editText);
            EditText editText2=(EditText)findViewById(R.id.editText2);
            username=editText1.getText().toString();
            password=editText2.getText().toString();
            ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=cm.getActiveNetworkInfo();
            if(info!=null){
//                Toast.makeText(MyActivity.this, "连网正常" + info.getTypeName(), Toast.LENGTH_SHORT).show();
                    Checksign checksign=new Checksign();
                    checksign.execute();

            }else{
                Toast.makeText(MyActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }

        }

        class Checksign extends AsyncTask<Void,Integer,Boolean>{

            public Boolean getFlag() {
                return flag;
            }

            public void setFlag(Boolean flag) {
                this.flag = flag;
            }

            public String content=null;

            private Boolean flag=false;

            @Override
            protected void onPreExecute() {

            }


                @Override
            protected Boolean doInBackground(Void... params) {

                    try {
                        String uriAPI = HostUri.value()+"/login";  //声明网址字符串
                        HttpPost httpRequest = new HttpPost(uriAPI);   //建立HTTP POST联机
//                        httpRequest.setHeader("Content-type","application/x-java-serialized-object");
//                        HttpGet httpRequest=new HttpGet("http://192.168.1.101:8080/Atest");

                        List<NameValuePair> params1 = new ArrayList<NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存
                        params1.add(new BasicNameValuePair("username", username));
                        params1.add(new BasicNameValuePair("pw", password));
                        httpRequest.setEntity(new UrlEncodedFormEntity(params1, HTTP.UTF_8));   //发出http请求
                        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);   //取得http响应

                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            String strResult = EntityUtils.toString(httpResponse.getEntity());   //获取字符串
                            content=strResult;

                            if(strResult.equals("index")){
                                flag = true;
                            }
                            content=strResult;
                        }else {
                            flag =false;
                        }
                    }catch (Exception e){
                        flag=false;

                    }
                    return flag;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                if(flag){
                    Intent intent=new Intent();
                    intent.setClass(MyActivity.this,SecondActivity.class);
                    intent.putExtra("Welcome",username);
                    MyActivity.this.startActivity(intent);
//                    Toast.makeText(MyActivity.this,content,Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MyActivity.this,content,Toast.LENGTH_SHORT).show();
                }
            }

            }





    }

}
