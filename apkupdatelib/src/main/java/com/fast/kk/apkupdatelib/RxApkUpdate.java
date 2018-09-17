package com.fast.kk.apkupdatelib;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class RxApkUpdate {

    /**
     * 下载进度监听，自己处理
     */
    public static void downLoadApkFast(String url, final OnDownloadProgressListener listener) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("download url can't be null");
        }

        if (null == listener) {
            throw new IllegalArgumentException("listener can't be null");
        }

        String fileName = getRandomName();
        File file = getDefaultPathFile(fileName);

        downLoadFileByListener(url, fileName, file, false, listener);
    }


    /**
     * 下载完成，是否自动弹出安装页面
     */
    public static void downLoadApkFast(String url, boolean isShowInstallPage, final OnDownloadProgressListener listener) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("download url can't be null");
        }

        if (null == listener) {
            throw new IllegalArgumentException("listener can't be null");
        }

        String fileName = getRandomName();
        File file = getDefaultPathFile(fileName);

        downLoadFileByListener(url, fileName, file, isShowInstallPage, listener);
    }

    /**
     * url: 下载地址
     * fileName：文件名(需待后缀，如：test.apk)
     * file: 文件下载保存路径
     * isShowInstallPage: 自动弹出安装页面
     * listener: 下载进度监听
     */
    public static void downLoadFileByListener(String url, String fileName, File file, final boolean isShowInstallPage, final OnDownloadProgressListener listener) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("download url can't be null");
        }

        if (null == listener) {
            throw new IllegalArgumentException("listener can't be null");
        }

        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName can't be null");
        }

        if (null == file) {
            throw new IllegalArgumentException("file can't be null");
        }

        RetrofitFactory.getDownloadService().downLoadFile(url)
                .compose(ioMainDownload())
                .subscribeWith(new FileDownLoadSubscriber(file) {
                    @Override
                    public void onSuccess(File file) {
                        listener.onDownloadSuccess(file);
                        if (isShowInstallPage) {
                            installApk(file);
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        ToastUtils.toastShow(msg);
                    }

                    @Override
                    public void onProgress(long progress, long total) {
                        if (total <= 0) {
                            //后台需要设置相关参数
                            ToastUtils.toastShow("获取文件长度失败，请服务器端设置！");
                            return;
                        }
                        int percent = (int) (progress * 100 / total);
                        listener.onProgress(total, progress, percent);
                    }
                });
    }

    @NonNull
    private static File getDefaultPathFile(String fileName) {
        String fileStoreDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "apkUpdate";

        File file = new File(fileStoreDir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static FlowableTransformer<ResponseBody, ResponseBody> ioMainDownload() {
        return new FlowableTransformer<ResponseBody, ResponseBody>() {
            @Override
            public Publisher<ResponseBody> apply(Flowable<ResponseBody> upstream) {
                return upstream.subscribeOn(Schedulers.io()).
                        observeOn(Schedulers.io()).
                        observeOn(Schedulers.computation()).
                        map(new Function<ResponseBody, ResponseBody>() {
                            @Override
                            public ResponseBody apply(ResponseBody responseBody) throws Exception {
                                return responseBody;
                            }
                        }).
                        observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 设置默认apk名称
     *
     * @return
     */
    private static String getRandomName() {
        String currentTimeStr = "" + System.currentTimeMillis();
        return currentTimeStr + "_update.apk";
    }

    /**
     * <安装>
     * 兼容android7.0
     */
    private static void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //Android 7.0及以上
            // 参数2 清单文件中provider节点里面的authorities ; 参数3  共享的文件,即apk包的file类
            Uri apkUri = FileProvider.getUriForFile(ApkApplication.getInstance(), "com.fast.kk.fileprovider", file);
            //对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        ApkApplication.getInstance().startActivity(intent);
    }
}
