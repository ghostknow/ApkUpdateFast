package com.fast.kk.apkupdatelib;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    String url = "http://www.eoemarket.com/download/124218_0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxApkUpdate.downLoadApkFast(url,true, new OnDownloadProgressListener() {
                    @Override
                    public void onProgress(long total, long progress, int percent) {
                        Log.e("download", "total : " + total + "  progress : " + progress + "  percent : " + percent);
                        progressBar.setProgress(percent);
                    }
                });
            }
        });

    }
}
