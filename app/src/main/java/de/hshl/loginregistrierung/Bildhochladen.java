package de.hshl.loginregistrierung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class Bildhochladen extends AppCompatActivity {

    Button btn;
    InputStream is;
    ImageView imageView;
    SeekBar redBar, greenBar, blueBar;
    EditText etBeschreibung;
    private String userid;
    private String encoded_string, image_name, userid2, beschreibung;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        setContentView(R.layout.activity_hochladen);
        btn = (Button) findViewById(R.id.button3);
        imageView = (ImageView) findViewById(R.id.bildanzeige);
        redBar = (SeekBar) findViewById(R.id.redbar);
        greenBar = (SeekBar) findViewById(R.id.greenbar);
        blueBar = (SeekBar) findViewById(R.id.bluebar);
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        etBeschreibung =(EditText)findViewById(R.id.beschreibung);

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(bmp);

        redBar.setOnSeekBarChangeListener(colorBarChangeListener);
        greenBar.setOnSeekBarChangeListener(colorBarChangeListener);
        blueBar.setOnSeekBarChangeListener(colorBarChangeListener);
        setColorFilter(imageView);

        btn = (Button) findViewById(R.id.button3);

    }
    public void onUpload(View view){
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] array = stream.toByteArray();
        encoded_string = Base64.encodeToString(array, 0);

        beschreibung = etBeschreibung.getText().toString();
        String type ="upload";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, beschreibung, encoded_string, userid);

    }

    SeekBar.OnSeekBarChangeListener colorBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {

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

    private void setColorFilter(ImageView imageView) {

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

        float redValue = ((float) redBar.getProgress()) / 255;
        float greenValue = ((float) greenBar.getProgress()) / 255;
        float blueValue = ((float) blueBar.getProgress()) / 255;

        float[] colorMatrix = {
                redValue, 0, 0, 0, 0,  //red
                0, greenValue, 0, 0, 0, //green
                0, 0, blueValue, 0, 0,  //blue
                0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }

}


