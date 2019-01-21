package project.com.maktab.hw_6;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public static Bitmap decodeUri(Activity activity, Uri uri) throws FileNotFoundException {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return decodeUri(activity, uri, point.x, point.y);

    }

    public static Bitmap getBitmapByUri(Activity activity, Uri imageUri) throws FileNotFoundException {
        Bitmap resultBitmap;
        InputStream imageStream = null;
        imageStream = activity.getContentResolver().openInputStream(imageUri);
        resultBitmap = BitmapFactory.decodeStream(imageStream);
        return resultBitmap;
    }


    public static Bitmap decodeUri(Context c, Uri uri, int desWidth, int desHeight)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < desWidth || height_tmp / 2 < desHeight)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

}
