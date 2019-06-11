package com.example.cookingapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentUser extends Fragment {
    Button post;
    Button potSave;
    Button logout;
    JSONObject user;
    Account account;
    AccountHelper helper;
    TextView tv_name;
    String name;
    String url_image;
    String avatar;
    String url="https://cookingbamboo.herokuapp.com/api/authen";
    ImageView anhbia;
    ImageView anhAvatar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.user_fragment,container,false);
        tv_name = (TextView) view.findViewById(R.id.yourname) ;
        anhbia=(ImageView)view.findViewById(R.id.image_bia);
        anhAvatar=(ImageView)view.findViewById(R.id.imageAvatar);
        post = (Button)view.findViewById(R.id.bt_post);
        potSave = (Button)view.findViewById(R.id.bt_postSaved);
        logout = (Button)view.findViewById(R.id.logout);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentPost());
            }
        });
        potSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentSaved());
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentNewPost());
            }
        });
        helper= new AccountHelper(getActivity());
        account=helper.getAccountById(1);
        //khôi phục trạng thái đã lưu
        if(savedInstanceState!= null){
            name = savedInstanceState.getString("name");
            url_image = savedInstanceState.getString("url_image");
            avatar = savedInstanceState.getString("avatar");
            setupViewPager();
            Toast.makeText(getActivity(), "create new", Toast.LENGTH_SHORT).show();
        }
        else{
            getData();
            loadFragment(new FragmentPost());
        }
       // setupViewPager();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("name",name);
        outState.putString("url_image",url_image);
        outState.putString("avatar",avatar);
        super.onSaveInstanceState(outState);
    }


    //lấy dữ liệu bằng volley
    private  void getData(){
        StringRequest jsonObjectRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                try{
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getString("success").equals("false")){
                        error();
                        Toast.makeText(getActivity().getApplicationContext(),"Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                    else {
                         user = jsonObject.getJSONObject("user");
                         name=user.getString("name");
                         url_image=user.getString("url_image");
                         avatar = user.getString("avatar");
                    }
                    setupViewPager();
                }catch (Throwable t){
                    Toast.makeText(getActivity().getApplicationContext(),"Có lỗi sảy ra", Toast.LENGTH_SHORT).show();
                }

            }
        },new Response.ErrorListener(){
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
        requestQueue.add(jsonObjectRequest);
    }
    //set up view
    private void setupViewPager() {
        tv_name.setText(name);
        Picasso.get().load(url_image).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(anhbia);
        Picasso.get().load(avatar).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(anhAvatar);

    }
    private  void error(){
        //load login view
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new FragmentMenu());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_use, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
