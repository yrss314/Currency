package com.example.currency;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
public class RateListActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    Handler handler;
    ListView listview;
    float dollarRate = (float) 0.1465;
    float euroRate = (float) 0.1259;
    float wonRate = (float) 171.7179;
    ArrayList<HashMap<String, String>> listItems;
    SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //ListView listView = (ListView) findViewById(R.id.mylist1);

        //String[] list_data={"one","two","three","four","five"};


        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_data);
        //listView.setAdapter(adapter);
        listview = (ListView) findViewById(R.id.my_list);
        Thread t1= new Thread(this);
        t1.start();

        handler = new Handler() {


            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    //List<String> list2 = (List<String>) msg.obj;
                    //ListAdapter adapter = new ArrayAdapter<String>(
                            //RateListActivity.this,
                            //android.R.layout.simple_list_item_1,
                            //list2);
                    //setListAdapter(adapter);

                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    // 生成适配器的 Item 和动态数组对应的元素
                    System.out.println(list2);
                    listItemAdapter = new SimpleAdapter(RateListActivity.this,
                            list2, // listItems 数据源
                            R.layout.hangbuju, // ListItem 的 XML 布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );
                    //setListAdapter(listItemAdapter);
                    listview.setAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };


        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);


    }

    @Override
    public void run() {
        //handler.postDelayed(this, 86400000);
        //Message msg = handler.obtainMessage(5);
        //msg.obj="Hello from run()";
        //handler.sendMessage(msg);


        HashMap<String, String> temp = new HashMap<String, String>();
        //List<String> temp=null;

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
            listItems = new ArrayList<HashMap<String, String>>();



            //System.out.println("通过Map.keySet遍历key和value：");
            for (String key : temp.keySet()) {
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("ItemTitle", key); // 标题文字
                map.put("ItemDetail", temp.get(key)); // 详情描述
                listItems.add(map);

            }
            System.out.println(listItems);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
        msg.obj=listItems;
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

    public HashMap<String, String>  useJsoup(String str) {
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

        HashMap<String, String> map = new HashMap<String, String>();

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
            String v1=Float.toString(v);
            String prior=str1+"==>"+v;
            form.add(prior);

            map.put(str1,v1);

        }
        //return form;
        return map;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = listview.getItemAtPosition(position);
        //通过map获取数据
        //HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        //String titleStr = map.get("ItemTitle");
        //String detailStr = map.get("ItemDetail");
        //Log.i("thread", "onItemClick: titleStr=" + titleStr);
        //Log.i("thread", "onItemClick: detailStr=" + detailStr);

        //通过view获取数据
        TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i("thread", "onItemClick: title2=" + title2);
        Log.i("thread", "onItemClick: detail2=" + detail2);

        Intent intent=new Intent(this,Calculation.class);
        Bundle bdl = new Bundle();
        bdl.putString("currency",title2);
        bdl.putString("rate",detail2);

        intent.putExtras(bdl);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("thread", "onClick: 对话框事件处理");
                        //删除数据项
                        listItems.remove(position);
                        //更新适配器
                        listItemAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
        return true;
    }
}