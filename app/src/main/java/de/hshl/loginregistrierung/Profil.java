package de.hshl.loginregistrierung;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profil extends AppCompatActivity {

    private TextView mTextMessage;
    BottomNavigationView navigation;
    String userid;
    String profilid;
    public Button btn1, btn2, btn3;
    boolean responsestatus;
    String abo_url ="http://192.168.188.24/Instalite/abo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Intent intent=getIntent();
        userid= intent.getStringExtra("userid");
        profilid= intent.getStringExtra("profilid");
        btn1=(Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.abonnieren);
        btn3=(Button)findViewById(R.id.deabonnieren);
        visible();
        bearbeiten();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(Profil.this, TakePicture.class);
                        intent1.putExtra("userid", userid);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(Profil.this, Newsfeed.class);
                        intent2.putExtra("userid", userid);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_search:
                        Intent intent5 = new Intent(Profil.this, search.class);
                        intent5.putExtra("userid", userid);
                        startActivity(intent5);


            }
                return false;
            }
        });

    }
    public void onAbonnieren(View view){
        String type="abonnieren";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, userid, profilid);

    }
    public void onDebonnieren(View view){
        String type="deabonnieren";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, userid, profilid);

    }
    public void bearbeiten() {
        btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profil.this, "Profilid: " + userid, Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void visible(){
        if(userid == profilid){
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
        }
        else{
            if(!abonniert()){
                btn1.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
            }else{
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
            }
        }
    }
    public boolean abonniert(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, abo_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    responsestatus = jsonObject.getBoolean("success");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Profil.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("userid", userid);
                params.put("profilid", profilid);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return responsestatus;

    }

}
