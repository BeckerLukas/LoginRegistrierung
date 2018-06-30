package de.hshl.loginregistrierung;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TakePicture extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView showpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        Button fotobutton = (Button) findViewById(R.id.fotobutton);
        showpic = (ImageView) findViewById(R.id.showpic);

        fotobutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

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
        Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("Bitmap");
        ImageView viewBitmap = (ImageView)findViewById(R.id.showpic);
        viewBitmap.setImageBitmap(bitmap);

    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            showpic.setImageBitmap(bitmap);
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

    ImageView iv;
    Button btn;
    Intent intent1;
    final int requcode = 3;
    Uri bilduri;
    Bitmap bm;
    InputStream is;




}