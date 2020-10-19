package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Calculation extends AppCompatActivity {
    private static final String TAG= "Test";
    EditText mEditText;
    TextView mTextView,currencyView;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        Intent intent=getIntent();
        final String currency=intent.getStringExtra("currency");
        String temp=intent.getStringExtra("rate");
        rate=Float.parseFloat(temp);

        mEditText = (EditText) findViewById(R.id.inputNumber);
        mTextView = (TextView) findViewById(R.id.textView5);
        currencyView= (TextView) findViewById(R.id.textView4);

        currencyView.setText(currency);
        mEditText.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d(TAG, "afterTextChanged");

                float input=Float.parseFloat(s.toString());
                float result=input*rate;
                mTextView.setText("="+result+currency);






                //if(s.length()>0){
                    //float input=Float.parseFloat(s.toString());
                    //float result=input*rate;
                    //mTextView.setText(input+"RMB==>"+result);

                //}
                //else {
                    //mTextView.setText("");
                //}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Log.d(TAG, "beforeTextChanged:" + s + "-" + start + "-" + count + "-" + after);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //Log.d(TAG, "onTextChanged:" + s + "-" + "-" + start + "-" + before + "-" + count);

            }

        });

    }
}