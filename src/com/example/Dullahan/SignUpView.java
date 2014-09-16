package com.example.Dullahan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2014-09-14.
 */
public class SignUpView extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Button button=(Button)findViewById(R.id.signup);
        button.setOnClickListener(new signupbuttonlistener());

    }

    String username=null;
    String pw1=null;
    String pw2=null;

    class signupbuttonlistener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            EditText editText1=(EditText)findViewById(R.id.signname);
            EditText editText2=(EditText)findViewById(R.id.signpw1);
            EditText editText3=(EditText)findViewById(R.id.signpw2);
            username=editText1.getText().toString();
            pw1=editText2.getText().toString();
            pw2=editText3.getText().toString();
//            System.out.println(username+"~~~"+pw1+"~~~"+pw2);
//            System.out.println(pw1.equals("")+"~~~");
//            System.out.println(pw1==null);
            if(username==null||username.equals("")){
                Toast.makeText(SignUpView.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!pw1.equals(pw2)){
                Toast.makeText(SignUpView.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                return;
            }
            if (pw1.equals("")||pw2.equals("")||pw1==null||pw2==null){
                Toast.makeText(SignUpView.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            new Signup().execute();
        }
    }

    class Signup extends AsyncTask<Void,Integer,Boolean>{
        String setResult=null;

        @Override
        protected Boolean doInBackground(Void... params) {
            String uri=HostUri.value()+"/signup";
            HttpPost httpPost=new HttpPost(uri);
            List<NameValuePair> list =new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("username",username));
            list.add(new BasicNameValuePair("password",pw2));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpPost);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    setResult= EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(setResult.equals("<script>alert(\"用户名已存在\")window.location=\"signup.html\"</script>")){
                Toast.makeText(SignUpView.this,"用户名已存在",Toast.LENGTH_SHORT).show();
            }
            if(setResult.equals("<script>window.location=\"welcomepage.html\"</script>")){
                System.out.println(setResult);
                super.onPostExecute(aBoolean);
                Toast.makeText(SignUpView.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(SignUpView.this,MyActivity.class);
                SignUpView.this.startActivity(intent);
            }
            else{
                Toast.makeText(SignUpView.this,"请求服务器出错",Toast.LENGTH_SHORT).show();
            }

        }


    }


}
