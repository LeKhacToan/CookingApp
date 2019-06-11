package com.example.cookingapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSearch extends Fragment {

    RecyclerView mRecyclerView;
    TopAdapter mRcvAdapter;
    List<Dish> data;
    SearchView searchView;
    String url="https://cookingbamboo.herokuapp.com/api/post";
    String name="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment,container,false);
        searchView = (SearchView) view.findViewById(R.id.search);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               // Toast.makeText(getContext(), "day la noi dung serach"+s, Toast.LENGTH_SHORT).show();
                name=s;
                getData();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        if(savedInstanceState != null){
            //data =new ArrayList<>();
           data = (List<Dish>)savedInstanceState.getSerializable("data");
           setUpRecyclerView();
        }
        else{
            getData();
        }

        data = new ArrayList<>();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("data",(Serializable)data);
        super.onSaveInstanceState(outState);
    }

    //Lấy dữ liệu từ trang cooking bamboo về
    private void getData(){
        StringRequest jsonArrayRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
               try {
                   JSONObject jsonObject= new JSONObject(response);
                   if(jsonObject.getString("success").equals("false")){
                       data = new ArrayList<>();
                       setUpRecyclerView();
                       Toast.makeText(getActivity().getApplicationContext(),"Không có dữ liệu", Toast.LENGTH_SHORT).show();
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
                       setUpRecyclerView();

                   }
               }
               catch (Throwable t){
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
                params.put("name", name);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    //hiển thị dữ liệu ra view
    private void setUpRecyclerView(){

        mRcvAdapter = new TopAdapter(getContext(),data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRcvAdapter);
    }
}
