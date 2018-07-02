package de.hshl.loginregistrierung;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class search extends AppCompatActivity {

    private ListView lvUser;
    private Button btn1;
    private EditText usernameEt;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent2=getIntent();
        userid = intent2.getStringExtra("userid");

        usernameEt=(EditText)findViewById(R.id.search);
        btn1=(Button)findViewById(R.id.searchView2);

    }
    public void onClick(View v) {
        if (v.getId()== R.id.searchView2) {
            final String username = usernameEt.getText().toString();
            JSONTask jsonTask = new JSONTask();
            jsonTask.execute(username);
        }

    }


    public class JSONTask extends AsyncTask<String, String,  List<Benutzer>>{

        @Override
        protected  List<Benutzer> doInBackground(String... params) {
            String suche_url = "http://192.168.188.24/Instalite/search.php";
            try {
                String username = params[0];
                URL url = new URL(suche_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = bufferedReader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("user");

                List<Benutzer> benutzerListe = new ArrayList<>();

                for(int i=0;i<parentArray.length();i++){
                    JSONObject finalObjekt = parentArray.getJSONObject(i);
                    Benutzer benutzer = new Benutzer();
                    benutzer.setUserid(finalObjekt.getString("userid"));
                    benutzer.setUsername(finalObjekt.getString("username"));
                    benutzer.setProfilbild(finalObjekt.getString("profilbild"));

                    benutzerListe.add(benutzer);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return benutzerListe;
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
        protected void onPostExecute( List<Benutzer> result){
            super.onPostExecute(result);

            BenutzerAdapter benutzerAdapter = new BenutzerAdapter(getApplicationContext(), R.layout.row_search,result);
            lvUser.setAdapter(benutzerAdapter);

        }
    }


   public class BenutzerAdapter extends ArrayAdapter{
        private List<Benutzer> bentuzerList;
        private int resource;
        private LayoutInflater inflater;
        public BenutzerAdapter(@NonNull Context context, int resource, List<Benutzer> objects) {
            super(context, resource, objects);
            bentuzerList = objects;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.resource=resource;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }

            ImageView search_profilbild;
            TextView username;
            Button btn2;
            String profilid;

            search_profilbild = (ImageView)convertView.findViewById(R.id.search_profilbild);
            username = (TextView)convertView.findViewById(R.id.username);
            btn2 =(Button)convertView.findViewById(R.id.username);


            username.setText(bentuzerList.get(position).getUsername());
            profilid = bentuzerList.get(position).getUserid();



            return convertView;
        }
    }

}
