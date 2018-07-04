package de.hshl.loginregistrierung;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

import de.hshl.loginregistrierung.Models.Benutzer;
import de.hshl.loginregistrierung.Models.News;

public class Newsfeed extends AppCompatActivity {
    BottomNavigationView navigation;
    String userid;
    private ListView lvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Intent intent=getIntent();
        userid = intent.getStringExtra("userid");
        lvUser = (ListView)findViewById(R.id.feed);

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
                        Intent intent1 = new Intent(Newsfeed.this, Profil.class);
                        intent1.putExtra("userid", userid);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(Newsfeed.this, TakePicture.class);
                        intent2.putExtra("userid", userid);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notifications:
                        break;


                }
                return false;
            }
        });
        JSONTask jsonTask = new JSONTask();
        jsonTask.execute(userid);

    }
    public class JSONTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected List<News> doInBackground(String... params) {
            String suche_url = "http://192.168.0.109/Instalite/Newsfeed.php";
            try {
                String username = params[0];
                URL url = new URL(suche_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("userid", "UTF-8")+"="+URLEncoder.encode(userid, "UTF-8");
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

                List<News> feedListe = new ArrayList<>();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObjekt = parentArray.getJSONObject(i);
                    News benutzer = new News();
                    benutzer.setUsername(finalObjekt.getString("username"));
                    benutzer.setBild(finalObjekt.getString("Bildpfad"));
                    benutzer.setZeit(finalObjekt.getString("Zeit"));

                    feedListe.add(benutzer);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return feedListe;
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
        protected void onPostExecute(List<News> result) {
            super.onPostExecute(result);

            Newsfeed.JSONTask.newsAdapter benutzerAdapter = new Newsfeed.JSONTask.newsAdapter(getApplicationContext(), R.layout.row_newsfeed, result);
            lvUser.setAdapter(benutzerAdapter);


        }


        public class newsAdapter extends ArrayAdapter {
            private List<News> benutzerList;
            private int resource;
            private LayoutInflater inflater;

            public newsAdapter(Context context, int resource, List<News> objects) {
                super(context, resource, objects);
                benutzerList = objects;
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                this.resource = resource;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = inflater.inflate(resource, null);
                }

                ImageView news_bild;
                TextView username;
                TextView zeit;

                news_bild = (ImageView) convertView.findViewById(R.id.imageView5);
                username = (TextView) convertView.findViewById(R.id.benutzername);
                zeit = (TextView)convertView.findViewById(R.id.Zeit);


                ImageLoader.getInstance().displayImage(benutzerList.get(position).getBild(), news_bild);
                username.setText(benutzerList.get(position).getUsername());
                zeit.setText(benutzerList.get(position).getZeit());


                return convertView;
            }
        }

    }
}
