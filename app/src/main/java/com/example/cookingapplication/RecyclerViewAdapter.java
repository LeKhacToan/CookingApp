package com.example.cookingapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    String url = "https://cookingbamboo.herokuapp.com/api/save";
    private List<Dish> dishs=new ArrayList<>() ;
    String id;
    Account account;
    Context mContext;

    public RecyclerViewAdapter(Context mContext,List<Dish> data) {
        this.mContext=mContext;
        this.dishs = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        //set up animation here
        //holder.imageView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
      //  holder.iv_favorite.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        final Dish dish =(Dish) dishs.get(position);
        Picasso.get().load(dish.getUrl_image()).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(holder.imageView);
        holder.tvTitle.setText(dish.getTitle());
        holder.tvAuther.setText(dish.getAuth());
        holder.tvTimer.setText(dish.getTime());
        //bắt sự kiện click vào các thành phần
        holder.item_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "click item view"+dish.getId(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(),ShowDish.class);
                intent.putExtra("id",dish.getId()+"");
                v.getContext().startActivity(intent);
            }
        });
        //click save mon an
        holder.iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(v.getContext(), "favorite ", Toast.LENGTH_SHORT).show();
                AccountHelper helper =new AccountHelper(v.getContext());
               account = helper.getAccountById(1);
               id =  Integer.toString(dish.getId());
                senddata(v);
            }
        });


    }


    @Override
    public int getItemCount() {
        return dishs.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuther;
        TextView tvTimer;
        TextView tvTitle;
        ImageView imageView;
        RelativeLayout item_dish;
        CardView cardView;
        ImageView iv_favorite;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            item_dish=(RelativeLayout)itemView.findViewById(R.id.item_dish);
            imageView = (ImageView) itemView.findViewById(R.id.imager);
            tvAuther = (TextView) itemView.findViewById(R.id.auther);
            tvTitle = (TextView) itemView.findViewById(R.id.titler);
            tvTimer = (TextView) itemView.findViewById(R.id.timer);
            cardView = (CardView) itemView.findViewById(R.id.carview);
            iv_favorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
        }
    }




    //gui du lieu luu bai viet
    private void senddata(View v){
        StringRequest jsonObjectRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                try{
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getString("success").equals("false")){
                        Toast.makeText(mContext,jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                  //  setupViewPager();
                }catch (Throwable t){
                    Toast.makeText(mContext,"Có lỗi sảy ra", Toast.LENGTH_SHORT).show();
                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String ,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",account.getEmail());
                params.put("password",account.getPassword());
                params.put("id",id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjectRequest);
    }
}


