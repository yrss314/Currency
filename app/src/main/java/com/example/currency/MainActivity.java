package com.example.currency;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity implements Runnable {
    TextView out;
    EditText edit;
    //SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
    //SharedPreferences.Editor editor=sp.edit();
    float dollarRate= (float) 0.1465;
    float euroRate= (float) 0.1259;
    float wonRate= (float) 171.7179;
    //editor.putFloat("dollar_rate",dollarRate);
    //editor.putFloat("euro_rate",euroRate);
    //editor.putFloat("won_rate",wonRate);
    //editor.apply();
    Handler handler;
    //Runnable aa=new MainActivity();
    DBHelper dbHelp;
    SQLiteDatabase sdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //handler.postDelayed(this, 1000);
        dbHelp =new DBHelper(MainActivity.this);
        sdb = dbHelp.getWritableDatabase(); // 创建 or 打开 可读/写的数据库

        out=findViewById(R.id.out);
        edit=findViewById(R.id.editTextTextPersonName2);

        Thread t=new Thread(this);
        t.start();

        handler=new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==5){
                    String str =(String)msg.obj;
                    Log.i("thread","handleMessage: getMessage msg = "+str);
                }
            }
        };
        SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();

        editor.putFloat("dollar_rate",dollarRate);
        editor.putFloat("euro_rate",euroRate);
        editor.putFloat("won_rate",wonRate);
        editor.apply();



    }

    public void dollar(View btn){
        SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        dollarRate=sp.getFloat("dollar_rate",0.0f);
        euroRate=sp.getFloat("euro_rate",0.0f);
        wonRate=sp.getFloat("won_rate",0.0f);

        Toast.makeText(this,"Hello msg",Toast.LENGTH_SHORT).show();
        float rmb=Float.parseFloat(edit.getText().toString());
        //Log.i(TAG,"abc:onClicked");
        if(btn.getId()==R.id.button){
            //dollar

            float outDollar=rmb*dollarRate;
            String out1=String.valueOf(outDollar);
            out.setText(out1);
        }else if (btn.getId()==R.id.button2){
            //euro

            float outDollar=rmb*euroRate;
            String out1=String.valueOf(outDollar);
            out.setText(out1);
        }else{
            //won
            float outDollar=rmb*wonRate;
            String out1=String.valueOf(outDollar);
            out.setText(out1);
        }

    }
    public void open(View btn){
        //config activity

        Intent intent=new Intent(this,Config.class);
        //config.putExtra("dollar_rate_key",dollarRate);
        //config.putExtra("euro_rate_key",euroRate);
        //config.putExtra("won_rate_key",wonRate);


        //startActivity(config);
        //startActivityForResult(config,1);
        ///栈
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate_key",dollarRate);
        bdl.putFloat("euro_rate_key",euroRate);
        bdl.putFloat("won_rate_key",wonRate);
        intent.putExtras(bdl);
        startActivityForResult(intent, 1);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void useJsoup(String str){
        Document doc = Jsoup.parse(str);
        Elements trs=doc.select("table").get(0).select("tr");

        Elements t1=trs.get(7).select("td");//货币
        String t1str=t1.get(0).text();
        String t11=t1.get(5).text();//折算价
        euroRate=100f/Float.parseFloat(t11);
        Log.i("thread","run"+t1str+"==>"+euroRate);

        Elements t2=trs.get(13).select("td");
        String t2str=t2.get(0).text();
        String t22=t2.get(5).text();
        wonRate=100f/Float.parseFloat(t22);
        Log.i("thread","run"+t2str+"==>"+wonRate);

        Elements t3=trs.get(26).select("td");
        String t3str=t3.get(0).text();
        String t33=t3.get(5).text();
        dollarRate=100f/Float.parseFloat(t33);
        Log.i("thread","run"+t3str+"==>"+dollarRate);
        //把数据转为字符串，然后输出到日志之中

    }
    @Override
    public void run() {
        //handler.postDelayed(this, 86400000);
        Message msg=handler.obtainMessage(5);
        msg.obj="Hello from run()";
        handler.sendMessage(msg);


        URL url=null;
        try{
            url=new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http=(HttpURLConnection)url.openConnection();
            InputStream in=http.getInputStream();

            String html=inputStream2String(in);
            Log.i("thread","run:html="+html);

            useJsoup(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in =new InputStreamReader(inputStream,"gb2312");
        while(true){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

}