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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSaved extends Fragment {
    AccountHelper helper;
    Account account;
    String url="https://cookingbamboo.herokuapp.com/api/SaveOfYou";
    RecyclerView mRecyclerView;
    List<Dish> data;
    TopAdapter topAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_saved,container,false);
        helper =new AccountHelper(getActivity());
        account = helper.getAccountById(1);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_save);
        getData();
        return view;
    }
    private void getData(){
        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getActivity().getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try{
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getString("success").equals("false")){
                        Toast.makeText(getActivity().getApplicationContext(),"Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("datas");
                        data = new ArrayList<>();
                        for (int i = 0;i < jsonArray.length(); i++){
                            try {

                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                int id = jsonData.getInt("id");
                                String title = jsonData.getString("title");
                                String auth = jsonData.getString("auth");
                                String time = jsonData.getString("time");
                                String url_image=jsonData.getString("url_image");
                                Dish dish = new Dish(id,title,auth,time,url_image);
                                data.add(dish);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    setupView();
                }catch (Throwable t){
                    Toast.makeText(getActivity().getApplicationContext(),"Có lỗi sảy ra", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String ,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",account.getEmail());
                params.put("password",account.getPassword());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void setupView(){
        topAdapter = new TopAdapter(getContext(),data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(topAdapter);
    }
}
