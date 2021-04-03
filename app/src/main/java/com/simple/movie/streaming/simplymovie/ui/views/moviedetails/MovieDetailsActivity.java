package com.simple.movie.streaming.simplymovie.ui.views.moviedetails;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simple.movie.streaming.simplymovie.R;
import com.simple.movie.streaming.simplymovie.models.MovieModel;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails = findViewById(R.id.imageView_details);
        titleDetails = findViewById(R.id.textView_title_details);
        descDetails = findViewById(R.id.textView_desc_detail);

        ratingBarDetails = findViewById(R.id.ratingBar_details);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if(getIntent().hasExtra("movie")){
            MovieModel movieModel = getIntent().getParcelableExtra("movie");

            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getOverview());
            ratingBarDetails.setRating((movieModel.getVote_average())/2);


            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/" + movieModel.getPoster_path()).into(imageViewDetails);

            Log.v("Tagy", "incoming intent " + movieModel.getTitle());
        }
    }
}

