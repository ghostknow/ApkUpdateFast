package com.fast.kk.apkupdatelib;

import java.io.File;

/**
 * 下载进度监听
 */
public abstract class OnDownloadProgressListener {

    public abstract void onProgress(long total, long progress, int percent);

    public void onDownloadSuccess(File file){}
}
