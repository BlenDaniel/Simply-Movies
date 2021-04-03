package com.simple.movie.streaming.simplymovie.ui.views.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simple.movie.streaming.simplymovie.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    //Widgets
    TextView title, release_date, duration;
    ImageView imageView, background_image;
    RatingBar ratingBar;

    // Click Listeners
    OnMovieListener onMovieListener;




    public MovieViewHolder(@NonNull View itemView, @NonNull View bgView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        title = itemView.findViewById(R.id.movie_title);

        background_image = bgView.findViewById(R.id.background_imageView);
        //release_date = itemView.findViewById(R.id.movie_category);
        //duration = itemView.findViewById(R.id.movie_duration);

        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);

        
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
