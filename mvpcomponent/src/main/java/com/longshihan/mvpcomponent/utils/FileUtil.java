package com.longshihan.mvpcomponent.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/7/25
 * owspace
 */
public class FileUtil {
    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String ADPATH = FileUtil.SDPATH + "/testart";

    public static void createSdDir() {
        File file = new File(FileUtil.ADPATH);
        if (!file.exists()) {
            boolean create = file.mkdirs();
            Logger.d("create = " + create);
        } else {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdir();
            }
        }
    }
    public static boolean isFileExist(String paramString) {
        if (paramString == null)
            return false;
        File localFile = new File(ADPATH + "/" + paramString);
        if (localFile.exists()) {
            return true;
        }
        return false;
    }
    public static File createFile(String fileName) throws IOException{
        File file = new File(ADPATH,fileName);
        file.createNewFile();
        return file;
    }
    public static List<String> getAllAD(){
        File file = new File(FileUtil.ADPATH);
        File[] fileList = file.listFiles();
        List<String> list = new ArrayList<>();
        if(null != fileList){
            for (File f:fileList) {
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }

    public static Uri saveBitmapToSDCard(Bitmap bitmap, String title) {
        File appDir = new File(ADPATH+"/Gank");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = title.replace("/","-") + "-girl.jpg";
        File file = new File(appDir,fileName);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(file);
    }
}
