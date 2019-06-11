package com.example.cookingapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class TopAdapter extends  RecyclerView.Adapter<TopAdapter.TopViewHolder> {

    private List<Dish> dishs=new ArrayList<>() ;
    Context mContext;
    public TopAdapter(Context mContext,List<Dish> data) {
        this.dishs = data;
        this.mContext = mContext;
    }

    @Override
    public TopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.top_item, parent, false);
        return new TopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopViewHolder holder, int position) {

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.swing_up_right));
        final Dish dish =(Dish) dishs.get(position);
        holder.tvTitle.setText(dish.getTitle());
        holder.tvAuther.setText(dish.getAuth());
        holder.tvTimer.setText(dish.getTime());
        Picasso.get().load(dish.getUrl_image()).fit().centerCrop().placeholder(R.drawable.error).error(R.drawable.error).into(holder.imImage);
        holder.itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(v.getContext(), "click item view"+dish.getId(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(),ShowDish.class);
                intent.putExtra("id",dish.getId()+"");
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dishs.size();
    }


    public class TopViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuther;
        TextView tvTimer;
        TextView tvTitle;
        ImageView imImage;
        RelativeLayout itemSearch;
        CardView cardView;
        public TopViewHolder(View itemView) {
            super(itemView);
            itemSearch = (RelativeLayout)itemView.findViewById(R.id.toan);
            tvAuther = (TextView) itemView.findViewById(R.id.tv_auth);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTimer = (TextView) itemView.findViewById(R.id.tv_time);
            imImage = (ImageView) itemView.findViewById(R.id.iv_image);
            cardView = (CardView) itemView.findViewById(R.id.cardseach);
        }
    }
}
