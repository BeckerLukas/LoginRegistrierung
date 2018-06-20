package de.hshl.loginregistrierung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


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
    }

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

}