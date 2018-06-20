package de.hshl.loginregistrierung;

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

public class TakePicture extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView showpicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        Button fotobutton = (Button) findViewById(R.id.fotobutton);
        showpicImageView= (ImageView) findViewById(R.id.showpic);

        //Disable the button if the user has no camera
        if(!hasCamera())
            fotobutton.setEnabled(false);
    }
       //Check if the user has camera
        private boolean hasCamera() {
    return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        }
    //launch camera
    public void launchCamera(View view){
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       //Mache ein Foto und gib sie an onActivityResult weiter
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    //If you want to return the image taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
           //Get the photo
           Bundle extras = data.getExtras();
           Bitmap photo = (Bitmap) extras.get("data");
           showpicImageView.setImageBitmap(photo);
        }
    }
}
