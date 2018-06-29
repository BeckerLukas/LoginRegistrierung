package de.hshl.loginregistrierung;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Newsfeed extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(Newsfeed.this, Profil.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(Newsfeed.this, TakePicture.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notifications:
                        break;


                }
                return false;
            }
        });

    }
}
