package com.sau.quiz2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sau.quiz2.MainActivity;
import com.sau.quiz2.R;
import com.sau.quiz2.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by saurabh on 2017-04-02.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<Picture> apods;
    private Context context;

    public ListAdapter(Context context, ArrayList<Picture> apods) {
        this.apods = apods;
        this.context = context;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDate;
        public ImageView imgApod;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
            this.txtTitle = (TextView) view.findViewById(R.id.txt_title);
            this.imgApod = (ImageView) view.findViewById(R.id.img_apod);
            this.txtDate = (TextView) view.findViewById(R.id.txt_date);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Picture apod = apods.get(position);
        Picasso.with(context).load(apod.getUrl()).fit().centerCrop().into(holder.imgApod);
        holder.txtTitle.setText(apod.getTitle());
        holder.txtDate.setText(apod.getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("isOpen", true);
                i.putExtra("apod", apod);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apods.size();
    }
}
