package com.example.cookingapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowDish extends AppCompatActivity {

    String url="https://cookingbamboo.herokuapp.com/api/post/";
    String link="";
    TextView tvTitle ;
    TextView tvAuth;
    TextView tvTime;
    TextView tvServing ;
    TextView tvDescription ;
    TextView tvNguyenlieu ;
    TextView tvStep ;
    ImageView imDish;
    Dish dish = new Dish();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dish);

        tvTitle = (TextView)findViewById(R.id.nameDish);
        tvAuth = (TextView)findViewById(R.id.idAuth);
        tvTime = (TextView)findViewById(R.id.idTime);
        tvServing = (TextView)findViewById(R.id.idServing);
        tvDescription = (TextView)findViewById(R.id.idDescription);
        tvNguyenlieu = (TextView)findViewById(R.id.idNl);
        tvStep = (TextView)findViewById(R.id.idStep);
        imDish = (ImageView) findViewById(R.id.idImage);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        link="https://cookingbamboo.herokuapp.com/api/post/"+id;
      //  Toast.makeText(this,link , Toast.LENGTH_SHORT).show();
        getData();
    }

    private void getData(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,link,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               //  Toast.makeText(getApplication().getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                 try{
                     dish.setTitle(response.getString("title"));
                     dish.setTime(response.getString("time"));
                     dish.setServing(response.getString("serving"));
                     dish.setResources(response.getString("nguyenlieu"));
                     dish.setDescrible(response.getString("description"));
                     dish.setAuth(response.getString("auth"));
                     dish.setUrl_image(response.getString("url_image"));
                     dish.setSteps(response.getString("step"));
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 setupView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void setupView(){
         tvAuth.setText(dish.getAuth());
         tvTitle.setText(dish.getTitle());
         tvTime.setText(dish.getTime());
         tvServing.setText(dish.getServing());
         tvDescription.setText(dish.getDescrible());
         tvNguyenlieu.setText(dish.getResources());
         tvStep.setText(dish.getSteps());
         Picasso.get().load(dish.getUrl_image()).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(imDish);
    }

}
