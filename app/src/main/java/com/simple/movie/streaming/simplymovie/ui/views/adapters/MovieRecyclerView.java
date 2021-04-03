package com.simple.movie.streaming.simplymovie.ui.views.adapters;

import android.graphics.Typeface;
import android.net.wifi.hotspot2.pps.Credential;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simple.movie.streaming.simplymovie.R;
import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.ui.views.popularadapter.PopularViewHolder;
import com.simple.movie.streaming.simplymovie.utils.Credentials;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Request;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MovieRecyclerView extends RecyclerView.Adapter{

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;


    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                parent, false);
        return new MovieViewHolder(view, onMovieListener);


        */

       View view = null;
       View backgroundView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main,
                parent, false);




       if(viewType == DISPLAY_SEARCH){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                   parent, false);


            return  new MovieViewHolder(view,backgroundView, onMovieListener);
       }
       else{
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_list,
                   parent, false);

           return new PopularViewHolder(view, backgroundView, onMovieListener);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {



       /*
        ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
        ((MovieViewHolder)holder).release_date.setText(mMovies.get(i).getRelease_date());
        ((MovieViewHolder)holder).duration.setText(mMovies.get(i).getOriginal_language());

        // vote average is over 10, and our rating bar is over 5 stars
        ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(i).getVote_average())/2);

        // ImageView: Using Glide Library
        Glide.with(holder.itemView.getContext())
        .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                .into(((MovieViewHolder)holder).imageView);

        */

       int itemViewType = getItemViewType(i);
       if(itemViewType == DISPLAY_SEARCH){
           ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
           ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(i).getVote_average())/2);

           //ImageView using Glide Library

           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                   .into(((MovieViewHolder)holder).imageView);


           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                   .apply(bitmapTransform(new BlurTransformation(22)))
                   .into(((MovieViewHolder)holder).background_image);
       }else{
           ((PopularViewHolder)holder).title_pop.setText(mMovies.get(i).getTitle());
           ((PopularViewHolder)holder).ratingBar_pop.setRating((mMovies.get(i).getVote_average())/2);

           //ImageView using Glide Library

           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                   .into(((PopularViewHolder)holder).imageView_pop);
           Glide.with(holder.itemView.getContext())
                   .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                   .apply(bitmapTransform(new BlurTransformation(22)))
                   .into(((PopularViewHolder)holder).background_image_pop);
       }
    }

    @Override
    public int getItemCount() {
        if(mMovies!= null){
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<MovieModel> mMovies){
        this.mMovies = mMovies;
        notifyDataSetChanged();

    }





    // Getting the id of the movie clicked
    public MovieModel getSelectedMovie(int position){
        if(mMovies != null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position){
        if(Credentials.POPULAR){
           return DISPLAY_POP;
        }else{
            return DISPLAY_SEARCH;
        }
    }
}
