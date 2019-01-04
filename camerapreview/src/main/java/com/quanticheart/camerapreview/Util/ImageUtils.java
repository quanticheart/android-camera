package com.quanticheart.camerapreview.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.quanticheart.camerapreview.CameraPreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageUtils {

    //==============================================================================================
    //
    // ** init vats
    //
    //==============================================================================================

    private static final String IMAGE_DIRECTORY = "/CustomImage";

    //==============================================================================================
    //
    // ** diretoy save images
    //
    //==============================================================================================

    private static String getDiretory() {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + timeStamp + ".jpeg";
    }

    //==============================================================================================
    //
    // ** Make file
    //
    //==============================================================================================

    private static File makeFile(String diretory) {
        File pictureFile = new File(diretory);
        if (pictureFile.exists()) {
            boolean b = pictureFile.delete();
        }
        return pictureFile;
    }

    //==============================================================================================
    //
    // ** Rotate Image
    //
    //==============================================================================================

    private static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    //==============================================================================================
    //
    // ** Create and save image
    //
    //==============================================================================================

    public static void createBitmap(Activity activity, byte[] data) {

        File file = makeFile(getDiretory());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            ExifInterface exif = new ExifInterface(file.toString());

            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                realImage = rotate(realImage, 270);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                realImage = rotate(realImage, 180);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
                realImage = rotate(realImage, 90);
            }

            boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
            Log.d("Info", bo + "");

            DialogUtils.openDialog(activity, realImage);

        } catch (FileNotFoundException e) {
            Log.d("Info", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    //==============================================================================================
    //
    // ** Save Image
    //
    //==============================================================================================
    public static String saveImage(Activity activity, Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(activity,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //==============================================================================================
    //
    // ** Image conversor utils
    //
    //==============================================================================================

    static Bitmap rotateBitmap(Context context, Uri uri, Bitmap bitmap) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            @SuppressLint("NewApi") ExifInterface exif = new ExifInterface(inputStream);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = ExifToDegrees(rotation);
            if (rotationInDegrees == 0)
                return bitmap;

            Matrix matrix = new Matrix();
            matrix.preRotate(rotationInDegrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            return bitmap;
        }
    }

    private static int ExifToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

}
