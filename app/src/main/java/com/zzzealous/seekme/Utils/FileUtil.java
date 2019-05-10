package com.zzzealous.seekme.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.zzzealous.seekme.Bean.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zzzealous on 2018/3/4.
 */

public class FileUtil {
    /**
     * 一级目录 SeekMe
     */
    public static String  FIRST_DIR_PATH = Environment.getExternalStorageDirectory().getPath()
             + File.separator + "seekme";

    /**
     * 二级目录 图片images
     */
    private static String  IMAGES_DIR_PATH = FIRST_DIR_PATH + File.separator + "images";


    /**
     * @param filePath
     * @return first-filename, second-filedir
     */

    public static Pair<String, String> getImagesDirPath (String filePath)
    {
        Pair<String,String> res = new Pair<>();
        // 新建一级目录
        File firstDir = new File(FIRST_DIR_PATH);
        if(!firstDir.exists()) {
            firstDir.exists();
        }
        //建立二级图片目录
        File imagedir = new File(IMAGES_DIR_PATH);
        if (! imagedir.exists()) {
            imagedir.mkdir();
        }
        //三级分类目录
        String saveDir = IMAGES_DIR_PATH + File.separator + filePath;
        // 生成文件名
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
        String fileName =  filePath + (t.format(new Date()))+".jpg";
        res.setFirst(fileName);
        res.setSecond(saveDir);
        return res;
    }

    /**
     * 保存图片
     * @param bitmap
     * @param filePath
     * @return first-fileName, second-filePath
     */
    public static Pair<String, String> saveBitmapToFile(Bitmap bitmap, String filePath){
        Pair<String, String> res = new Pair<>();
        String picPath = null;
        String fileName = null;
        FileOutputStream fileOutputStream = null;
        try {

            // 新建一级目录
            File firstDir = new File(FIRST_DIR_PATH);
            if(!firstDir.exists()) {
                firstDir.exists();
            }
            //建立二级图片目录
            File imagedir = new File(IMAGES_DIR_PATH);
            if (! imagedir.exists()) {
                imagedir.mkdir();
            }
            //三级分类目录
            String saveDir = IMAGES_DIR_PATH + File.separator + filePath;
            File dir = new File(saveDir);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            // 生成文件名
            SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
            fileName =  filePath + (t.format(new Date()))+".jpg";
            Log.i("TAG","文件名"+fileName);
            // 新建文件
            File file = new File(saveDir, fileName);
            // 打开文件输出流
            fileOutputStream = new FileOutputStream(file);
            Log.i("TAG","文件输出流");
            // 生成图片文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            // 相片的完整路径
            picPath = file.getPath();

            Log.i("TAG","图片存储成功"+picPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        res.setFirst(fileName);
        res.setSecond(picPath);
        return  res;
    }

    /**
     * 根据路径，从文件中获取图片
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromFile(String path) {

        Bitmap bitmap = null;

        File file = new File(path);

        if(file.exists()) {
            bitmap = BitmapFactory.decodeFile(path);
        }

        return bitmap;
    }

    /**
     * 根据路径，删除图片
     * @param path
     */
    public static void deleteBitmapByPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 根据时间创建个图片名
     * @return
     */
    public static String getPicName(){
        String fileName;
        Calendar calendar=Calendar.getInstance();
        String yyyy, MM, dd, hh , mm ,ss;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        yyyy = year+"";

        if(month<10)
            MM = "0" + month;
        else
            MM = month+"";

        if(day<10)
            dd = "0" + day;
        else
            dd = day+"";

        if(hour>=0 && hour<10)
            hh = "0" + hour;
        else
            hh = hour+"";

        if(minute>=0 && minute<10)
            mm = "0" + minute;
        else
            mm = minute+"";

        if(second >=0 && second<10)
            ss = "0" + second;
        else
            ss = second+"";

        fileName = yyyy+MM+dd+hh+mm+ss;

        return fileName;
    }

}
