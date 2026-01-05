package com.example.uploadimgaeforserver;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    MaterialToolbar toolbar2;
    TextInputEditText etName,etEmai,etMobile,etAddress,etSoil,etCardNmber;
    Button myButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.whitePro));
        window.setNavigationBarColor(getResources().getColor(R.color.whitePro));

        toolbar2 = findViewById(R.id.toolbar2);
        etName = findViewById(R.id.etName);
        etEmai = findViewById(R.id.etEmai);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);
        etSoil = findViewById(R.id.etSoil);
        etCardNmber = findViewById(R.id.etCardNmber);
        myButton = findViewById(R.id.myButton);


        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("কৃষক প্রোফাইল");
        toolbar2.setTitleTextColor(ContextCompat.getColor(MainActivity2.this, R.color.black));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Drawable drawable = toolbar2.getNavigationIcon();
        if (drawable != null) {
            drawable.setTint(ContextCompat.getColor(MainActivity2.this, R.color.black));
        }

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfoString();
            }
        });
    }
    private void saveInfoString()
    {
        String url = "http://192.168.0.102/Api_seu/save_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if(s.equals("SUCCESS"))
                {
                    Toast.makeText(MainActivity2.this,"প্রোফাইল সেভ হয়েছে",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity2.this,volleyError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map map = new HashMap<String,String>();
                map.put("name",etName.getText().toString());
                map.put("email",etEmai.getText().toString());
                map.put("mobile",etMobile.getText().toString());
                map.put("address",etAddress.getText().toString());
                map.put("quenty",etSoil.getText().toString());
                map.put("card",etCardNmber.getText().toString());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // অথবা onBackPressed();
        return true;
    }
}