package com.example.cookingapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentMenu extends Fragment {

    Button login;
    EditText edUsername;
    EditText edPassword;
    TextView error;
    String email;
    String password;
    AccountHelper helper;
    JSONObject user;
    String url="https://cookingbamboo.herokuapp.com/api/authen";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,container,false);

        login = (Button)view.findViewById(R.id.login);
        edUsername = (EditText)view.findViewById(R.id.et_username);
        edPassword = (EditText)view.findViewById(R.id.et_password);
        error = (TextView) view.findViewById(R.id.error);
        helper = new AccountHelper(getActivity());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email = edUsername.getText().toString();
                 password = edPassword.getText().toString();
                 Authen();
            }
        });
        return view;
    }

    private void Authen(){
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
                        Account account = new Account(1,email,password);
                        user = jsonObject.getJSONObject("user");
                        Global.name = user.getString("name");
                        Global.url_image = user.getString("url_image");
                        Global.avatar=user.getString("avatar");
                        helper.addAccount(account);
                        setupView();
                    }
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
    private void setupView(){
       //load user view
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, new FragmentUser());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void error(){
        error.setText("Email hoặc mật khẩu không chính xác");
    }
}
