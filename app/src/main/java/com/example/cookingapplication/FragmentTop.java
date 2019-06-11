package com.example.cookingapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentTop extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRcvAdapter;
    List<Dish> data;
    String url = "https://cookingbamboo.herokuapp.com/api/top_week/0";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.top_fragment,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_top_view);

        data = new ArrayList<>();
        if(savedInstanceState!=null){
            data = (List<Dish>) savedInstanceState.getSerializable("data");
            setUpRecyclerView();
        }
        else {
            getData();
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("data",(Serializable)data);
        super.onSaveInstanceState(outState);
    }

    private void getData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                for (int i = 0;i < response.length(); i++){
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String auth = jsonObject.getString("auth");
                        String time = jsonObject.getString("time");
                        String url_image=jsonObject.getString("url_image");
                       // String url_image = "https://cookingbamboo.herokuapp.com//upload//image_baiviet//PANS_recipe6814-636029870210229116.jpg";
                        Dish dish = new Dish(id,title,auth,time,url_image);
                        data.add(dish);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setUpRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);

    }


    private void setUpRecyclerView(){
        mRcvAdapter = new RecyclerViewAdapter(getContext(),data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRcvAdapter);
    }
}
