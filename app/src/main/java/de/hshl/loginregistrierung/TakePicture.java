package de.hshl.loginregistrierung;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TakePicture extends AppCompatActivity implements OnClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView showpic;
    final int requcode = 3;
    Uri bilduri;
    Bitmap bm;
    InputStream is;
    String userid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        Intent intent3=getIntent();
        userid = intent3.getStringExtra("userid");
        Button fotobutton = (Button) findViewById(R.id.fotobutton);
        showpic = (ImageView) findViewById(R.id.showpic);
        Button btn = (Button) findViewById(R.id.button6);
        Button btn2 = (Button)findViewById(R.id.weiter);
        btn2.setOnClickListener(this);
        fotobutton.setOnClickListener(this);
        btn.setOnClickListener(this);

    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == 0) {
                super.onActivityResult(requestCode, resultCode, data);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                showpic.setImageBitmap(bitmap);

            }else if(requestCode == 3){
            if(resultCode == RESULT_OK){

                if(requestCode == requcode){

                    bilduri = data.getData();
                    try {
                        is = getContentResolver().openInputStream(bilduri);
                        bm = BitmapFactory.decodeStream(is);
                        showpic.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    }
                }
            }

        }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fotobutton){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }else if(v.getId()  == R.id.button6){
            Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/*");
            startActivityForResult(intent1, requcode);
        }else if(v.getId() == R.id.weiter){
            Bitmap bmp = ((BitmapDrawable)showpic.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG,100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent2 = new Intent(TakePicture.this, Bildhochladen.class);
            intent2.putExtra("userid", userid);
            intent2.putExtra("picture", byteArray);
            startActivity(intent2);
        }
    }

    }