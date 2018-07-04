package de.hshl.loginregistrierung;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshl.loginregistrierung.Models.Benutzer;
import de.hshl.loginregistrierung.Models.Profil_Bilder;
import de.hshl.loginregistrierung.Models.Profil_row;

public class Profil extends AppCompatActivity {

    private TextView username, beschreibung;
    BottomNavigationView navigation;
    String userid;
    String profilid;
    public Button btn1, btn2, btn3;
    boolean responsestatus;
    String abo_url ="http://192.168.0.109/Instalite/abo.php";
    GridView lvbilder;
    ImageView profilbild;



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
        lvbilder =(GridView) findViewById(R.id.lvbilder);
        username =(TextView)findViewById(R.id.username2);
        beschreibung =(TextView)findViewById(R.id.beschreibung);
        profilbild = (ImageView)findViewById(R.id.imageView);
        visible();
        bearbeiten();
        JSONTask jsonTask = new JSONTask();
        jsonTask.execute(profilid);
        JSONTask2 jsonTask2 = new JSONTask2();
        jsonTask2.execute(profilid);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

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
    public class JSONTask2 extends AsyncTask<String, Void, Profil_row> {

        @Override
        protected Profil_row doInBackground(String... params) {
            String suche_url = "http://192.168.0.109/Instalite/Profil2.php";
            try {
                String profila = params[0];
                URL url = new URL(suche_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("profilid", "UTF-8")+"="+URLEncoder.encode(profila, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject parentObject = new JSONObject(result);


                Profil_row profil = new Profil_row();
                String usern = parentObject.getString("username");
                String beschreibung = parentObject.getString("beschreibung");
                String profilbild = parentObject.getString("profilbild");


                profil.setBeschreibung(beschreibung);
                profil.setUsername(usern);
                profil.setProfilbild(profilbild);

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return profil;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Profil_row result) {
            super.onPostExecute(result);
            username.setText(result.getUsername());
            beschreibung.setText(result.getBeschreibung());
            ImageLoader.getInstance().displayImage(result.getProfilbild(), profilbild);



        }
    }

    public class JSONTask extends AsyncTask<String, Void, List<Profil_Bilder>> {

        @Override
        protected List<Profil_Bilder> doInBackground(String... params) {
            String suche_url = "http://192.168.0.109/Instalite/Profil.php";
            try {
                String profila = params[0];
                URL url = new URL(suche_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("profilid", "UTF-8")+"="+URLEncoder.encode(profila, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result+=line;
                }

                JSONObject parentObject = new JSONObject(result);
                JSONArray parentArray = parentObject.getJSONArray("user");

                List<Profil_Bilder> profilListe = new ArrayList<>();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObjekt = parentArray.getJSONObject(i);
                    Profil_Bilder profil = new Profil_Bilder();
                    profil.setBild(finalObjekt.getString("bild"));

                    profilListe.add(profil);
                    }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return profilListe;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Profil_Bilder> result) {
            super.onPostExecute(result);

            Profil.JSONTask.ProfilAdapter benutzerAdapter = new Profil.JSONTask.ProfilAdapter(getApplicationContext(), R.layout.layout, result);
            lvbilder.setAdapter(benutzerAdapter);


        }


        public class ProfilAdapter extends ArrayAdapter {
            private List<Profil_Bilder> profilList;
            private int resource;
            private LayoutInflater inflater;

            public ProfilAdapter(Context context, int resource, List<Profil_Bilder> objects) {
                super(context, resource, objects);
                profilList = objects;
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                this.resource = resource;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = inflater.inflate(resource, null);
                }

                ImageView profil_bilder;

                profil_bilder = (ImageView) convertView.findViewById(R.id.bilder);
                ImageLoader.getInstance().displayImage(profilList.get(position).getBild(), profil_bilder);

                return convertView;
            }
        }

    }
}
