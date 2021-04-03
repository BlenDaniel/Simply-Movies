package com.simple.movie.streaming.simplymovie.ui.views.popularadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simple.movie.streaming.simplymovie.R;
import com.simple.movie.streaming.simplymovie.ui.views.adapters.OnMovieListener;

public class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Click Listeners
    OnMovieListener onMovieListener;
    public ImageView imageView_pop, background_image_pop;
    public RatingBar ratingBar_pop;
    public TextView title_pop;

    public PopularViewHolder(@NonNull View itemView, @NonNull View bgView, OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        title_pop = itemView.findViewById(R.id.movie_title_pop);
        imageView_pop = itemView.findViewById(R.id.movie_img_pop);
        background_image_pop = bgView.findViewById(R.id.background_imageView);
        ratingBar_pop = itemView.findViewById(R.id.rating_bar_pop);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
