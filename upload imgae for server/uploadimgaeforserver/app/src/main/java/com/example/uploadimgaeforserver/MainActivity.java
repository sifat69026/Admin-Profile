package com.example.uploadimgaeforserver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    ImageView upImage;
    Button myButton;
    TextView texView,tvDisplayAddress,tvmobile,tvfiledproname,tvDisplayCard,tvName,tvEmail;
    ShapeableImageView imageViewMain;
    ProgressBar progressBar;
    MaterialButton  btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.whitePro));
        window.setNavigationBarColor(getResources().getColor(R.color.whitePro));

        toolbar = findViewById(R.id.toolbar);
        upImage = findViewById(R.id.upImage);
//        myButton = findViewById(R.id.myButton);
//        texView = findViewById(R.id.texView);

        imageViewMain = findViewById(R.id.imageViewMain);
        tvDisplayAddress = findViewById(R.id.tvDisplayAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvmobile = findViewById(R.id.tvmobile);
        tvfiledproname = findViewById(R.id.tvfiledproname);
        tvDisplayCard = findViewById(R.id.tvDisplayCard);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);



//        progressBar = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profiles");
        toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Drawable drawable = toolbar.getNavigationIcon();
        if(drawable!=null)
        {
            drawable.setTint(ContextCompat.getColor(MainActivity.this,R.color.black));
        }

        loadImage();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

//        btnEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (imageViewMain.getDrawable() == null) {
//                    Toast.makeText(MainActivity.this,"Please select image first",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewMain.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
//
//                byte[] imageByte = byteArrayOutputStream.toByteArray();
//                String imgaeString = Base64.encodeToString(imageByte,Base64.NO_WRAP);
//                //texView.setText(imgaeString);
//
//                strigRequest(imgaeString);
//
//
//            }
//        });

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                galeraLuncher.launch(intent);

//                if(checkPermition())
//                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    captuerLuncher.launch(intent);
//
//                }

                ImagePicker.with(MainActivity.this)
                        .crop()
                        .maxResultSize(1000,1000)
                        .compress(1024)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imgePicker.launch(intent);
                                return null;
                            }
                        });
            }
        });
    }

    private void loadProfile()
    {
        String url = "http://192.168.0.102/Api_seu/get_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    String Name = jsonObject.getString("name");
                    String Email = jsonObject.getString("email");
                    String Mobile = jsonObject.getString("mobile");
                    String Address = jsonObject.getString("address");
                    String Quenty = jsonObject.getString("quenty");
                    String Card = jsonObject.getString("card");

                    tvName.setText(Name);
                    tvEmail.setText(Email);
                    tvmobile.setText(Mobile);
                    tvDisplayAddress.setText(Address);
                    tvfiledproname.setText(Quenty);
                    tvDisplayCard.setText(Card);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(MainActivity.this,volleyError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    private void saveImage(Bitmap bitmap) {
        SharedPreferences sp = getSharedPreferences("ImagePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();

        String imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        editor.putString("profile_image", imageString);
        editor.apply();
    }

    private void loadImage() {
        SharedPreferences sp = getSharedPreferences("ImagePref", MODE_PRIVATE);
        String imageString = sp.getString("profile_image", null);

        if (imageString != null) {
            byte[] imageBytes = Base64.decode(imageString, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageViewMain.setImageBitmap(bitmap);
        }
    }

    ActivityResultLauncher<Intent> imgePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData()!=null)
            {

                Uri uri = result.getData().getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imageViewMain.setImageBitmap(bitmap);
                    saveImage(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    });

    ActivityResultLauncher<Intent> captuerLuncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == Activity.RESULT_OK)
            {
                Intent intent = result.getData();
                Bundle bundle = intent.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                imageViewMain.setImageBitmap(bitmap);

            }

        }
    });

    ActivityResultLauncher<Intent> galeraLuncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult ruselt) {

            if(ruselt.getResultCode() == Activity.RESULT_OK)
            {
                Intent intent = ruselt.getData();
                Uri uri = intent.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imageViewMain.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    });

    private boolean checkPermition()
    {
        boolean givePermiton = true;

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED)
        {
            givePermiton = true;
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},102);
            givePermiton = false;
        }

        return givePermiton;
    }

    private void strigRequest(String imgaes64)
    {

      //  progressBar.setVisibility(View.VISIBLE);
        String url = "http://192.168.0.103/Api_seu/imgaeUplold.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            //    progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this,volleyError.getMessage(),Toast.LENGTH_SHORT).show();
             //   progressBar.setVisibility(View.GONE);

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map map = new HashMap<String,String>();
                map.put("image",imgaes64);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}