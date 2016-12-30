package su.psn.elegantcircleview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView cropView;
    private Drawable cropViewDrawable;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        cropView = (ImageView) findViewById(R.id.cropView);
//        cropViewDrawable = cropView.getDrawable();
//        bitmap = Utils.drawableToBitmap(cropViewDrawable);
//        Bitmap bmp = cropBitmap(bitmap);
//        cropView.setImageBitmap(bmp);
    }

    /**
     * 对现有的bitmap进行裁切。
     *
     * @param bitmap source bitmap
     * @return 剪裁后的new bitmap
     */
    private Bitmap cropBitmap(Bitmap bitmap) {
        Bitmap bmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    // 0,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight());
        } else {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 4 - bitmap.getWidth() / 4,
                    bitmap.getWidth(),
                    bitmap.getWidth());
        }
        return bmp;
    }
}
