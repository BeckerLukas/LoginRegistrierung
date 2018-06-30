package de.hshl.loginregistrierung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;


import java.io.FileNotFoundException;
import java.io.InputStream;

public class Bildhochladen extends AppCompatActivity {

    ImageView iv;
    Button btn;
    Intent intent1;
    final int requcode = 3;
    Uri bilduri;
    Bitmap bm;
    InputStream is;
    ImageView imageView;
    SeekBar redBar, greenBar, blueBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hochladen);

        iv = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(intent1, requcode);
            }
        });
        imageView = (ImageView)findViewById(R.id.iv);
        redBar = (SeekBar)findViewById(R.id.redbar);
        greenBar = (SeekBar)findViewById(R.id.greenbar);
        blueBar = (SeekBar)findViewById(R.id.bluebar);

        redBar.setOnSeekBarChangeListener(colorBarChangeListener);
        greenBar.setOnSeekBarChangeListener(colorBarChangeListener);
        blueBar.setOnSeekBarChangeListener(colorBarChangeListener);

        setColorFilter(imageView);

    }
    SeekBar.OnSeekBarChangeListener colorBarChangeListener
            = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setColorFilter(imageView);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){

            if(requestCode == requcode){

                bilduri = data.getData();
                try {
                    is = getContentResolver().openInputStream(bilduri);
                    bm = BitmapFactory.decodeStream(is);
                    iv.setImageBitmap(bm);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setColorFilter(ImageView iv){

        /*
         * 5x4 matrix for transforming the color+alpha components of a Bitmap.
         * The matrix is stored in a single array, and its treated as follows:
         * [  a, b, c, d, e,
         *   f, g, h, i, j,
         *   k, l, m, n, o,
         *   p, q, r, s, t ]
         *
         * When applied to a color [r, g, b, a], the resulting color is computed
         * as (after clamping)
         * R' = a*R + b*G + c*B + d*A + e;
         * G' = f*R + g*G + h*B + i*A + j;
         * B' = k*R + l*G + m*B + n*A + o;
         * A' = p*R + q*G + r*B + s*A + t;
         */

        float redValue = ((float)redBar.getProgress())/255;
        float greenValue = ((float)greenBar.getProgress())/255;
        float blueValue = ((float)blueBar.getProgress())/255;

        float[] colorMatrix = {
                redValue, 0, 0, 0, 0,  //red
                0, greenValue, 0, 0, 0, //green
                0, 0, blueValue, 0, 0,  //blue
                0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        iv.setColorFilter(colorFilter);
    }


}