package com.fast.kk.apkupdatelib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Android运行linux命令
 */
public final class RootInstallHelp {
    private static final String TAG = "RootCmd";
    private static boolean mHaveRoot = false;

    //隐藏导航栏
    public static String CLOSE_NAVIGATION = "settings put global policy_control immersive.navigation=*";

    //隐藏状态栏
    public static String CLOSE_STATUS_BAR = "settings put global policy_control immersive.status=*";

    //隐藏导航栏和状态栏
    public static String CLOSE_ALL_BAR = "settings put global policy_control immersive.full=*";

    //恢复默认状态
    public static String RESET_DEFAULT_BAR = "settings put global policy_control immersive.status=*";

    /**
     * 判断机器Android是否已经root，即是否获取root权限
     */
    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                Log.i(TAG, "have root!");
                mHaveRoot = true;
            } else {
                Log.i(TAG, "not root!");
            }
        } else {
            Log.i(TAG, "mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }

    /**
     * 执行命令并且输出结果
     */
    public static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 执行命令但不关注结果输出
     */
    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean installByRoot(File file) {

        if(!haveRoot()){
            throw new IllegalArgumentException("The device does not have root privileges and cannot be installed automatically.");
        }

        String filePath;
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        } else {
            filePath = file.getAbsolutePath();
        }

        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("Please check apk file path!");
        } else {
            boolean result = false;
            Process process = null;
            OutputStream outputStream = null;
            BufferedReader errorStream = null;

            try {
                process = Runtime.getRuntime().exec("su");
                outputStream = process.getOutputStream();
                String command = "pm install -r " + filePath + "\n";
                outputStream.write(command.getBytes());
                outputStream.flush();
                outputStream.write("exit\n".getBytes());
                outputStream.flush();
                process.waitFor();
                errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder msg = new StringBuilder();

                String line;
                while ((line = errorStream.readLine()) != null) {
                    msg.append(line);
                }

                Log.e("install", "install msg is " + msg);
                if (!msg.toString().contains("Failure")) {
                    result = true;
                }
            } catch (Exception var17) {
                Log.e("install", var17.getMessage(), var17);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }

                    if (errorStream != null) {
                        errorStream.close();
                    }
                } catch (IOException var16) {
                    outputStream = null;
                    errorStream = null;
                    process.destroy();
                }

            }

            return result;
        }
    }
}
