package com.frico.usct.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileImageUtils {
    public static File saveBitmapFile(Bitmap bitmap) {

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/skm.jpg");
        if(file.exists()){
            file.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

            return file;
        } catch (IOException e) {
            LogUtils.w(":"+e.toString());
            e.printStackTrace();
        }

        return file;
    }
}
