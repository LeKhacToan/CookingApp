package com.example.cookingapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRcvAdapter;
    List<Dish> data;
    int i=0;
    String url="https://cookingbamboo.herokuapp.com/api/dish/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.home_fragment,container,false);
        i=0;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //lắng nghe sự kiện scroll cuối recyclerview  thêm dữ liệu
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                   // Toast.makeText(getContext(), "Last scroll end", Toast.LENGTH_LONG).show();
                    i++;
                    if(i<4){
                        loadNewData(i);
                    }
                }
            }
        });

        data = new ArrayList<>();
        getData();
        return view;
    }

//Lấy dữ liệu từ internet
    private void getData(){
        //phương thức get, url: "https://cookingbamboo.herokuapp.com/api/dish/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url+i,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getContext(), "thu thoi nhe", Toast.LENGTH_SHORT).show();
                for (int i = 0;i < response.length(); i++){
                    try {
                       //phân tích dữ liệu trả về
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String auth = jsonObject.getString("auth");
                        String time = jsonObject.getString("time");
                        String url_image=jsonObject.getString("url_image");
                       //Tạo đối tượng món ăn mới
                        Dish dish = new Dish(id,title,auth,time,url_image);
                        //Thêm món ăn vào danh sách các món
                        data.add(dish);

                    } catch (JSONException e) {
                       // Toast.makeText(getContext(), "thu thoi nhe", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                //set up view cho người dùng
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

//Lấy dữ liệu mới khi người dùng scroll đế cuối màn hình
    private void loadNewData(int i){
        if(i<4){
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url+i,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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
                    mRcvAdapter.notifyItemInserted(data.size()-1);
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
        else{
            Toast.makeText(getActivity().getApplicationContext(),"Không có dữ liệu",Toast.LENGTH_LONG).show();
        }

    }

}
