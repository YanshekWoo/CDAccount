package com.xuexiang.cdaccount.somethingDao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupTask extends AsyncTask<String, Void, Integer> {

    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restoreDatabase";
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;

    public BackupTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        // 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db　　　　 // 默认路径是 /data/data/(包名)/databases/*.db
        @SuppressLint("SdCardPath") File dbFile = mContext.getDatabasePath(
                "/data/data/com.example.myapplication/databases/jile.db");
        @SuppressLint("SdCardPath") File exportDir = mContext.getDatabasePath("/data/data/com.example.myapplication/files");
        boolean tmp2 = true;
        if (!exportDir.exists()) {
            tmp2 =  exportDir.mkdirs();
            Log.d("9","9"+tmp2);
        }
        if(!exportDir.exists()){
            System.out.println("1文件夹不存在------------"+exportDir);
            System.out.println("2文件夹不存在------------"+dbFile);
            Log.d("!!!!!!!!!!!!!!!!!!!!!!", "!");
        }
        String tmp = exportDir.getName();
        boolean tmp1 = true;
        Log.d("!!!","!!! "+tmp);
        File backup = new File(exportDir, dbFile.getName());
        // File backup = exportDir;
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                tmp1 = backup.createNewFile();
                Log.d("backup!!!", "!!!!!");
                fileCopy(dbFile, backup);
                return Log.d("backup!!", "ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("backup!!!", "fail    "+tmp1);
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                return Log.d("restore!!", "success");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("restore!!!", "fail");
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("trans error","!!!!");
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

}
