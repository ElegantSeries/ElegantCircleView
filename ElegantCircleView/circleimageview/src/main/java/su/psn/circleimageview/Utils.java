package su.psn.circleimageview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by SUCONG on 2016/12/28.
 */

public class Utils {

    private Utils() {

    }

    public static int getValuesDifference(int firstValue, int secondValue) {
        return Math.abs(firstValue - secondValue);
    }

    public static int getBorderMax(int pressedBorderRing, int defaultBorder) {
        return pressedBorderRing > defaultBorder ? pressedBorderRing : defaultBorder;
    }

    public static int twiceValue(int value) {
        return value * 2;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0)) {
            return null;
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
