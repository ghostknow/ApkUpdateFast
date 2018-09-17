package com.fast.kk.apkupdatelib;

/**
 * @author by 王可可
 * @version 1.0
 */
public class FileLoadingBean {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已下载大小
     */
    long progress;

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadingBean(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}