package com.katya.katyacepfeneri;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UncaughtExceptionActivity extends Activity {

    TextView error;

    public UncaughtExceptionActivity (){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_uncaughtexception);

        error = (TextView) findViewById(R.id.error);

        error.setText(getIntent().getStringExtra("error"));
    }
}
