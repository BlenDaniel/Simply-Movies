package com.simple.movie.streaming.simplymovie.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.simple.movie.streaming.simplymovie.models.MovieModel;

//Single Movie
public class MovieResponse {
    // 1 Finding the movie object
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie(){
        return movie;
    }
}
