package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Config extends AppCompatActivity {
    EditText edit1;
    EditText edit2;
    EditText edit3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        edit1=findViewById(R.id.editTextTextPersonName);
        edit2=findViewById(R.id.editTextTextPersonName3);
        edit3=findViewById(R.id.editTextTextPersonName4);

        Intent intent=getIntent();
        double dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);
        double euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        double won2=intent.getFloatExtra("won_rate_key",0.0f);



        edit1.setText(String.valueOf(dollar2));
        edit2.setText(String.valueOf(euro2));
        edit3.setText(String.valueOf(won2));


    }
    public void save(View v){
        edit1=findViewById(R.id.editTextTextPersonName);
        edit2=findViewById(R.id.editTextTextPersonName3);
        edit3=findViewById(R.id.editTextTextPersonName4);
        float newDollar=Float.parseFloat(edit1.getText().toString());
        float newEuro=Float.parseFloat(edit2.getText().toString());
        float newWon=Float.parseFloat(edit3.getText().toString());
        //保存到Bundle或放入到Extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);//设置resultCode及带回的数据



        //获取SharedPreferences对象，修改保存内容
        SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();

        editor.putFloat("dollar_rate",newDollar);
        editor.putFloat("euro_rate",newEuro);
        editor.putFloat("won_rate",newWon);
        editor.apply();

//返回到调用页面
        finish();
    }
}