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
public class PostView extends Activity{

    String content=null;
    String clientuser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        Intent intent=getIntent();
        clientuser=intent.getStringExtra("Welcome");
        System.out.println(clientuser);
        Button postbutton=(Button)findViewById(R.id.postmes);
        postbutton.setOnClickListener(new PostbuttonListener());

    }

    class PostbuttonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            EditText editText=(EditText)findViewById(R.id.PostText);
            content=editText.getText().toString();
            if(content.length()<10){
                new PostMessage().execute();
            }
        }
    }

    class PostMessage extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            String Uri=HostUri.value()+"/index.html";
            HttpPost httpPost=new HttpPost(Uri);
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("chatText",content));
            list.add(new BasicNameValuePair("username",clientuser));
            System.out.println(list);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpPost);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String setResult= EntityUtils.toString(httpResponse.getEntity());
                    System.out.println(setResult);
                    return true;
                }else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Intent intent=new Intent();
                intent.setClass(PostView.this,SecondActivity.class);
                PostView.this.startActivity(intent);
            }else{
                Toast.makeText(PostView.this,"提交失败",Toast.LENGTH_SHORT);
                Intent intent=new Intent();
                intent.setClass(PostView.this,SecondActivity.class);
                PostView.this.startActivity(intent);
            }
        }
    }
}
