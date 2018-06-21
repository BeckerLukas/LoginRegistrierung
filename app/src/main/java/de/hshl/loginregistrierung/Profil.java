package de.hshl.loginregistrierung;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Profil extends AppCompatActivity {

    private TextView mTextMessage;
    BottomNavigationView navigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(Profil.this, TakePicture.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(Profil.this, Newsfeed.class);
                        startActivity(intent2);
                        break;


            }
                return false;
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);


    }

}
