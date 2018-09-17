package com.wangkeke.apkupdatefast;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fast.kk.apkupdatelib.OnDownloadProgressListener;
import com.fast.kk.apkupdatelib.RxApkUpdate;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    String url = "http://www.eoemarket.com/download/124218_0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();
    }

    private void requestPer() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean){
                            initView();
                        }
                    }
                });
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
