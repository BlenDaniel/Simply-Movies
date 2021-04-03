package com.simple.movie.streaming.simplymovie.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.simple.movie.streaming.simplymovie.models.MovieModel;

import java.util.List;

public class MovieMultipleResponse {

    @SerializedName("total_results")
    @Expose()
    private int total_count;

    @SerializedName("results")
    @Expose()
    private List<MovieModel> movies;

    public int getTotal_count(){
        return total_count;
    }

    public List<MovieModel> getMovies(){
        return movies;
    }

    @Override
    public String toString() {
        return "MovieMultipleResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}';
    }




}
