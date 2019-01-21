package project.com.maktab.hw_6;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.io.File;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int desWidth, int desHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int sampleSize = 1;
        if (srcWidth > desWidth || srcHeight > desHeight) {


            float heightScale = srcHeight / desHeight;
            float widthScale = srcWidth / desWidth;
            sampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);

        }

        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;

    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaledBitmap(path, point.x, point.y);

    }

}
