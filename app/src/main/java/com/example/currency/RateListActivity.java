package com.example.currency;


import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//public class List extends ListActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list);
 //       java.util.List<String> list1=new ArrayList<String>();
  //      for(int i=1;i<100;i++){
//            list1.add("item"+i);
//        }

//        String[] list_data={"one","two","three","four"};
//        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_data);
//        setListAdapter(adapter);
//    }
//}
public class RateListActivity extends ListActivity implements Runnable {
    Handler handler;
    //ListView listview;
    float dollarRate = (float) 0.1465;
    float euroRate = (float) 0.1259;
    float wonRate = (float) 171.7179;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list);
        //ListView listView = (ListView) findViewById(R.id.mylist1);

        //String[] list_data={"one","two","three","four","five"};


        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_data);
        //listView.setAdapter(adapter);
        Thread t1= new Thread(this);
        t1.start();

        handler = new Handler() {


            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(
                            RateListActivity.this,
                            android.R.layout.simple_list_item_1,
                            list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        //handler.postDelayed(this, 86400000);
        //Message msg = handler.obtainMessage(5);
        //msg.obj="Hello from run()";
        //handler.sendMessage(msg);
        List<String> temp=null;

        URL url = null;
        try {
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i("thread", "run:html=" + html);

            //Message msg = handler.obtainMessage(5);
            temp=useJsoup(html);
            //handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
        msg.obj=temp;
        handler.sendMessage(msg);
    }


    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public List<String>  useJsoup(String str) {
        //Document doc = Jsoup.parse(str);
        //Elements trs=doc.select("table").get(0).select("tr");

        //Elements t1=trs.get(7).select("td");//货币
        //String t1str=t1.get(0).text();
        //String t11=t1.get(5).text();//折算价
        //euroRate=100f/Float.parseFloat(t11);
        //Log.i("thread","run"+t1str+"==>"+euroRate);

        //Elements t2=trs.get(13).select("td");
        //String t2str=t2.get(0).text();
        //String t22=t2.get(5).text();
        //wonRate=100f/Float.parseFloat(t22);
        //Log.i("thread","run"+t2str+"==>"+wonRate);

        //Elements t3=trs.get(26).select("td");
        //String t3str=t3.get(0).text();
        //String t33=t3.get(5).text();
        //dollarRate=100f/Float.parseFloat(t33);
        //Log.i("thread","run"+t3str+"==>"+dollarRate);
        //把数据转为字符串，然后输出到日志之中

        List<String> form=new ArrayList();
        Document doc = Jsoup.parse(str);
        Elements tables = doc.getElementsByTag("table");
        Element table6 = tables.get(0);
        Elements tds = table6.getElementsByTag("td");
        for (int i = 0; i < tds.size(); i += 6) {
            Element td1 = tds.get(i);
            Element td2 = tds.get(i + 5);
            String str1 = td1.text();
            String val = td2.text();
            Log.i("thread", "run: " + str1 + "==>" + val);
            float v = 100f / Float.parseFloat(val);
            String prior=str1+"==>"+val;
            form.add(prior);

        }
        return form;

    }
}