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

    public static final String COMMAND_BACKUP = "backupDatabase";
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

        @SuppressLint("SdCardPath") File dbFile = mContext.getDatabasePath("/data/data/com.xuexiang.cdaccount/databases/cdaccount.db");
        @SuppressLint("SdCardPath") File exportDir = mContext.getDatabasePath("/data/data/com.xuexiang.cdaccount/backup");

        // 路径不存在，创建文件夹
        if (!exportDir.exists()) {
            boolean tmp =  exportDir.mkdirs();
        }

        // 备份文件名
        String BACKUP_NAME = "BACKUP.db";
        File backup = new File(exportDir, BACKUP_NAME);

        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                boolean tmp = backup.createNewFile();
                Log.d("backup!!!", "!!!!!");
                fileCopy(dbFile, backup);
                return 0;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return 1;
            }
        }
        else if (command.equals(COMMAND_RESTORE)) {
            Log.d("here", "hesr is ok");
            try {
                fileCopy(backup, dbFile);
                return 0;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return -1;
            }
        } else {
            return -1;
        }
    }


    /**
     * 复制文件
     * @param dbFile 源文件
     * @param backup 目标文件
     * @throws IOException IO异常
     */
    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        try (FileChannel inChannel = new FileInputStream(dbFile).getChannel(); FileChannel outChannel = new FileOutputStream(backup).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
