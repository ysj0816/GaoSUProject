package com.chuyu.gaosuproject.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;

import com.chuyu.gaosuproject.activity.SignActivity;

/**
 * Created by wo on 2017/7/14.
 */

public class PictureUtil {
    /**
     * 转换为64位字符串
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath, 480, 800);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 图片转成base64
     *
     * @param
     * @return
     */
    public static String bitmapToBase64(String filePath) {
        Bitmap bitmap = getSmallBitmap(filePath, 480, 800);
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        //base64 encode
        String encodeString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodeString;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;
        Log.i("test", "w:" + width + "**he:" + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath  图片的路径
     * @param reqWidth  要求的图片的像素
     * @param reqHeight 要求的图片的像素
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static File getAlbumDir() {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String getAlbumName() {
        return "sheguantong";
    }




    private static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建一个带文字的图片
     *
     * @param src
     * @param title3
     * @param context
     * @return
     */
    public static Bitmap createBitmap(Bitmap src, String title3, Context context) {
        if (src == null) {
            return src;
        }
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Log.i("test", "后w:" + w + "**h" + h);
        // 创建一个新的位图
        float startX = (float) (w * 0.2);
        float startY = (float) (h * 0.2);
        // 创建一个新的位图
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Log.i("Watertitle", "w=" + w + "\nh=" + h);
        Canvas mCanvas = new Canvas(newBitmap);
        // 在src的右下角添加水印
        // paint.setAlpha(100);
        // 开始加入文字
        if (null != title3) {
            TextPaint textPaint = new TextPaint(Paint.DITHER_FLAG);
            // 往位图中开始画入src原始图片
            mCanvas.drawBitmap(src, 0, 0, null);
            float scale = context.getResources().getDisplayMetrics().density;
            Log.i("logl", "" + scale);
            textPaint.setColor(0xFCFF0000);
            textPaint.setTextSize(40);
            String familyName = "宋体";
            Typeface typeface = Typeface.create(familyName,
                    Typeface.BOLD_ITALIC);
            textPaint.setTypeface(typeface);
            textPaint.setTextAlign(Paint.Align.LEFT);

            mCanvas.drawText(title3, startX, startY, textPaint);
            mCanvas.translate(0, 0);
            // 保存
            mCanvas.save(Canvas.ALL_SAVE_FLAG);
            // 存储
            mCanvas.restore();
        }
        return newBitmap;
    }

    /**
     * 保存图片
     *
     * @param pBitmap
     * @param context
     * @return
     */
    public static String saveFile(Bitmap pBitmap, Context context) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return null;
        }
        String filePathName = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName() + File.separator + "DCIM";

        File file = new File(filePathName);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd-hh-mm-ss");
        String bitmapPath = filePathName + File.separator + sdf.format(new Date(System
                .currentTimeMillis())) + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapPath);
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            return bitmapPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmapPath;
    }

    /**
     * 压缩图片 增加水印
     *
     * @return
     */
    public static Bitmap zoomCompressImg(String cacheImg, Context context) {
        Bitmap bitmap = PictureUtil.getSmallBitmap(cacheImg, 720, 1080);
        Bitmap bitmapcanvs = PictureUtil.createBitmap(bitmap, DateUtils.getNowTime(), context);
        return bitmapcanvs;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        return true;
    }
}
