package com.simple.movie.streaming.simplymovie.utils;

import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.response.MovieMultipleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //Search
    //https://api.themoviedb.org/3/movie?api_key={api_key}&query=Jack+Reacher
    @GET("/3/search/movie")
    Call<MovieMultipleResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page

    );

    //Search and get popular movies
    //https://api.themoviedb.org/3/movie/popular?api_key=b30c1e5a102c80d997c08acd56f6c112&page=1

    @GET("/3/movie/popular")
    Call<MovieMultipleResponse> getPopular(
      @Query("api_key") String key,
      @Query("page") int page
    );


    // Making Search with ID
    // https://api.themoviedb.org/3/movie/550?api_key=b30c1e5a102c80d997c08acd56f6c112

    @GET("3/movie/{movie_id}")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );
}
