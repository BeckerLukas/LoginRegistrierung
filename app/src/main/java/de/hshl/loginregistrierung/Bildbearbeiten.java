package de.hshl.loginregistrierung;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Bildbearbeiten extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildbearbeiten);
    }



        private Bitmap changeToGreyscale (Bitmap src){
            int width = src.getWidth(),
                    height = src.getHeight();

            Bitmap dst = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(dst);
            Paint paint = new Paint();

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            paint.setColorFilter(filter);

            canvas.drawBitmap(src, left:0, top:0, paint);
            return dst;
        }
    }

