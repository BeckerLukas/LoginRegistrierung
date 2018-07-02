package de.hshl.loginregistrierung;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity  implements  View.OnClickListener {


    private EditText mPasswordView;
    EditText usernameEt, passwordEt;
    private View mProgressView;
    private View mLoginFormView;
    public Button btn1, btn2;

    public static final String login_url = "http://192.168.188.24/Instalite/login.php";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registration();
        usernameEt = (EditText)findViewById(R.id.email);
        passwordEt = (EditText)findViewById(R.id.password);

        btn2 =(Button)findViewById(R.id.email_sign_in_button);
        btn2.setOnClickListener(this);




        }
        /*public void OnLogin(View view){
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String type="login";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
        } */



    public void registration() {
        btn1 = (Button)findViewById(R.id.email_reg_button);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(register);
            }
        });
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.email_sign_in_button){
            final String email = usernameEt.getText().toString();
            final String password = passwordEt.getText().toString();
            if(email.equals("") || password.equals("")){
                Toast.makeText(LoginActivity.this, "Enter valid Username & Password", Toast.LENGTH_SHORT).show();
            }else{
                StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean responsestatus = jsonObject.getBoolean("success");
                            if(responsestatus){
                                String userid =jsonObject.getString("userid");
                                String profilid= jsonObject.getString("userid");
                                Toast.makeText(LoginActivity.this,"Profilid:" + userid, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, Profil.class);
                                intent.putExtra("userid", userid);
                                intent.putExtra("profilid", profilid);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Invalid E-Mail or Password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params=new HashMap<String, String>();
                        params.put(KEY_EMAIL, email);
                        params.put(KEY_PASSWORD, password);
                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            }
        }
    }
}

