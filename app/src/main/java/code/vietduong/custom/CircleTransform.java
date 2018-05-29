package code.vietduong.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * Created by codev on 4/15/2018.
 */

public class CircleTransform implements Transformation {

    private int r2 = 0;
    public CircleTransform(int r2) {
        this.r2 = r2;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);

        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);

        float r = size / 2f;

        paint.setStrokeWidth((r-r2));

        canvas.drawCircle(r, r, r-(r-r2)/2, paint);


        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}